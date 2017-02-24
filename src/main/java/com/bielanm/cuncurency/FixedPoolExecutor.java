package com.bielanm.cuncurency;

import java.util.ArrayList;
import java.util.List;

public abstract class FixedPoolExecutor implements PoolExecutor {

    private final int coreThreadCount;
    private BlockingQueue queue;

    private List<Thread> threads;

    public FixedPoolExecutor(int coreThreadCount, BlockingQueue queue) {
        this.coreThreadCount = coreThreadCount;
        this.queue = queue;
        initThreads();
    }

    private void initThreads() {
        threads = new ArrayList<>(coreThreadCount);

        TaskExecutorFactory executorFactory = getTaskExecutorFactory();
        for (int i = 0; i < coreThreadCount; i++) {
            Runnable task = executorFactory.createTaskExecutor(queue);
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }
    }

    public void submit(Runnable task) {
        queue.enqueue(task);
    }


    public abstract TaskExecutorFactory getTaskExecutorFactory();

}
