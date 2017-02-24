package com.bielanm.cuncurency;

public interface TaskExecutorFactory {

    TaskExecutor createTaskExecutor(BlockingQueue queue);

}
