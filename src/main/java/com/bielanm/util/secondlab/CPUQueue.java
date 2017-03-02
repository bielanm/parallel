package com.bielanm.util.secondlab;


import com.bielanm.exceptions.OverflowException;
import com.bielanm.cuncurency.LinkedBlockingQueue;

public class CPUQueue extends LinkedBlockingQueue {

    private static int DEFAULT_LIMIT = 10;
    private static int count = 0;

    private final int limitSize;
    private final String name;

    private volatile int maxSize = 0;

    public CPUQueue() {
        this(DEFAULT_LIMIT);
    }

    public CPUQueue(int limitSize) {
        this(limitSize, "Queue_" + count++);
    }

    public CPUQueue(int limitSize, String name) {
        this.limitSize = limitSize;
        this.name = name;
    }

    public synchronized void enqueue(Process process) {
        checkOverflowing();
        System.out.println(process.getName() + " was added to " + getName() + ".");
        super.enqueue(process);
    }

    private void checkOverflowing() {
        int size = size();
        maxSize = size > maxSize ? size : maxSize;
        if(size == limitSize)
            throw new OverflowException("Queue " + name + " overflowing!...");
    }

    public String getName() {
        return name;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
