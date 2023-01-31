package org.algiri.bots.commands;

import org.algiri.model.User;

import java.time.LocalDate;

import static org.algiri.utils.Util.getLessonsByDay;

public class Tomorrow extends AbstractCommands{
    public Tomorrow() {
        super("завтра");
    }

    @Override
    public String execute(User user) {
        int today = LocalDate.now().getDayOfWeek().getValue()-1;
        if(today == 5)
            return "Завтра воскресенье, пар нет";
        if(today == 6)
            return getLessonsByDay(user.getUserData().getGroupId(), 0).toString();
        return getLessonsByDay(user.getUserData().getGroupId(), today+1).toString();
    }
}
