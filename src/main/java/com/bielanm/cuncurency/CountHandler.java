package com.bielanm.cuncurency;

public class CountHandler implements Command {

    private AtomicInteger counter;
    private Runnable task;

    public CountHandler(int count, Runnable task) {
        this.counter = new AtomicInteger(count);
        this.task = task;
    }

    public void endTask() {
        int result = this.counter.decrementAndGet();
        if(result == 0) execute();
    }

    @Override
    public void execute() {
        task.run();
    }
}
