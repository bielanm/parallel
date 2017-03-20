package com.bielanm.os.sleepingbarberproblem;

import com.bielanm.cuncurency.Lock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BarberShop {

    private final Barber barber;
    private final Queue<Client> barberQueue;

    private final Lock doorLock;

    public BarberShop(Barber barber, Queue<Client> barberQueue) {
        this.barber = barber;
        this.barberQueue = barberQueue;
        this.doorLock = new Lock();
    }

    public void onClient(Client client) {
        printStatus();
        doorLock.lock();
        if(barber.isSleep()) {
            printStatus();
            barber.wake();
            printStatus();
            doorLock.unlock();
            barber.shave(client);
            while (true) {
                printStatus();
                doorLock.lock();
                Client newClient = barberQueue.poll();
                if(newClient != null) {
                    doorLock.unlock();
                    barber.shave(newClient);
                } else {
                    doorLock.unlock();
                    barber.sleep();
                    break;
                }
            }
            printStatus();
        } else {
            printStatus();
            barberQueue.offer(client);
            doorLock.unlock();
            printStatus();
        }
        ConcurrentLinkedQueue<Client> l;
    }

    public void printStatus() {
        System.out.println("Barber is " + (barber.isSleep() ? "" : " not") + " sleeping, queue size " + barberQueue.size());
    }
}
