package com.bielanm.util.secondlab;

import com.bielanm.util.Randomizer;

public class ProcessFactory {

    private static final long MAX_SLEEP_MILISECONDS = 10000;

    private static final Randomizer rndmz = new Randomizer();

    public static Process newProcessWithRandomSleepTime() {
        return new Process(rndmz.nextLong() % MAX_SLEEP_MILISECONDS);
    }

    public static Process newProcessWithMaxSleepTime() {
        return new Process(rndmz.nextLong() % MAX_SLEEP_MILISECONDS);
    }


}
