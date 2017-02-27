package com.bielanm.cuncurency;

public class CountHandler implements TaskExecutor {

    private volatile AtomicInteger counter;
    private Runnable task;

    public CountHandler(int count, Runnable task) {
        this.counter = new AtomicInteger(count);
    }

    public void endTask() throws InterruptedException {
        int result = this.counter.decrementAndGet();
        if(result == 0) execute();
    }

    @Override
    public void execute() throws InterruptedException {
        task.run();
    }


}
