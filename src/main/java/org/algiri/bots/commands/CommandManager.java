package org.algiri.bots.commands;

import org.algiri.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private static final List<AbstractCommands> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new Today());
        commands.add(new Tomorrow());
        commands.add(new Week());
    }

    public String execute(String input, User user) {
        for (AbstractCommands command : commands) {
            if (command.getCommand().equals(input))
                return command.execute(user);
        }

        return null;
    }
}
