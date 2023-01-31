package org.algiri.bots.commands;

import org.algiri.model.User;
import java.time.LocalDate;
import static org.algiri.utils.Util.getLessonsByDay;

public class Today extends AbstractCommands {

    public Today() {
        super("сегодня");
    }

    @Override
    public String execute(User user) {
        return getLessonsByDay(user.getUserData().getGroupId(), LocalDate.now().getDayOfWeek().getValue() - 1).toString();
    }
}
