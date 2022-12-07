package org.algiri.bots;

import org.algiri.DataBase;
import org.algiri.model.Function;
import org.algiri.Parser;
import org.algiri.model.Lesson;
import org.algiri.model.User;

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
        String pr = "";
        // обработка команд
        try {
            switch (user.getMessage().split(" ")[0]) {
                case "сегодня" -> {
                    // если беседа, то скип
                    if(user.isConv() && mes.equalsIgnoreCase("сегодня"))
                        return;
                    // если сегодня воскресенье
                    if (today == 6) {
                        send("Сегодня отдыхаем", userId);
                        return;
                    }
                    function = Function.TODAY;
                }
                case "завтра" -> {
                    // если беседа, то скип
                    if(user.isConv() &&  mes.equalsIgnoreCase("завтра"))
                        return;
                    if (today == 5) {
                        send("Завтра воскресенье, пар нет", userId);
                        return;
                    }
                    function = Function.TOMORROW;
                    if (today == 6) {
                        function = Function.TOMORROW_SUNDAY;
                    }
                }
                case "неделя" -> {
                    if(user.isConv() && mes.equalsIgnoreCase("неделя"))
                        return;
                    function = Function.THIS_WEEK;
                    if (today == 6) {
                        function = Function.NEXT_WEEK;
                    }
                }
                case "препод" -> {
                    pr = user.getMessage().split(" ")[1];
                    function = Function.TEACHER;
                }
                case "сбросить" -> {
                    if(user.isConv() && mes.equalsIgnoreCase("сбросить"))
                        return;
                    bd.updateUsersData(userId, "?");
                    send("Ваша группа сброшена, вы можете установить новую", userId);
                }
                case "донат" ->
                        send("""
                                Вы бы могли поддержать проект материально, это поможет ему развиваться дальше
                                Сбер: 2202203299972713
                                Qiwi: zrezlorez (по нику)""", userId);

            }
            if(function!=null) {
                List<Lesson> info = bd.getTimeTableData(isNumerator, function, user.getUserData().getGroup());
                String timetable = getStringTimetable(info, function, today, pr);
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


    private static String getStringTimetable(List<Lesson> list, Function function, int today, String pr) {
        StringBuilder result = new StringBuilder();
        int day = -1;
        String str = "";
        for (Lesson lesson : list) {
            switch (function) {
                case TEACHER -> {
                    if(lesson.getDay()==today || lesson.getDay() == today+1 && lesson.getTeacher().toLowerCase().contains(pr)) {
//                        StringBuilder result = new StringBuilder();
//                        String is213 = "Группа ис213: 8:30-10:10: Алгоритмы\n\n";
//                        String is214 = "Группа ис214: 8:30-10:10: Алгоритмы\n\n";
//                        String is215 = "Группа ис215: 8:30-10:10: Алгоритмы\n\n";
//                        result.append(is213);
//                        if(is214.contains(is213.substring(is213.indexOf(": 8:30")))) {
//                            result.insert(is214.indexOf(": 8:30"), ", ис214");
//                        }
//                        else result.append(is214);
//
//                        result.append(is215);
                        str = String.format("Группа %s, %s-%s: %s\n\n",
                                lesson.getGroup(),
                                lesson.getTimeStart(),
                                lesson.getTimeEnd(),
                                lesson.getName());

                    }
                }
                case TODAY -> {
                    if(lesson.getDay()==today) {
                        addLesson(result, lesson);
                    }
                }
                case TOMORROW -> {
                    if(lesson.getDay()==today+1) {
                        addLesson(result, lesson);
                    }
                }
                case TOMORROW_SUNDAY -> {
                    if(lesson.getDay()==0)
                        addLesson(result, lesson);
                }
                case THIS_WEEK, NEXT_WEEK -> {
                    if(lesson.getDay() != day) {
                        day = lesson.getDay();
                        result.append(Parser.days.get(lesson.getDay()).trim()).append("\n\n");
                    }
                    addLesson(result, lesson);
                }
            }
        }
        return result.toString();
    }

    private static void addLesson(StringBuilder result, Lesson lesson) {
        result.append(String.format("%s - %s: %s\n\n",
                lesson.getTimeStart(),
                lesson.getTimeEnd(),
                lesson.getName()));
    }

    private static boolean getNumerator() {
        LocalDateTime date2 = LocalDate.now().atStartOfDay();
        return Duration.between(date1, date2).toDays()/7%2==0;
    }
}