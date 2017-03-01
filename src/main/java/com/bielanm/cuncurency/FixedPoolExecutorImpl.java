package com.bielanm.cuncurency;

public class FixedPoolExecutorImpl extends FixedPoolExecutor {

    public FixedPoolExecutorImpl(int coreThreadCount, BlockingQueue queue) {
        super(coreThreadCount, queue);
    }

    @Override
    public TaskExecutorFactory getTaskExecutorFactory() {
        return queue -> new TaskExecutorImpl(queue);
    }
}
