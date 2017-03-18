package com.bielanm.os.producerconsumer;


import com.bielanm.cuncurency.AtomicInteger;

public class Process implements Runnable {

    private static AtomicInteger count = new AtomicInteger(0);
    private final long execMilis;
    private final String name;

    public Process(long execMilis, String name) {
        this.execMilis = execMilis;
        this.name = name;
    }

    public Process(long execMilis) {
        this(execMilis, "Process_" + count.incrementAndGet());
    }


    @Override
    public void run() {
        long time = System.currentTimeMillis();
        System.out.println(getName() + " started");
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(execMilis/5);
                Thread.yield();
            }
        } catch (InterruptedException e) {
        }
        System.out.println(name + " finished, execution time: " + (System.currentTimeMillis() - time) + "/" + execMilis + "ms.");

    }

    public String getName() {
        return name;
    }

}
