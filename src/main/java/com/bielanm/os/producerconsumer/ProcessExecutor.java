package com.bielanm.os.producerconsumer;


import com.bielanm.cuncurency.BlockingQueue;
import com.bielanm.cuncurency.FixedPoolExecutorImpl;
import com.bielanm.cuncurency.TaskExecutorFactory;
import com.bielanm.cuncurency.TaskExecutorImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class ProcessExecutor extends FixedPoolExecutorImpl {

    private static AtomicInteger instanceCount = new AtomicInteger(0);

    private final Counter counter = new Counter();
    private final String name;

    public ProcessExecutor(BlockingQueue queue, Integer threadCount) {
        this(queue, threadCount, "ProcessExecutor_" + instanceCount.incrementAndGet());
    }

    public ProcessExecutor(BlockingQueue queue, Integer threadCount, String name) {
        super(threadCount, queue);
        this.name = name;
        threads.forEach(thread -> thread.setPriority(Thread.MIN_PRIORITY));
    }

    @Override
    public TaskExecutorFactory getTaskExecutorFactory() {
        return queue -> new TaskExecutorImpl(queue) {
            @Override
            public void execute() {
                super.execute();
                counter.increment();
            }
        };
    }

    public int getExecutedTasks() {
        return counter.getCount();
    }

    public String getName() {
        return name;
    }

    public class Counter {
        private volatile int count = 0;

        public synchronized void increment() {
            count ++;
        };

        public int getCount() {
            return count;
        }
    }
}
