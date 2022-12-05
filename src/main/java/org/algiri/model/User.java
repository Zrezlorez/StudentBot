package org.algiri.model;


import org.algiri.DataBase;

import static org.algiri.bots.AbstractBot.GROUPNAME_LIST;


public class User {
    UserData userData;
    String message;
    String name;
    long id;

    public User(String message, long id) {
        this.message = message.toLowerCase().replace("\s", "");
        this.id = id;
        userData = DataBase.getINSTANCE().getUserData(id);
    }

    public String register() {
        if (userData.isNull()) {
            userData = DataBase.getINSTANCE().insertUsersData(id, "?", name);
        }
        if (!userData.isHaveGroup()) {
            if (!message.isEmpty() && GROUPNAME_LIST.contains(message)) {
                DataBase.getINSTANCE().updateUsersData(id, message);
                return "Ваша группа установлена";
            }
            return "Введите свою группу (например: ис211)";
        }
        return null;
    }
}
