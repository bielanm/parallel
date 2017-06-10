package com.romanb.cuncurency;

public class Cuncurent {

    public static PoolExecutor simpleFixedPoolExecutor(int coreThreadCount) {
        return new FixedPoolExecutorImpl(coreThreadCount, new LinkedBlockingQueue());
    }

}
