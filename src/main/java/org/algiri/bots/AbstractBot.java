package org.algiri.bots;

import org.algiri.DataBase;
import org.algiri.model.Function;
import org.algiri.Parser;
import org.algiri.model.Lesson;
import org.algiri.model.User;
import org.algiri.model.UserData;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public interface AbstractBot {

    void send(String mes, long userId);
    void send(String mes, long userId, String[]... lines);
    String getName(long userId);



    LocalDateTime date1 = LocalDate.of(2022, 9, 5).atStartOfDay();
    List<String> GROUPNAME_LIST = new ArrayList<>(){{
        add("ис211");
        add("ис212");
        add("ис213");
        add("ис214");
        add("ис215");
        add("то221");
        add("то222");
        add("то223");
        add("ис223");
        add("ис224");
        add("ис225");
        add("ис226");
        add("ис227");
        add("лх221");
        add("лх222");
        add("лх223");
        add("лх224");
    }};
    default void bot(String mes, long userId) {
        boolean isNumerator = getNumerator();
        DataBase bd =  DataBase.getINSTANCE();
        UserData userData = bd.getUserData(userId);
        User user = new User(mes, userId);
        String[] line1 = {"Сегодня", "Завтра", "Неделя"};
        String[] line2 = {"Сбросить", "Донат"};
        // обработка регистрации пользователя
        try {
            String answer = user.register();
            if(answer!=null) {
                send(answer, userId, line1, line2);
                return;
            }

        } catch (Exception e) {
            send("Ошибка регистрации, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
            send(e.getMessage(), userId);
        }

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
        int today = LocalDate.now().getDayOfWeek().getValue()-1;
        Function function = null;
        // обработка команд
        try {
            switch (mes.toLowerCase()) {
                case "сегодня", "[club216410844|@parabots] сегодня", "/сегодня" -> {
                    // если беседа, то скип
                    if(userData.isConv() && mes.equalsIgnoreCase("сегодня"))
                        return;
                    // если сегодня воскресенье
                    if (today == 6) {
                        send("Сегодня отдыхаем", userId);
                        return;
                    }
                    function = Function.TODAY;
                }
                case "завтра", "[club216410844|@parabots] завтра", "/завтра" -> {
                    // если беседа, то скип
                    if(userData.isConv() &&  mes.equalsIgnoreCase("завтра"))
                        return;
                    if (today == 5) {
                        send("Завтра воскресенье, пар нет", userId);
                        return;
                    }
                    function = Function.TOMORROW;
                    if (today == 6) {
                        function = Function.TOMORROW_SUNDAY;
                        today = -1;
                    }
                    today++;
                }
                case "неделя", "[club216410844|@parabots] неделя", "/неделя" -> {
                    if(userData.isConv() && mes.equalsIgnoreCase("неделя"))
                        return;
                    function = Function.THIS_WEEK;
                    if (today == 6) {
                        function = Function.NEXT_WEEK;
                    }
                }
                case "сбросить", "[club216410844|@parabots] сбросить", "/сбросить" -> {
                    if(userData.isConv() && mes.equalsIgnoreCase("сбросить"))
                        return;
                    bd.updateUsersData(userId, "?");
                    send("Ваша группа сброшена, вы можете установить новую", userId);
                }
                case "донат", "[club216410844|@parabots] донат", "/донат" ->
                        send("""
                                Вы бы могли поддержать проект материально, это поможет ему развиваться дальше
                                Сбер: 2202203299972713
                                Qiwi: zrezlorez (по нику)""", userId);

            }
            if(function!=null) {
                List<Lesson> info = bd.getTimeTableData(isNumerator, today, userData.getGroup(), function);
                String timetable = getStringTimetable(info, function);
                if(timetable.isEmpty()) {
                    send("По вашему запросу нет пар", userId);
                    return;
                }
                send(timetable, userId, line1, line2);
            }


        } catch (Exception e) {
            send("Ошибка выполнения команд, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
            send(e.getMessage(), userId);
        }
    }


    private static String getStringTimetable(List<Lesson> list, Function function) {
        StringBuilder result = new StringBuilder();
        int day = -1;
        for (Lesson lesson : list) {
            if ((function == Function.THIS_WEEK || function == Function.NEXT_WEEK) && day != lesson.getDay()) {
                day = lesson.getDay();
                result.append(Parser.days.get(lesson.getDay()).trim()).append("\n\n");
            }
            result.append(String.format("%s - %s: %s\n\n",
                    lesson.getTimeStart(),
                    lesson.getTimeEnd(),
                    lesson.getName()));
        }

        return result.toString();
    }

    private static boolean getNumerator() {
        LocalDateTime date2 = LocalDate.now().atStartOfDay();
        return Duration.between(date1, date2).toDays()/7%2==0;
    }
}