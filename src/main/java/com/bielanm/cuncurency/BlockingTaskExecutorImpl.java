package com.bielanm.cuncurency;

import java.util.List;

public class BlockingTaskExecutorImpl extends FixedPoolExecutorImpl {

    public BlockingTaskExecutorImpl(int coreThreadCount, BlockingQueue queue) {
        super(coreThreadCount, queue);
    }

    public void submit(List<Runnable> tasks) {
        int taskCount = tasks.size();
        Object waiter = new Object();
        CountHandler handler = new CountHandler(taskCount, () -> {
                synchronized (waiter) {
                    waiter.notify();
                }
            });

        for (Runnable task : tasks) {
            queue.enqueue(() -> {
                task.run();
                handler.endTask();
            });
        }

        synchronized (waiter){
            try {
                waiter.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
