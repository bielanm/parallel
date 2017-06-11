package com.bielanm.os.sleepingbarberproblem;

import com.bielanm.cuncurency.AtomicInt;
import com.bielanm.cuncurency.Lock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BarberShop {

    private final Barber barber;
    private final Queue<Client> barberQueue;

    private final Lock doorLock;

    private AtomicInt integer = new AtomicInt(0);

    public BarberShop(Barber barber, Queue<Client> barberQueue) {
        this.barber = barber;
        this.barberQueue = barberQueue;
        this.doorLock = new Lock();
    }

    public void onClient(Client client) {
        doorLock.lock();
        client.setName("Client_" + integer.incrementAndGet());
        System.out.println(client.getName() + " come in the barber-shop.");
        System.out.println(client.getName() + " check barber.");
        if(barber.isSleep()) {
            System.out.println(client.getName() + " wake up barber.");
            barber.wake();
            System.out.println("Barber start shave " + client.getName());
            doorLock.unlock();
            barber.shave(client);
            while (true) {
                doorLock.lock();
                System.out.println("Barber end shaving.");
                System.out.println("Barber go to check queue.");
                Client newClient = barberQueue.poll();
                if(newClient != null) {
                    System.out.println("Barber start shave " + newClient.getName());
                    doorLock.unlock();
                    barber.shave(newClient);
                } else {
                    System.out.println("Queue is empty, so barber go to bed.");
                    doorLock.unlock();
                    barber.sleep();
                    break;
                }
            }
        } else {
            System.out.println(client.getName() + " go to queue, becouse of barber is busy.");
            barberQueue.offer(client);
            doorLock.unlock();
        }
        ConcurrentLinkedQueue<Client> l;
    }

    public void printStatus() {
        System.out.println("Barber is " + (barber.isSleep() ? "" : " not") + " sleeping, queue size " + barberQueue.size());
    }
}
