package com.romanb.cuncurency;

public interface TaskExecutorFactory {

    TaskExecutor createTaskExecutor(BlockingQueue queue);

}
