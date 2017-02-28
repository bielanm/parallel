package com.bielanm.cuncurency;


public class TaskExecutorImpl implements TaskExecutor {

    protected final BlockingQueue queue;

    public TaskExecutorImpl(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public final void run() {
        while (true) {
            execute();
        }
    }

    public void execute() {
        Runnable task = queue.dequeue();
        task.run();
    }

}
