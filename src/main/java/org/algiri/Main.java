package org.algiri;


import com.pengrad.telegrambot.TelegramBot;
import org.algiri.bots.TgBot;
import org.algiri.bots.VkBot;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static final Random RANDOM = new Random();
    // основа
    public static final TelegramBot bot = new TelegramBot("5524044091:AAHe9Wt7GHqfjf4_mEVKG-WbaDcIEUkF2IY");
    // тест бот
    //public static final TelegramBot bot = new TelegramBot("1116496780:AAH8HZ8kDNoSQW3LNXKM8ladh434hCJfEls");



    public static void main(String[] args) {

        /*Parser.parse("to1-221-223-ot.xlsx", 3, new ArrayList<>(){{
            add("group-name");
        }});*/


        DataBase bd = new DataBase();
        VkBot vkBot = new VkBot(bd);
        TgBot tgBot = new TgBot(bd);
        System.out.println("\nбот включен");
        ExecutorService tgThread = Executors.newSingleThreadExecutor();
        ExecutorService vkThread = Executors.newSingleThreadExecutor();

        tgThread.execute(tgBot::run);
        vkThread.execute(vkBot::run);


    }
}