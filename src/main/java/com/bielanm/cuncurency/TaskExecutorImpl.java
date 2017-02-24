package com.bielanm.cuncurency;


public class TaskExecutorImpl implements TaskExecutor {

    private final BlockingQueue queue;

    public TaskExecutorImpl(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public final void run() {
        while (true) {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute() throws InterruptedException {
        Runnable task = queue.dequeue();
        task.run();
    }
}
