package org.algiri.bots;

import org.algiri.DataBase;
import org.algiri.Parser;

import java.time.LocalDate;
import java.util.*;



public interface AbstractBot {

    void send(String mes, long userId);
    void send(String mes, long userId, Object keyboard);
    Object createKeyboard(String[] s_line1, String[] s_line2);
    String getName(long userId);

    /* что такое func
    * 1 = сегодня завтра
    * 2 = эта неделя
    * 3 = завтра если сегодня вс
    * 4 = след неделя
    */


    GregorianCalendar numeratorDate = new GregorianCalendar(2022, Calendar.SEPTEMBER, 5);
    List<String> GROUPNAME_LIST = new ArrayList<>(){{
        add("ис211");
        add("ис212");
        add("ис213");
        add("ис214");
        add("ис215");
        add("то221");
        add("то222");
        add("то223");
        add("ис224");
        add("ис225");
    }};
    default void bot(String mes, long userId, DataBase bd){
        List<String> res = bd.getUserData(userId);
        // создаю клавиатуру
        var keyboard= createKeyboard(
                new String[]{"Сегодня", "Завтра", "Неделя"},
                new String[]{"Сбросить", "Донат"});
        // обработка регистрации пользователя
        try {
            // если пользоват еля нет в бд
            if (res.isEmpty()) {
                res = bd.insertUsersData(userId, "?", getName(userId));
            }
            // если группа не установлена
            if (res.get(1).equals("?")) {
                long convId = Long.parseLong(res.get(0));
                if (!mes.isEmpty() && GROUPNAME_LIST.contains(mes.toLowerCase().replace("\s", ""))) {
                    bd.updateUsersData(userId, mes.toLowerCase().replace("\s", ""));
                    send("Ваша группа установлена", userId, keyboard);
                } else if(convId>2000001000 || convId<2000000000 || Long.parseLong(res.get(0))<0) {
                    send("Введите свою группу (например: ис211)", userId);
                    return;
                }
            }

        } catch (Exception e) {
            send("Ошибка регистрации, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
            send(e.getMessage(), userId);
        }

        boolean isConv = Long.parseLong(res.get(0))<0;
        int today = LocalDate.now().getDayOfWeek().getValue()-1;
        Date now = new Date();
        int function = -1;
        // обрабокта комманд
        try {
            switch (mes.toLowerCase()) {
                case "сегодня", "[club216410844|@parabots] сегодня", "/сегодня" -> {
                    // если беседа, то скип
                    if(isConv && mes.equalsIgnoreCase("сегодня"))
                        return;
                    // если сегодня воскресенье
                    if (today == 6) {
                        send("Сегодня отдыхаем", userId);
                        return;
                    }
                    function = 1;
                }
                case "завтра", "[club216410844|@parabots] завтра", "/завтра" -> {
                    // если беседа то скип
                    if(isConv &&  mes.equalsIgnoreCase("завтра"))
                        return;
                    if (today == 5) {
                        send("Завтра воскресенье, пар нет", userId);
                        return;
                    }
                    function = 1;
                    if (today == 6) {
                        function = 3;
                        today = -1;
                    }

                    today++;
                }
                case "неделя", "[club216410844|@parabots] неделя", "/неделя" -> {
                    if(isConv && mes.equalsIgnoreCase("неделя"))
                        return;
                    function = 2;
                    if (today == 6) {
                        function = 4;
                        today = -1;
                    }
                }
                case "сбросить", "[club216410844|@parabots] сбросить", "/сбросить" -> {
                    if(isConv && mes.equalsIgnoreCase("сбросить"))
                        return;
                    bd.updateUsersData(userId, "?");
                    send("Ваша группа сброшена, вы можете установить новую", userId);
                }
                case "донат", "[club216410844|@parabots] донат", "/донат" ->
                        send("Вы бы могли поддержать проект материально, это поможет проекту развиваться дальше\n" +
                        "Сбер: 2202203299972713", userId);
            }

            if(function>0){
                // получаю расписание в виде списка
                List<String> info = bd.getTimeTableData(isNumerator(now, numeratorDate.getTime()), today, res.get(1), function);
                // создаю выходную строку с расписанием
                String timetable = getStringTimetable(info, function);
                if(timetable.isEmpty()) {
                    send("Вы не установили группу или по вашему запросу нет пар", userId);
                    return;
                }
                send(timetable, userId, keyboard);
            }


        } catch (Exception e) {
            send("Ошибка выполнения команд, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
            send(e.getMessage(), userId);
        }
    }



    private static String getStringTimetable(List<String> list, int function) {
        StringBuilder result = new StringBuilder();
        int day = -1;
        for (int i = 0; i<list.size(); i+=6) {
            if(function%2==0 && day!=Integer.parseInt(list.get(i))) {
                day = Integer.parseInt(list.get(i));
                result.append(Parser.days.get(day).trim()).append("\n\n");
            }
            result.append(String.format("%s - %s: %s\n\n", list.get(i+1), list.get(i+2), list.get(i+3)));
        }

        return result.toString();
    }
    private boolean isNumerator(Date d1, Date d2) {
        return (((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24)))/7 % 2 == 0;
    }
}