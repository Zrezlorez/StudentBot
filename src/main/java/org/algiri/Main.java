package org.algiri;


import org.algiri.bots.TgBot;
import org.algiri.bots.VkBot;
import org.algiri.data.Parser;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static boolean isTest = true;
    public static void main(String[] args) {
        System.out.println("Bot enabled!");



        ExecutorService tgThread = Executors.newSingleThreadExecutor();
        ExecutorService vkThread = Executors.newSingleThreadExecutor();
        tgThread.execute(new TgBot()::run);
        vkThread.execute(new VkBot()::run);

//        Parser.parse("lh1-221-224-ot.xlsx", "лх221", "лх222", "лх223", "лх224");

    }
}