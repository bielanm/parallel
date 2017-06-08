package com.bielanm.os.producerconsumer;


import com.bielanm.cuncurency.*;

import java.lang.*;
import java.util.LinkedList;
import java.util.concurrent.atomic.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessQueue extends MyQueue<Runnable> implements BlockingQueue {

    private final String name;
    private final int maxSize;

    private final Semaphore empty;
    private final Semaphore fill;
    private final Lock lock = new Lock();
    private final Lock enqueue = new Lock();
    private final Lock dequeue = new Lock();
    private final Object monitor = new Object();
    AtomicInteger size = new AtomicInteger(0);

    public ProcessQueue(String name, int maxSize) {
        this.name = name;
        this.maxSize = maxSize;
        empty = new Semaphore(maxSize);
        fill = new Semaphore(0);
        System.out.println("Empty " + name + " with max size " + maxSize + " was created.");
    }

    public void enqueue(Runnable runnable) {
        Process process = castToProcess(runnable); //for output
        System.out.println(getState() + "\n" + process.getAuthor() + " try to add " + process.getName() + " task to " + getName() + "...");
        enqueue.lock();
        empty.decrease();
        lock.lock();
        if(size.get() <= 1){
            offer(process);
            size.incrementAndGet();
            System.out.println(process.getAuthor() + " add " + process.getName() + " to " + getName() + "...\n" + getState());
            fill.increase();
            lock.unlock();
        } else {
            lock.unlock();
            offer(process);
            size.incrementAndGet();
            System.out.println(process.getAuthor() + " add " + process.getName() + " to " + getName() + "...\n" + getState());
            fill.increase();
        }
        enqueue.unlock();
    }

    @Override
    public Runnable dequeue() {
        Process process;
        System.out.println("Consumer try to take data from " + getName() + "...");
        dequeue.lock();
        fill.decrease();
        lock.lock();
        if(size.get() <= 1){
            process = castToProcess(poll());
            size.decrementAndGet();
            System.out.println("Consumer took " + process.getName() + "\n" + getState());
            empty.increase();
            lock.unlock();
        } else {
            lock.unlock();
            process = castToProcess(poll());
            size.decrementAndGet();
            System.out.println("Consumer took " + process.getName() + "\n" + getState());
            empty.increase();
        }
        dequeue.unlock();
        return process;
    }

    private String getState() {
        if(isEmpty())
            return getName() + " is empty.";
        else
            return this.toString();
    }

    public String getName() {
        return name;
    }

    private Process castToProcess(Runnable runnable) {
        if(runnable instanceof Process)
            return (Process) runnable;
        throw new IllegalArgumentException("Process can`t be null or non Process instance");
    }
}
