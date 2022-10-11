package org.algiri;


import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    // токен и айди группы вк
    public static final String TOKEN = "vk1.a.9AQs2ozEmJPOItH-rRjTytJRVmgJkoWkeAWHzVZWUXubclk4B-InLeqbEBYKrYVu44__jISNwYGyKCruFl9TBPF3tGDXAnRZm-YQkBLndKRcI7_wKZR6LlFNcY2N_i5A7z2kEuTpnzqhlJ2bMya1XNlfXoBWv9Y3Aw8cuKJXUMeoHcQcBz_1D6OgmjOYM7qd";
    public static final int GROUP_ID = 216410844;
    // рандом для случайного id сообщения
    public static final Random RAMDOM = new Random();
    // класс вк
    public static VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());

    // класс группы
    public static GroupActor group = new GroupActor(GROUP_ID, TOKEN);




    public static void main(String[] args) {
        //Parser.parse(bd.user);
        DataBase bd = new DataBase();
        VkBot vkBot = new VkBot(bd);
        TgBot tgBot = new TgBot(bd);
        System.out.println("\nBot enabled");


        ExecutorService tgThread = Executors.newSingleThreadExecutor();
        ExecutorService vkThread = Executors.newSingleThreadExecutor();
        tgThread.execute(tgBot::run);
        vkThread.execute(vkBot::run);
    }
}