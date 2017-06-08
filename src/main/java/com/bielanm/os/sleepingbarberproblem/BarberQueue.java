package com.bielanm.os.sleepingbarberproblem;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BarberQueue extends ConcurrentLinkedQueue<Client> {

    private volatile int size = 0;

    private final int maxSize;

    public BarberQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public synchronized Client poll() {
        Client client = super.poll();
        if(client != null){
            size--;
            System.out.println("Pool one, queue size: " + size);
        }

        return client;
    }

    @Override
    public synchronized boolean offer(Client client) {
        if (size >= maxSize) {
            System.out.println("Queue is over, so " + client.getName() + " go home.");
            return false;
        }
        size++;
        System.out.println("Queue size: " + size);
        return super.offer(client);
    }
}
