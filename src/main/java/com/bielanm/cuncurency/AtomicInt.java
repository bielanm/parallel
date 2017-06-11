package com.bielanm.cuncurency;

public class AtomicInt {

    private volatile int value;

    public AtomicInt(int value) {
        this.value = value;
    }

    synchronized public int decrementAndGet() {
        return value = value - 1;
    }
    synchronized public int get() { return value; }
    synchronized public int incrementAndGet() {return value = value + 1; }
}
