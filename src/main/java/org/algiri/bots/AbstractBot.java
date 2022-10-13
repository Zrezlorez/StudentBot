package org.algiri.bots;

import org.algiri.DataBase;
import org.algiri.Parser;

import java.time.LocalDate;
import java.util.*;



public abstract class AbstractBot {

    public abstract void send(String mes, long userId);
    public abstract void send(String mes, long userId, Object keyboard);
    public abstract Object createKeyboard(String[] s_line1, String[] s_line2);
    public abstract String getName(long userId);
    public abstract void run();




    private static final GregorianCalendar numeratorDate = new GregorianCalendar(2022, Calendar.SEPTEMBER, 5);
    public static final List<String> GROUPNAME_LIST = new ArrayList<>(){{
        add("��211");
        add("��212");
        add("��213");
        add("��214");
        add("��215");
    }};

    public void bot(String mes, long userId, DataBase bd){
        List<String> res = bd.getUserData(userId);
        var keyboard= createKeyboard(new String[]{"�������", "������"}, new String[]{"������", "��������"});
        try {
            if (res.isEmpty()) {
                res = bd.insertUsersData(userId, "?", getName(userId));
            }
            if (res.get(1).equals("?")) {
                if (GROUPNAME_LIST.contains(mes.toLowerCase().replace("\s", ""))) {
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
        int function = -1;
        try {
            switch (mes.toLowerCase()) {
                case "�������" -> {
                    if (today == 6) {
                        send("������� ��������", userId);
                        return;
                    }
                    function = 1;
                }
                case "������" -> {
                    if (today == 5) {
                        send("������ �����������, ��� ���", userId);
                        return;
                    }
                    if (today == 6)
                        today = -1;
                    function = 1;
                    today++;
                }
                case "������" -> function = 2;
                case "������� ������", "�������� ������", "��������", "�������" -> {
                    bd.updateUsersData(userId, "?");
                    send("���� ������ ��������, �� ������ ���������� �����", userId);
                }

            }
            if(function>-1){
                List<String> info = bd.getTimeTableData(isNumerator(now, numeratorDate.getTime()), today, res.get(1), function);
                String timetable = getStringTimetable(info, function);
                if(timetable.isEmpty()) {
                    send("������ ��� ���", userId);
                    return;
                }
                send(timetable, userId, keyboard);
            }


        } catch (Exception e) {
            send("������ ���������� ������, ����������, ���������� � ������������ - vk.com/zrezlorez", userId);
        }
    }



    private static String getStringTimetable(List<String> list, int function) {
        StringBuilder result = new StringBuilder();
        int oldday = -1;
        for (int i = 0; i<list.size(); i+=6) {
            if(function==2 && Integer.parseInt(list.get(i))!=oldday) {
                oldday++;
                result.append(Parser.days.get(oldday).trim()).append("\n\n");
            }
            result.append(String.format("%s - %s: %s\n\n", list.get(i+1), list.get(i+2), list.get(i+3)));
        }

        return result.toString();
    }
    private boolean isNumerator(Date d1, Date d2) {
        return ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24)) % 2 == 1;
    }
}