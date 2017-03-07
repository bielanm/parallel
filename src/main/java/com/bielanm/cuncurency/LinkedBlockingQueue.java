package com.bielanm.cuncurency;

import java.util.LinkedList;

public class LinkedBlockingQueue extends LinkedList<Runnable> implements BlockingQueue {

    volatile private int size = 0;

    public LinkedBlockingQueue() {
    }

    @Override
    public synchronized void enqueue(Runnable runnable) {
        if(size == 0) {
            notifyAll();
        }
        offer(runnable);
        size++;
    }

    @Override
    public synchronized Runnable dequeue() {
        try {
            while (size == 0) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Runnable runnable = poll();
        size--;
        return runnable;
    }

}
