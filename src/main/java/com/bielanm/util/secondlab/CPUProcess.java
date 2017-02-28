package com.bielanm.util.secondlab;


import com.bielanm.OverflowException;
import com.bielanm.cuncurency.LinkedBlockingQueue;
import com.bielanm.util.Randomizer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CPUProcess implements Runnable {

    private final long sleepMilis;
    private final ProcessFactory processFactory;
    private final List<Queue<Runnable>> queues = new LinkedList<>();

    public CPUProcess(long sleepMilis, Queue<Runnable> queue, ProcessFactory processFactory) {
        queues.add(queue);
        this.processFactory = new ProcessFactory();
        this.sleepMilis = sleepMilis;
    }


    @Override
    public void run() {
        try {
            Randomizer randomizer = new Randomizer();
            while (true) {
                Thread.sleep(sleepMilis);
                offerProcess(ProcessFactory.newProcessWithRandomSleepTime());
            }
        } catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void offerProcess(Process process) {

        for (Queue<Runnable> queue : queues) {
            try {
                queue.offer(process);
                return;
            } catch (OverflowException exc) {
                System.out.println(exc.getMessage());
            }
        }

        Queue<Runnable> newQueue = new LinkedBlockingQueue();
        newQueue.offer(process);
        queues.add(newQueue);
    }

}
