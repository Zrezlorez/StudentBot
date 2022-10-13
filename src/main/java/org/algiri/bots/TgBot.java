package org.algiri.bots;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.algiri.DataBase;

import static org.algiri.Main.bot;

@RequiredArgsConstructor
public class TgBot extends AbstractBot {
    private final DataBase bd;


    @Override
    public void run(){
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();
        if(message==null) return;
        bot(message.text(), message.chat().id(), bd);

    }

    @Override
    public void send(String mes, long userId) {
        BaseRequest<SendMessage, SendResponse> request = new SendMessage(userId, mes);
        bot.execute(request);
    }

    @Override
    public void send(String mes, long userId, Object keyboard) {
        BaseRequest<SendMessage, SendResponse> request = new SendMessage(userId, mes).replyMarkup((Keyboard) keyboard);
        bot.execute(request);
    }
    @Override
    public Keyboard createKeyboard(String[] s_line1, String[] s_line2) {
        return new ReplyKeyboardMarkup(
                s_line1,
                s_line2)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);
    }

    @SneakyThrows
    @Override
    public String getName(long userId) {
        return "пользователь тг";
    }
}