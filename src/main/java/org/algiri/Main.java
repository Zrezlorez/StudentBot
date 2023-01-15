package org.algiri;


import org.algiri.bots.TgBot;
import org.algiri.bots.VkBot;
import org.algiri.data.Parser;


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

//        Parser.parse("lh1-221-224-ot.xlsx", "лх221", "лх222", "лх223", "лх224");
//        Parser.parse("is1-223-224-ot.xlsx", "ис223", "ис224");
//        Parser.parse("is1-211-215-ot.xlsx", "ис211", "ис212", "ис213", "ис214", "ис215");
//        Parser.parse("to1-221-223-ot.xlsx", "то221", "то222", "то223");

    }
}