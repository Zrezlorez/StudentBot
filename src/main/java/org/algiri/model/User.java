package org.algiri.model;


import lombok.Getter;
import org.algiri.DataBase;

import static org.algiri.bots.AbstractBot.GROUPNAME_LIST;


public class User {
    @Getter
    UserData userData;
    @Getter
    String message;
    String name;
    long id;
    @Getter
    boolean isConv;

    public User(String message, long id) {
        this.message = message.toLowerCase()
                .replace("[club216410844|@parabots] ", "")
                .replace("/", "");
        this.id = id;
        userData = DataBase.getINSTANCE().getUserData(id);
        isConv = id<0 || (id>2000000004 && id<2000000100);
    }

    public String register() {
        if (userData.isNull()) {
            userData = DataBase.getINSTANCE().insertUsersData(id, "?", name);
        }
        if (!userData.isHaveGroup()) {
            if (!message.isEmpty() && GROUPNAME_LIST.contains(message.replace("\s", ""))) {
                DataBase.getINSTANCE().updateUsersData(id, message.replace("\s", ""));
                return "Ваша группа установлена";
            }
            return "Введите свою группу (например: ис211)";
        }
        return null;
    }
}
