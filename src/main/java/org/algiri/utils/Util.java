package org.algiri.utils;

import org.algiri.data.DataBase;
import org.algiri.model.Lesson;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Util {

    public static StringBuilder getLessonsByDay(int groupId, int day) {
        boolean isNumerator = Util.getNumerator();
        StringBuilder result = new StringBuilder();
        List<Lesson> info = DataBase.getINSTANCE().getTimeTable(groupId);
        for (Lesson lesson : info) {
            if(lesson.isNumerator() == isNumerator && lesson.getDay() == day)
                Util.addLesson(result, lesson);
        }
        return result;
    }
    public static void addLesson(StringBuilder result, Lesson lesson) {
        result.append(String.format("%s - %s: %s\n\n",
                lesson.getTimeStart(),
                lesson.getTimeEnd(),
                lesson.getName()));
    }

    public static boolean getNumerator() {
        LocalDateTime date2 = LocalDate.now().atStartOfDay();
        return Duration.between(LocalDate.of(2022, 9, 5).atStartOfDay(), date2).toDays() / 7 % 2 != 0;
    }
}
