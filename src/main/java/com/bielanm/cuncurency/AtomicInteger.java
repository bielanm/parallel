package com.bielanm.cuncurency;

public class AtomicInteger {

    private volatile int value;

    public AtomicInteger(int value) {
        this.value = value;
    }

    synchronized public int decrementAndGet() {
        return value = value - 1;
    }
    synchronized public int get() { return value; }
}
