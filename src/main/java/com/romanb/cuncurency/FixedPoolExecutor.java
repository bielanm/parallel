package com.romanb.cuncurency;

import java.util.ArrayList;
import java.util.List;

public abstract class FixedPoolExecutor implements PoolExecutor {

    private final int coreThreadCount;
    protected boolean isAlive = true;
    protected final BlockingQueue queue;
    protected final List<Throwable> errors = new ArrayList<>();

    protected List<Thread> threads;

    public FixedPoolExecutor(int coreThreadCount, BlockingQueue queue) {
        this.coreThreadCount = coreThreadCount;
        this.queue = queue;
        initThreads();
    }

    private void initThreads() {
        threads = new ArrayList<>(coreThreadCount);

        TaskExecutorFactory executorFactory = getTaskExecutorFactory();
        for (int i = 0; i < coreThreadCount; i++) {
            TaskExecutor taskExecutor = executorFactory.createTaskExecutor(queue);
            Thread thread = new Thread(taskExecutor);
            threads.add(thread);
            thread.start();
        }
    }

    public void submit(Runnable task) {
        checkAlive();
        queue.enqueue(task);
    }

    public void submit(List<Runnable> tasks) {
        checkAlive();
        for (Runnable task: tasks) {
            queue.enqueue(task);
        }
    }

    public void shutdown() {
        isAlive = false;
    }

    protected void checkAlive() {
        if(!isAlive) throw new IllegalStateException("Pool executor was shutdown");
    }

    public abstract TaskExecutorFactory getTaskExecutorFactory();

}
