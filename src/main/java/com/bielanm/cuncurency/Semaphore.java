package com.bielanm.cuncurency;

public class Semaphore {

    public Semaphore(int available) {
        this.available = available;
    }

    private volatile int available;

    public synchronized void decrease() {
        try {
            while (available == 0) wait();
            available--;
        } catch (InterruptedException e) {
            System.err.println("Lock interrupted exception");
        }
    }

    public synchronized void increase() {
        if(available == 0){
            notifyAll();
        }
        available++;
    }

    public synchronized int size() {
        return available;
    }

}
