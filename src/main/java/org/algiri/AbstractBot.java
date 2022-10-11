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
        add("��211");
        add("is212");
        add("��212");
        add("is213");
        add("��213");
        add("is214");
        add("��214");
        add("is215");
        add("��215");
    }};

    public void bot(String mes, long userId, DataBase bd, boolean isVk){
        List<String> res = bd.getUserData("WHERE id = " + userId);
        var keyboard= createKeyboard(new String[]{"������"}, new String[]{"�������", "������"});
        try {
            if (res.isEmpty()) {
                bd.insertUsersData(userId, getName(userId), isVk);
                res = bd.getUserData("WHERE id = " + userId);
            }
            if (res.get(1).equals("?")) {
                if (GROUPNAME_LIST.contains(mes.toLowerCase(Locale.ROOT).replace("\s", ""))) {
                    bd.updateUsersData(userId, mes.toLowerCase().replace("\s", ""));
                    send("���� ������ �����������", userId, keyboard);
                } else {
                    send("������� ���� ������ (��������: ��211)", userId);
                    return;
                }
            }

        } catch (Exception e) {
            send("������ �����������, ����������, ���������� � ������������ - vk.com/zrezlorez", userId);
        }


        int today = LocalDate.now().getDayOfWeek().getValue()-1;
        Date now = new Date();
        boolean isCommand = false;
        String sql = "";
        try {
            switch (mes.toLowerCase()) {
                case "������" -> {
                    if (today == 6) {
                        send("������, �� ����������, �������� ��������", userId);
                        return;
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    String timeNow = formatter.format(now);
                    sql = String.format("WHERE \"isNumerator\" = %b and day = %d and ((timeend > '%s' and timestart < '%s') or (timestart>'%s' and timeend >'%s')) ORDER BY timestart LIMIT 2",
                            isNumerator(now, numeratorDate.getTime()), today, timeNow, timeNow, timeNow, timeNow);
                    isCommand = true;
                }
                case "�������" -> {
                    if (today == 6) {
                        send("������� �����������, ����� ����?", userId);
                        return;
                    }
                    sql = String.format("WHERE \"isNumerator\" = %b and day = %d ORDER BY timestart",
                            isNumerator(now, numeratorDate.getTime()), today);
                    isCommand = true;
                }
                case "������" -> {
                    if (today == 5) {
                        send("������ �����������, �������", userId);
                        return;
                    }
                    if (today == 6)
                        today = -1;
                    sql = String.format("WHERE \"isNumerator\" = %b and day = %d ORDER BY timestart",
                            isNumerator(now, numeratorDate.getTime()), today+1);
                    isCommand = true;
                }

                case "�������� ������", "������� ������", "�������� ������", "��������" -> {
                    bd.updateUsersData(userId, "?");
                    send("���� ������ ��������, �� ������ ���������� �����", userId);
                }

            }
            if(isCommand){
                List<String> info = bd.getTimeTableData(sql);
                if(info.isEmpty())
                    send("������ ��� ���", userId);
                send(getStringTimetable(info, res.get(1)), userId, keyboard);
            }


        } catch (Exception e) {
            e.printStackTrace();
            send("������ ���������� ������, ����������, ���������� � ������������ - vk.com/zrezlorez", userId);
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
            case "is211", "��211" -> 3;
            case "is212", "��212" -> 4;
            case "��213", "is213" -> 5;
            case "��214", "is214" -> 6;
            case "��215", "is215" -> 7;
            default -> 0;
        };
    }

    private boolean isNumerator(Date d1, Date d2) {
        return ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24)) % 2 == 1;
    }
}
