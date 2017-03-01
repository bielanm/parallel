package com.bielanm.cuncurency;

import java.util.LinkedList;

public class LinkedBlockingQueue extends LinkedList<Runnable> implements BlockingQueue {


    public LinkedBlockingQueue() {
    }

    @Override
    public synchronized void enqueue(Runnable runnable) {
        if(size() == 0) {
            notifyAll();
        }
        offer(runnable);
    }

    @Override
    public synchronized Runnable dequeue() {
        try {
            while (size() == 0) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return poll();
    }

}
