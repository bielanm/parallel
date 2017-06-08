package com.bielanm.os.producerconsumer;

import com.bielanm.cuncurency.AtomicInteger;

public class Producer implements Runnable {

    private final long sleepMilis;
    private final int maxProcess;
    private final ProcessQueue blockingQueue;
    private AtomicInteger processCount;
    private String name;
    private static int count = 0;

    public Producer(ProcessQueue blockingQueue, long sleepMilis, int maxProcess) {
        this.blockingQueue = blockingQueue;
        this.sleepMilis = sleepMilis;
        this.maxProcess = maxProcess;
        processCount = new AtomicInteger(0);

        name = "Producer_" + count++;
    }


    @Override
    public void run() {
        try {
            while (maxProcess > processCount.get()) {
                Thread.sleep(sleepMilis);
                Process process = ProcessFactory.newProcessWithRandomExecTime();
                process.setAuthor(getName());
                //System.out.println(getName() + " create " + process.getName());
                blockingQueue.enqueue(process);
                Thread.yield();
                processCount.incrementAndGet();
            }
        } catch (InterruptedException exc) {
            //TODO
        }
    }

    public String getName() {
        return name;
    }
}
