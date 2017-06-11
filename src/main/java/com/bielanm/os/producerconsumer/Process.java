package com.bielanm.os.producerconsumer;


import com.bielanm.cuncurency.AtomicInt;

public class Process implements Runnable {

    private static AtomicInt count = new AtomicInt(0);
    private final long execMilis;
    private final String name;
    private String author;

    public Process(long execMilis, String name) {
        this.execMilis = execMilis;
        this.name = name;
    }

    public Process(long execMilis) {
        this(execMilis, "Task" + count.incrementAndGet());
    }


    @Override
    public void run() {
        long time = System.currentTimeMillis();
        System.out.println("Consumer start executing " + getName());
        try {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(execMilis/5);
                Thread.yield();
            }
        } catch (InterruptedException e) {
        }
        System.out.println("Consumer finish execution " + getName() + " , exec_time: " + (System.currentTimeMillis() - time) + "/" + execMilis + "ms.");

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
