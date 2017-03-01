package com.bielanm.util.secondlab;


import com.bielanm.OverflowException;

import java.util.LinkedList;
import java.util.List;

public class CPUProcess implements Runnable {

    private final long sleepMilis;
    private final List<CPU> cpus = new LinkedList<>();
    private final List<CPUQueue> queues = new LinkedList<>();

    private volatile int processCount = 0;

    public CPUProcess(long sleepMilis) {
        this.sleepMilis = sleepMilis;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(sleepMilis);
                offerProcess(ProcessFactory.newProcessWithRandomExecTime());
            }
        } catch (InterruptedException exc) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StringBuilder msg = new StringBuilder();
            cpus.forEach(cpu -> {
                cpu.shutdown();
                msg.append("\n")
                        .append(cpu.getName() + " execute " + cpu.getExecutedTasks() + "/" + processCount);
            });
            if(queues.size() > 0) {
                CPUQueue last = queues.get(queues.size() - 1);
                msg.append("\n")
                        .append("Last queue: " + last.getName() + ", max size: " + last.getMaxSize());
            } else {
                msg.append("Queues is empty");
            }
            msg.append("\n");

            System.out.println(msg.toString());
        }
    }

    private void offerProcess(Process process) {
        processCount++;
        for (CPUQueue queue : queues) {
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
        CPUQueue newQueue = new CPUQueue();
        newQueue.enqueue(process);
        System.out.println(newQueue.getName() + " was created.");
        queues.add(newQueue);
        cpus.add(new CPU(newQueue));
    }

}
