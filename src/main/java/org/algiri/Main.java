package org.algiri;


import com.pengrad.telegrambot.TelegramBot;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.algiri.bots.TgBot;
import org.algiri.bots.VkBot;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    // рандом для случайного id сообщения
    public static final Random RAMDOM = new Random();
    // класс тг
    public static final TelegramBot bot = new TelegramBot("5524044091:AAHe9Wt7GHqfjf4_mEVKG-WbaDcIEUkF2IY");
    // класс вк
    public static VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
    // класс группы
    public static GroupActor group = new GroupActor(216410844, "vk1.a.9AQs2ozEmJPOItH-rRjTytJRVmgJkoWkeAWHzVZWUXubclk4B-InLeqbEBYKrYVu44__jISNwYGyKCruFl9TBPF3tGDXAnRZm-YQkBLndKRcI7_wKZR6LlFNcY2N_i5A7z2kEuTpnzqhlJ2bMya1XNlfXoBWv9Y3Aw8cuKJXUMeoHcQcBz_1D6OgmjOYM7qd");




    public static void main(String[] args) {

        DataBase bd = new DataBase();
        VkBot vkBot = new VkBot(bd);
        TgBot tgBot = new TgBot(bd);
        System.out.println("\nбот включен");

        //Parser.parse();
        ExecutorService tgThread = Executors.newSingleThreadExecutor();
        ExecutorService vkThread = Executors.newSingleThreadExecutor();

        tgThread.execute(tgBot::run);
        vkThread.execute(vkBot::run);

    }
}