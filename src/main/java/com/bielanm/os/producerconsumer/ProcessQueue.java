package com.bielanm.os.producerconsumer;


import com.bielanm.cuncurency.LinkedBlockingQueue;
import com.bielanm.exceptions.OverflowException;

import java.lang.*;

public class ProcessQueue extends LinkedBlockingQueue {

    private static int count = 0;

    private final String name;

    private volatile int maxSize = 0;

    public ProcessQueue(String name) {
        this.name = name;
    }

    public synchronized void enqueue(Process process) {
        super.enqueue(process);
        System.out.println(process.getName() + " was added to " + getName() + "(" + size + ")" + ".");
    }

    public String getName() {
        return name;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
