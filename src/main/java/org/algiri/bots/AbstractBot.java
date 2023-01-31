package org.algiri.bots;

import org.algiri.bots.commands.CommandManager;
import org.algiri.model.User;


public interface AbstractBot {

    void send(String mes, long userId);

    void send(String mes, int messageId, long userId, String[]... lines);

    String getName(long userId);

    CommandManager commandManager = new CommandManager();

    default void bot(String mes, int messageId, long userId) {
        if (mes == null) return;
        User user = new User(mes, userId);
        String[] line1 = {"Сегодня", "Завтра", "Неделя"};
        String[] line2 = {"Сбросить", "Донат", "Удалить"};
        // обработка регистрации пользователя
        try {
            if (user.getUserData().getId() == 0) {
                user.register();
            }
            if (user.getUserData().getGroupId() < 2) {
                send(user.changeGroup(), messageId, userId, line1, line2);
                return;
            }
        } catch (Exception e) {
            send("Ошибка регистрации, пожалуйста, обратитесь к разработчику - vk.com/zrezlorez", userId);
            send(e.getMessage(), userId);
        }


        // обработка команд
        String result = commandManager.execute(user.getMessage(), user);
        if (result == null) return;
        if (result.isEmpty()) {
            send("По вашему запросу нет пар", messageId, userId, line1, line2);
            return;
        }
        send(result, messageId, userId, line1, line2);
    }
}