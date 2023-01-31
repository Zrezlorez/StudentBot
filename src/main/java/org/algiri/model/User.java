package org.algiri.model;


import lombok.Getter;
import org.algiri.data.DataBase;


public class User {
    @Getter
    private UserData userData;
    @Getter
    private final String message;
    private final long id;
    @Getter
    private final boolean isConv;

    public User(String message, long id) {
        this.message = message.toLowerCase()
                .replace("[club216410844|@parabots] ", "")
                .replace("/", "")
                .trim();
        this.id = id;
        userData = DataBase.getINSTANCE().getUserData(id);
        isConv = id < 0 || (id > 2000000000 && id < 2000001000);
    }

    public void register() {
        userData = DataBase.getINSTANCE().insertUserData(id, "?", "name");
    }

    public String changeGroup() {
        if (!message.isEmpty() && DataBase.getINSTANCE().getGroups().contains(message.replace("\s", ""))) {
            DataBase.getINSTANCE().updateUsersData(id, message.replace("\s", ""));
            return "Ваша группа установлена";
        }
        return "Перед использованием бота, вам нужно ввести свою группу (например: ис211)";
    }
}
