package com.bielanm.util.secondlab;


import com.bielanm.OverflowException;
import com.bielanm.cuncurency.BlockingQueue;
import com.bielanm.cuncurency.LinkedBlockingQueue;
import com.bielanm.util.Randomizer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CPUProcess implements Runnable {

    private final long sleepMilis;
    private final List<BlockingQueue> queues = new LinkedList<>();

    public CPUProcess(long sleepMilis, BlockingQueue queue) {
        queues.add(queue);
        this.sleepMilis = sleepMilis;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(sleepMilis);
                offerProcess(ProcessFactory.newProcessWithRandomSleepTime());
            }
        } catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void offerProcess(Process process) {

        for (BlockingQueue queue : queues) {
            try {
                queue.enqueue(process);
                return;
            } catch (OverflowException exc) {
                System.out.println(exc.getMessage());
            }
        }
        runProcessInNewQueue(process);
    }

    private void runProcessInNewQueue(Process process) {
        BlockingQueue newQueue = new CPUQueue();
        newQueue.enqueue(process);
        queues.add(newQueue);
        new CPU(newQueue);
    }

}
