package com.bielanm;

import com.bielanm.util.secondlab.CPUProcess;

public class SecondSolution {

    private static final long SLEEP_BETWEEN_GENERATED_PROCESS = 10;
    private static final long GENERATED_TIME = 1000;

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new CPUProcess(SLEEP_BETWEEN_GENERATED_PROCESS));
        t.start();
        Thread.sleep(GENERATED_TIME);
        t.interrupt();
    }

}
