package org.algiri;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;



public abstract class AbstractBot {

    public abstract void send(String mes, long userId);
    public abstract void send(String mes, long userId, Object keyboard);
    public abstract Object createKeyboard(String[] s_line1, String[] s_line2);
    public abstract String getName(long userId);
    public abstract void run();




    private static final GregorianCalendar numeratorDate = new GregorianCalendar(2022, Calendar.SEPTEMBER, 5);
    private static final List<String> GROUPNAME_LIST = new ArrayList<>(){{
        add("is211");
        add("ис211");
        add("is212");
        add("ис212");
        add("is213");
        add("ис213");
        add("is214");
        add("ис214");
        add("is215");
        add("ис215");
    }};

    public void bot(String mes, long userId, DataBase bd, boolean isVk){
        List<String> res = bd.getUserData("WHERE id = " + userId);
        var keyboard= createKeyboard(new String[]{"Сейчас"}, new String[]{"Сегодня", "Завтра"});
        try {
            if (res.isEmpty()) {
                bd.insertUsersData(userId, getName(userId), isVk);
                res = bd.getUserData("WHERE id = " + userId);
            }
            if (res.get(1).equals("?")) {
                if (GROUPNAME_LIST.contains(mes.toLowerCase(Locale.ROOT).replace("\s", ""))) {
                    bd.updateUsersData(userId, mes.toLowerCase().replace("\s", ""));
                    send("Ваша группа установлена", userId, keyboard);
                } else {
                    send("Введите свою группу (например: ис211)", userId);
                    return;
                }
            }

        } catch (Exception e) {
            send("Ошибка регистрации, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
        }


        int today = LocalDate.now().getDayOfWeek().getValue()-1;
        Date now = new Date();
        boolean isCommand = false;
        String sql = "";
        try {
            switch (mes.toLowerCase()) {
                case "сейчас" -> {
                    if (today == 6) {
                        send("Сейчас, по расписанию, законный выходной", userId);
                        return;
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    String timeNow = formatter.format(now);
                    sql = String.format("WHERE \"isNumerator\" = %b and day = %d and ((timeend > '%s' and timestart < '%s') or (timestart>'%s' and timeend >'%s')) ORDER BY timestart LIMIT 2",
                            isNumerator(now, numeratorDate.getTime()), today, timeNow, timeNow, timeNow, timeNow);
                    isCommand = true;
                }
                case "сегодня" -> {
                    if (today == 6) {
                        send("Сегодня воскресенье, какие пары?", userId);
                        return;
                    }
                    sql = String.format("WHERE \"isNumerator\" = %b and day = %d ORDER BY timestart",
                            isNumerator(now, numeratorDate.getTime()), today);
                    isCommand = true;
                }
                case "завтра" -> {
                    if (today == 5) {
                        send("Завтра воскресенье, отдыхай", userId);
                        return;
                    }
                    if (today == 6)
                        today = -1;
                    sql = String.format("WHERE \"isNumerator\" = %b and day = %d ORDER BY timestart",
                            isNumerator(now, numeratorDate.getTime()), today+1);
                    isCommand = true;
                }

                case "поменять группу", "сменить группу", "сбросить группу", "сбросить" -> {
                    bd.updateUsersData(userId, "?");
                    send("Ваша группа сброшена, вы можете установить новую", userId);
                }

            }
            if(isCommand){
                List<String> info = bd.getTimeTableData(sql);
                if(info.isEmpty())
                    send("Сейчас пар нет", userId);
                send(getStringTimetable(info, res.get(1)), userId, keyboard);
            }


        } catch (Exception e) {
            e.printStackTrace();
            send("Ошибка выполнения команд, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
        }
    }



    private static String getStringTimetable(List<String> list, String group) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i<list.size(); i+=9) {
            if(list.get(i+getIntByGroup(group)).equals("-"))
                continue;
            result.append(String.format("%s - %s: %s\n\n", list.get(i+1).substring(0, 5), list.get(i+2).substring(0, 5), list.get(i+getIntByGroup(group))));
        }

        return result.toString();
    }

    private static int getIntByGroup(String group){
        return switch (group) {
            case "is211", "ис211" -> 3;
            case "is212", "ис212" -> 4;
            case "ис213", "is213" -> 5;
            case "ис214", "is214" -> 6;
            case "ис215", "is215" -> 7;
            default -> 0;
        };
    }

    private boolean isNumerator(Date d1, Date d2) {
        return ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24)) % 2 == 1;
    }
}
