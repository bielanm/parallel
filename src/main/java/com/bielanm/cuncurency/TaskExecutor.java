package com.bielanm.cuncurency;

public interface TaskExecutor extends Runnable {
    void execute() throws InterruptedException;
}
