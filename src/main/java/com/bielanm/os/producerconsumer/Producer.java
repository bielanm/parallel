package com.bielanm.os.producerconsumer;

import com.bielanm.cuncurency.AtomicInteger;

public class Producer implements Runnable {

    private final long sleepMilis;
    private final int maxProcess;
    private final ProcessQueue blockingQueue;
    private AtomicInteger processCount;

    public Producer(ProcessQueue blockingQueue, long sleepMilis, int maxProcess) {
        this.blockingQueue = blockingQueue;
        this.sleepMilis = sleepMilis;
        this.maxProcess = maxProcess;
        processCount = new AtomicInteger(0);
    }


    @Override
    public void run() {
        try {
            while (maxProcess > processCount.get()) {
                Thread.sleep(sleepMilis);
                Process process = ProcessFactory.newProcessWithRandomExecTime();
                System.out.println(process.getName() + " created");
                blockingQueue.enqueue(process);
                processCount.incrementAndGet();
            }
        } catch (InterruptedException exc) {
            //TODO
        }
    }

}
