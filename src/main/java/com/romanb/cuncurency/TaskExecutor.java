package com.romanb.cuncurency;

public interface TaskExecutor extends Runnable {
    void execute() throws InterruptedException;
}
