package com.bielanm.util.secondlab;

public class Process implements Runnable {

    private static int count = 0;
    private final long execMilis;
    private final String name;

    public Process(long execMilis, String name) {
        this.execMilis = execMilis;
        this.name = name;
    }

    public Process(long execMilis) {
        this(execMilis, "Process_" + ++count);
    }


    @Override
    public void run() {
        long time = System.currentTimeMillis();
        System.out.println(name + " started");
        try {
            Thread.sleep(execMilis);
        } catch (InterruptedException e) {
        }
        System.out.println(name + " finished, execution time: " + (System.currentTimeMillis() - time) + "/" + execMilis + "ms.");

    }

    public String getName() {
        return name;
    }
}
