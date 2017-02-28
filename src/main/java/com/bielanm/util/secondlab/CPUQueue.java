package com.bielanm.util.secondlab;


import com.bielanm.OverflowException;
import com.bielanm.cuncurency.LinkedBlockingQueue;

public class CPUQueue extends LinkedBlockingQueue {

    private static int count = 0;

    private final int limitSize;
    private final String name;

    public CPUQueue(int limitSize) {
        this(limitSize, "Queue_" + count++);
    }

    public CPUQueue(int limitSize, String name) {
        this.limitSize = limitSize;
        this.name = name;
    }

    @Override
    public synchronized Runnable dequeue() {
        return super.dequeue();
    }

    @Override
    public synchronized void enqueue(Runnable runnable) {
        checkOverflowing();
        super.enqueue(runnable);
    }

    private void checkOverflowing() {
        if(size() == limitSize)
            throw new OverflowException("Queue " + name + " overflowing!...");
    }

    private boolean allowed(){
        return (limitSize - size()) > 0;
    }
}
