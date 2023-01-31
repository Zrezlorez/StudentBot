package org.algiri.bots.commands;

import org.algiri.data.DataBase;
import org.algiri.data.Parser;
import org.algiri.model.User;
import org.algiri.model.Lesson;
import org.algiri.utils.Util;

import java.time.LocalDate;
import java.util.List;

import static org.algiri.utils.Util.addLesson;

public class Week extends AbstractCommands{
    public Week() {
        super("неделя");
    }

    @Override
    public String execute(User user) {
        int day = -1;
        boolean isNumerator = Util.getNumerator();
        if(LocalDate.now().getDayOfWeek().getValue()-1 > 4) isNumerator = !isNumerator;
        StringBuilder result = new StringBuilder();
        List<Lesson> info = DataBase.getINSTANCE().getTimeTable(user.getUserData().getGroupId());
        for (Lesson lesson : info) {
            if(lesson.isNumerator() != isNumerator) continue;
            if(lesson.getDay() != day) {
                day = lesson.getDay();
                result.append(Parser.days.get(lesson.getDay()).trim()).append("\n\n");
            }
            addLesson(result, lesson);
        }
        return result.toString();
    }
}
