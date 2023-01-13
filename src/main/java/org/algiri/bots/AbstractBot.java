package org.algiri.bots;

import org.algiri.data.DataBase;
import org.algiri.model.Function;
import org.algiri.data.Parser;
import org.algiri.model.Lesson;
import org.algiri.data.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public interface AbstractBot {

    void send(String mes, long userId);
    void send(String mes, int messageId, long userId, String[]... lines);
    String getName(long userId);



    LocalDateTime date1 = LocalDate.of(2022, 9, 5).atStartOfDay();
    default void bot(String mes, int messageId, long userId) {
        if(mes==null) return;
        DataBase bd =  DataBase.getINSTANCE();
        User user = new User(mes, userId);
        String[] line1 = {"Сегодня", "Завтра", "Неделя"};
        String[] line2 = {"Сбросить", "Донат", "Удалить"};
        // обработка регистрации пользователя
        try {
            if(user.getUserData().getId() == 0) {
                user.register();
            }
            if(user.getUserData().getGroupId() < 2) {
                send(user.changeGroup(), messageId, userId, line1, line2);
                return;
            }

        } catch (Exception e) {
            send("Ошибка регистрации, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
            send(e.getMessage(), userId);
        }

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
        int today = LocalDate.now().getDayOfWeek().getValue()-1;
        Function function = null;
        String teacherName = "";
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
                    var message = user.getMessage().split(" ");
                    if(message.length<2) {
                        send("Пожалуйста, после команды препод укажите его фамилию. Пример: /препод Силонов", messageId, userId);
                        return;
                    }
                    teacherName = message[1];
                    function = Function.TEACHER;
                }
                case "удалить" -> {
                    send("Ваша клавиатура удалена", messageId, userId, new String[]{"0"}, new String[]{"0"});
                    return;
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
                List<Lesson> info = bd.getTimeTable(getNumerator(), user.getUserData().getGroupId());
                String timetable = getStringTimetable(info, function, today, teacherName);
                if(timetable.isEmpty()) {
                    send("По вашему запросу нет пар", userId);
                    return;
                }
                send(timetable, messageId, userId, line1, line2);
            }
        } catch (Exception e) {
            send("Ошибка выполнения команд, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
            send(e.getMessage(), userId);
        }
    }


    private static String getStringTimetable(List<Lesson> list, Function function, int today, String teacherName) {
        StringBuilder result = new StringBuilder();
        int day = -1;
        String str;
        for (Lesson lesson : list) {
            switch (function) {
                case TEACHER -> {
                    if((lesson.getDay()==today || lesson.getDay()==today+1) && lesson.getTeacher().toLowerCase().contains(teacherName)) {
                        if(day!=lesson.getDay()){
                            day = lesson.getDay();
                            result.append(Parser.days.get(day)).append("\n\n");
                        }
                        str = String.format("Группа %s, %s-%s: %s\n\n",
                                lesson.getGroup(),
                                lesson.getTimeStart(),
                                lesson.getTimeEnd(),
                                lesson.getName());
                        if(result.substring(result.lastIndexOf(",")+1).contains(lesson.getName())) {
                            result.insert(result.lastIndexOf(","), ", " + lesson.getGroup());
                        }
                        else result.append(str);
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