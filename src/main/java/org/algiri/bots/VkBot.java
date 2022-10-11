package org.algiri.bots;

import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.algiri.DataBase;

import java.util.*;


import static org.algiri.Main.*;
@RequiredArgsConstructor
public class VkBot extends AbstractBot  {
    private final DataBase bd;


    @SneakyThrows
    public void run() {
        int ts = vk.messages().getLongPollServer(group).execute().getTs();
        while (true){
            Thread.sleep(500);
            MessagesGetLongPollHistoryQuery historyQuery =  vk.messages().getLongPollHistory(group).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();


            for (Message message : messages) {
                bot(message.getText(), message.getPeerId(), bd, true);
            }
            ts = vk.messages().getLongPollServer(group).execute().getTs();
        }
    }


    @SneakyThrows
    @Override
    public void send(String mes, long userId) {
        vk.messages()
                .send(group)
                .message(mes)
                .peerId((int)userId)
                .randomId(RAMDOM.nextInt(10000))
                .execute();
    }

    @SneakyThrows
    @Override
    public void send(String mes, long userId, Object keyboard) {
        vk.messages()
                .send(group)
                .message(mes)
                .peerId((int)userId)
                .randomId(RAMDOM.nextInt(10000))
                .keyboard((Keyboard) keyboard)
                .execute();

    }
    @Override
    public com.vk.api.sdk.objects.messages.Keyboard createKeyboard(String[] s_line1, String[] s_line2) {
        com.vk.api.sdk.objects.messages.Keyboard keyboard = new com.vk.api.sdk.objects.messages.Keyboard();
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        List<KeyboardButton> line2 = new ArrayList<>();
        for(var z: s_line1)
            line1.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel(z).setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        for(var z: s_line2)
            line2.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel(z).setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        allKey.add(line1);
        allKey.add(line2);
        keyboard.setButtons(allKey);
        return keyboard;
    }

    @SneakyThrows
    @Override
    public String getName(long userId) {
        GetResponse user = vk.users().get(group).userIds(String.valueOf(userId)).execute().get(0);
        return user.getFirstName() + " " + user.getLastName();
    }

}