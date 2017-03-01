package com.bielanm.util.secondlab;


import com.bielanm.cuncurency.BlockingQueue;
import com.bielanm.cuncurency.FixedPoolExecutorImpl;
import com.bielanm.cuncurency.TaskExecutorFactory;
import com.bielanm.cuncurency.TaskExecutorImpl;

public class CPU extends FixedPoolExecutorImpl {

    private static int instanceCount = 0;

    private final Counter counter = new Counter();
    private final String name;

    public CPU(BlockingQueue queue) {
        this(queue, "CPU_" + instanceCount++);
    }

    public CPU(BlockingQueue queue, String name) {
        super(1, queue);
        this.name = name;
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
