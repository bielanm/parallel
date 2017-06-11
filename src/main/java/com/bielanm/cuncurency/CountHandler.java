package com.bielanm.cuncurency;

public class CountHandler implements Command {

    private AtomicInt counter;
    private Runnable task;

    public CountHandler(int count, Runnable task) {
        this.counter = new AtomicInt(count);
        this.task = task;
    }

    public void endTask() {
        this.counter.decrementAndGet();
        check();
    }

    public void check() {
        if(counter.get() == 0) execute();
    }

    public int get() {
        return this.counter.get();
    }

    @Override
    public void execute() {
        task.run();
    }
}
