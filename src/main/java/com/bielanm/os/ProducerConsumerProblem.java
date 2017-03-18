package com.bielanm.os;

import com.bielanm.os.producerconsumer.ProcessExecutor;
import com.bielanm.os.producerconsumer.ProcessQueue;
import com.bielanm.os.producerconsumer.Producer;

public class ProducerConsumerProblem {

    public static final int PRODUCER_SLEEP_TIME = 10;
    public static final int MAX_PROCESS_COUNT = 100;
    public static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        ProcessQueue queue = new ProcessQueue("Queue");

        ProcessExecutor executor = new ProcessExecutor(queue, THREAD_COUNT, "Executor");
        Producer producer = new Producer(queue, PRODUCER_SLEEP_TIME, MAX_PROCESS_COUNT);

        Thread thread = new Thread(producer);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        Thread.sleep(PRODUCER_SLEEP_TIME*MAX_PROCESS_COUNT*5);

        String msg = new StringBuilder()
                .append(executor.getName() + " ")
                .append(executor.getExecutedTasks() + " executed tasks.")
                .toString();
        System.out.println(msg);

        executor.shutdown();
    }

}
