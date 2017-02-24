package com.bielanm.cuncurency;


import java.util.LinkedList;
import java.util.Queue;

public class LinkedBlockingQueue implements BlockingQueue{

    private Queue<Runnable> queue;

    public LinkedBlockingQueue() {
        this(new LinkedList<Runnable>());
    }

    public LinkedBlockingQueue(LinkedList<Runnable> queue) {
        this.queue = queue;
    }

    @Override
    public synchronized void enqueue(Runnable runnable) {
        if(queue.size() == 0) {
            notifyAll();
        }
        queue.offer(runnable);
    }

    @Override
    public synchronized Runnable dequeue() throws InterruptedException {
        while(queue.size() == 0) {
            wait();
        }
        return queue.poll();
    }


}
