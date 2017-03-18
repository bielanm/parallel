package com.bielanm.os.eatingphilosophers;

import java.util.concurrent.atomic.AtomicInteger;

public class Fork {

    private static AtomicInteger counter = new AtomicInteger(0);

    private final String name;
    private final Integer number;

    public Fork() {
        this("Fork_" + counter.incrementAndGet(), counter.get());
    }

    public Fork(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }
}
