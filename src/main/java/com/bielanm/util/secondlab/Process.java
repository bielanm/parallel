package com.bielanm.util.secondlab;

public class Process implements Runnable {

    private final long sleepMilis;

    public Process(long sleepMilis) {
        this.sleepMilis = sleepMilis;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(sleepMilis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
