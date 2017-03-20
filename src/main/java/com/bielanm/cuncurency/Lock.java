package com.bielanm.cuncurency;

public class Lock {

    private volatile boolean isBisy = false;

    public synchronized void lock() {
        try {
            while (isBisy) wait();
            isBisy = true;
        } catch (InterruptedException e) {
            System.err.println("Lock interrupted exception");
        }
    }

    public synchronized void unlock() {
        if(isBisy){
            isBisy = false;
            notify();
        }
    }

}
