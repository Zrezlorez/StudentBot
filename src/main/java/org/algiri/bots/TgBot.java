package org.algiri.bots;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.algiri.DataBase;

import static org.algiri.Main.bot;

@RequiredArgsConstructor
public class TgBot implements AbstractBot {
    private final DataBase bd;


    public void run(){
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();
        if(message==null) return;
        send(String.format("%s написал %s", getName(message.chat().id()), message.text()), 1827409284);
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
        for (int i=0; i<2; i++) {
            s_line1[i] = "/" + s_line1[i];
            s_line2[i] = "/" + s_line2[i];
        }
        return new ReplyKeyboardMarkup(
                s_line1,
                s_line2)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);
    }
    @SneakyThrows
    @Override
    public String getName(long userId) {
        var result = bot.execute(new GetChat(userId));
        if (result.chat().username()!=null)
            return result.chat().username();
        if (result.chat().title()!=null)
            return "беседа тг: " + result.chat().title();
        return "тг: " +
                result.chat().firstName() +
                " " +
                (result.chat().lastName()==null ? "" : result.chat().lastName());
    }
}