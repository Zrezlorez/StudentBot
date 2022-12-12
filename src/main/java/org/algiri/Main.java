package org.algiri;


import org.algiri.bots.TgBot;
import org.algiri.bots.VkBot;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static boolean isTest = false;
    public static void main(String[] args) {
        System.out.println("Bot enabled!");
        ExecutorService tgThread = Executors.newSingleThreadExecutor();
        ExecutorService vkThread = Executors.newSingleThreadExecutor();
        tgThread.execute(new TgBot()::run);
        vkThread.execute(new VkBot()::run);


//        Parser.parse("to1-221-223-ot.xlsx", "то221", "то222", "то223");

    }
}