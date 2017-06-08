package com.bielanm.os;

import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.os.producerconsumer.ProcessExecutor;
import com.bielanm.os.producerconsumer.ProcessQueue;
import com.bielanm.os.producerconsumer.Producer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumerProblem {

    public static final int PRODUCER_SLEEP_TIME = 20;
    public static final int MAX_PROCESS_COUNT = 333;
    public static final int THREAD_COUNT = 1;

    private static final int MAX_QUEUE_SIZE = 5;

    public static void main(String[] args) throws InterruptedException {
        ProcessQueue queue = new ProcessQueue("Queue", MAX_QUEUE_SIZE);

        ProcessExecutor executor = new ProcessExecutor(queue, THREAD_COUNT, "Executor");
        Producer producer1 = new Producer(queue, PRODUCER_SLEEP_TIME, MAX_PROCESS_COUNT);
        Producer producer2 = new Producer(queue, PRODUCER_SLEEP_TIME, MAX_PROCESS_COUNT);
        Producer producer3 = new Producer(queue, PRODUCER_SLEEP_TIME, MAX_PROCESS_COUNT);

        ExecutorService executorProduce = Executors.newFixedThreadPool(3);
        executorProduce.submit(producer1);
        executorProduce.submit(producer2);
        executorProduce.submit(producer3);

        Thread.sleep(PRODUCER_SLEEP_TIME*MAX_PROCESS_COUNT*5);

        String msg = new StringBuilder()
                .append(executor.getName() + " ")
                .append(executor.getExecutedTasks() + " executed tasks.")
                .toString();
        System.out.println(msg);

        executor.shutdown();
    }

}
