package com.bielanm.os.sleepingbarberproblem;


import com.bielanm.cuncurency.AtomicInt;

import static com.bielanm.os.SleepingBarberProblem.BARBER_SHAVE_TIME;

public class Barber {

    private volatile boolean isSleep = true;
    private volatile AtomicInt current = new AtomicInt(0);

    public void shave(Client client) {
        current.incrementAndGet();
        try {
            Thread.sleep(BARBER_SHAVE_TIME);
        } catch (InterruptedException e) {
            System.err.println("Barber shave error");
        }
        current.decrementAndGet();
    }

    public void sleep() {
        isSleep = true;
    }

    public void wake() {
        isSleep = false;
    }

    public boolean isSleep() {
        return isSleep;
    }
}
