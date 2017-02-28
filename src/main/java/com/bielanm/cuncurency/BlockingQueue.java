package com.bielanm.cuncurency;

public interface BlockingQueue {

    void enqueue(Runnable runnable);
    Runnable dequeue();

}
