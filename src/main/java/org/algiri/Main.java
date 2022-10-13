package org.algiri;


import api.longpoll.bots.exceptions.VkApiException;
import com.pengrad.telegrambot.TelegramBot;
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