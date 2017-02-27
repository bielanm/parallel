package com.bielanm.cuncurency;

public interface TaskExecutor {
    void execute() throws InterruptedException;
}
