package org.algiri.bots;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.additional.buttons.Button;
import api.longpoll.bots.model.objects.additional.buttons.TextButton;
import api.longpoll.bots.model.objects.basic.Message;
import api.longpoll.bots.model.objects.basic.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.algiri.DataBase;

import java.util.ArrayList;
import java.util.List;

import static org.algiri.Main.*;

@RequiredArgsConstructor
public class VkBot extends LongPollBot implements AbstractBot {


    private final DataBase bd;


    @SneakyThrows
    public void run() {
        this.startPolling();
    }


    @SneakyThrows
    @Override
    public void onMessageNew(MessageNew messageNew) {
        Message message = messageNew.getMessage();
        if(message.hasText()) {
            bot(message.getText(), message.getPeerId(), bd);
        }
    }

    @Override
    public String getAccessToken() {
        String testGroup = "vk1.a.Pka1R03PDVhnW8YRw6IAecvMLCYehLqZuapGYTUMzy-fBO8Dpx_3ilh-Xz20MSFerL9MctIuuWjUjkGhgJ7_4ZXfQrdllcjnS4z-ISd0jsiLHsJ60DQzaCpjGHXPh8f-T91mybW6X6cgR0Gc22DQQ2De8bThfox3NalzkFrSWAvHjbmatKa_XbP4FWza6r-I";
        String mainGroup = "vk1.a.9AQs2ozEmJPOItH-rRjTytJRVmgJkoWkeAWHzVZWUXubclk4B-InLeqbEBYKrYVu44__jISNwYGyKCruFl9TBPF3tGDXAnRZm-YQkBLndKRcI7_wKZR6LlFNcY2N_i5A7z2kEuTpnzqhlJ2bMya1XNlfXoBWv9Y3Aw8cuKJXUMeoHcQcBz_1D6OgmjOYM7qd";
        return testGroup;
    }

    @SneakyThrows
    @Override
    public void send(String mes, long userId) {
        vk.messages
                .send()
                .setMessage(mes)
                .setPeerId((int)userId)
                .setRandomId(RANDOM.nextInt(10000))
                .execute();
    }

    @SneakyThrows
    @Override
    public void send(String mes, long userId, Object keyboard) {
        vk.messages
                .send()
                .setMessage(mes)
                .setPeerId((int)userId)
                .setRandomId(RANDOM.nextInt(10000)).setKeyboard((api.longpoll.bots.model.objects.additional.Keyboard) keyboard)
                .execute();

    }
    @Override
    public Keyboard createKeyboard(String[] s_line1, String[] s_line2) {

        List<List<Button>> allKey = new ArrayList<>();
        List<Button> line1 = new ArrayList<>();
        List<Button> line2 = new ArrayList<>();
        for(var z: s_line1)
            line1.add(new TextButton(Button.Color.PRIMARY, new TextButton.Action(z)));
        for(var z: s_line2)
            line2.add(new TextButton(Button.Color.PRIMARY, new TextButton.Action(z)));
        allKey.add(line1);
        allKey.add(line2);
        return new Keyboard(allKey);
    }

    @SneakyThrows
    @Override
    public String getName(long userId) {
        if (userId >= 2000000000 && userId < 2000001000)
            return "беседа вк";
        User user = vk.users.get().setUserIds(String.valueOf(userId)).execute().getResponse().get(0);
        return user.getFirstName() + " " + user.getLastName();
    }
}