package org.algiri.bots.commands;

import org.algiri.model.User;

public abstract class AbstractCommands {


    private final String command;

    public AbstractCommands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public abstract String execute(User user);
}
