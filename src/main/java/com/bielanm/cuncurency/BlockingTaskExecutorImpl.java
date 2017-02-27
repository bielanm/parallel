package com.bielanm.cuncurency;

import java.util.List;

public class BlockingTaskExecutorImpl extends FixedPoolExecutorImpl {

    public BlockingTaskExecutorImpl(int coreThreadCount, BlockingQueue queue) {
        super(coreThreadCount, queue);
    }

    public void submit(List<Runnable> tasks) {
        int taskCount = tasks.size();
        Object waiter = new Object();
        CountHandler handler = new CountHandler(taskCount, () -> waiter.notify());

        try {
            for (Runnable task : tasks) {
                queue.enqueue(() -> {
                    task.run();
                    try {
                        handler.endTask();
                    } catch (InterruptedException e) {
                        errors.add(e);
                    }
                });
            }

            synchronized (waiter){
                waiter.wait();
            }
        } catch (InterruptedException e) {
            errors.add(e);
        }
    }
}
