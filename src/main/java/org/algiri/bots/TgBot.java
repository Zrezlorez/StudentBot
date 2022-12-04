package org.algiri.bots;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.SneakyThrows;
import static org.algiri.Main.*;

public class TgBot implements AbstractBot {
    public static final TelegramBot bot = new TelegramBot(getToken());
    public void run(){
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void process(Update update) {
        Message message = update.message();
        if(message==null) return;
        if(message.chat().id()!=1827409284)
            send(String.format("%s написал %s", getName(message.chat().id()), message.text()), 1827409284);
        bot(message.text(), message.chat().id());
    }

    private static String getToken() {
        if(isTest)  return "1116496780:AAH8HZ8kDNoSQW3LNXKM8ladh434hCJfEls";
        return "5524044091:AAHe9Wt7GHqfjf4_mEVKG-WbaDcIEUkF2IY";

    }

    @Override
    public void send(String mes, long userId) {
        BaseRequest<SendMessage, SendResponse> request = new SendMessage(userId, mes);
        bot.execute(request);
    }
    @Override
    public void send(String mes, long userId, String[]... lines) {
        BaseRequest<SendMessage, SendResponse> request = new SendMessage(userId, mes).
                replyMarkup(createKeyboard(lines[0], lines[1]));
        bot.execute(request);
    }
    public Keyboard createKeyboard(String[] line1, String[] line2) {
        for (int i=0; i< line1.length; i++) {
            line1[i] = "/" + line1[i];
        }
        for (int i=0; i< line2.length; i++) {
            line2[i] = "/" + line2[i];
        }
        return new ReplyKeyboardMarkup(line1, line2)
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