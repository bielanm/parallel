package com.bielanm.util;

public class InterruptedContext {

    public static void execute(InterruptedTask task) {
        try {
            task.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public interface InterruptedTask {
        void run() throws InterruptedException;
    }

}
