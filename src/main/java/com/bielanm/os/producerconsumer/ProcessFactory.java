package com.bielanm.os.producerconsumer;

import com.bielanm.util.Randomizer;

import java.lang.*;

public class ProcessFactory {

    private static final long MAX_EXEC_MILISECONDS = 20;

    private static final Randomizer rndmz = new Randomizer();

    public static Process newProcessWithRandomExecTime() {
        return new Process(Math.abs(rndmz.nextLong()) % MAX_EXEC_MILISECONDS);
    }

    public static Process newProcessWithMaxExecTime() {
        return new Process(MAX_EXEC_MILISECONDS);
    }


}
