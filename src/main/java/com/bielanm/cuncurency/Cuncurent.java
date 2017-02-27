package com.bielanm.cuncurency;

public class Cuncurent {

    public static PoolExecutor simpleFixedPoolExecutor(int coreThreadCount) {
        return new FixedPoolExecutorImpl(coreThreadCount, new LinkedBlockingQueue());
    }

    public static PoolExecutor blockingFixedPoolExecutor(int coreThredCount) {
        return new BlockingTaskExecutorImpl(coreThredCount, new LinkedBlockingQueue());
    }

}
