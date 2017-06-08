package com.bielanm.cuncurency;


import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LinkedBlockingQueue extends ConcurrentLinkedQueue<Runnable> implements BlockingQueue {

    volatile protected int size = 0;

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
        }
        Runnable runnable = poll();
        size--;
        return runnable;
    }

    @Override
    public int size() {
        return size;
    }
}
