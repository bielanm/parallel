package com.bielanm.os;

import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.os.sleepingbarberproblem.Barber;
import com.bielanm.os.sleepingbarberproblem.BarberQueue;
import com.bielanm.os.sleepingbarberproblem.BarberShop;
import com.bielanm.os.sleepingbarberproblem.Client;
import com.bielanm.util.Randomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public class SleepingBarberProblem {

    public static final int BARBER_SHAVE_TIME = 10;
    public static final int THREAD_COUNT = 16;
    public static final int CLIENTS_COUNT = 100;

    public static final int MAX_QUEUE_SIZE = 7;

    private static final Randomizer randomizer = new Randomizer();

    public static void main(String[] args) {
        List<Client> clients = new CopyOnWriteArrayList<>();
        Stream.iterate(0, i -> {clients.add(new Client()); return i++;}).limit(CLIENTS_COUNT).count();

        BarberQueue barberQueue = new BarberQueue(MAX_QUEUE_SIZE);
        Barber barber = new Barber();
        BarberShop barberShop = new BarberShop(barber, barberQueue);

        List<Runnable> tasks = new ArrayList<>();
        clients.stream().forEach(client -> tasks.add(() -> {
                try {
                    Thread.sleep(abs(randomizer.nextInt() % BARBER_SHAVE_TIME*20));
                } catch (InterruptedException e) {
                    System.err.println("Sleep error");
                }
                barberShop.onClient(client);
        }));

        PoolExecutor executor = Cuncurent.blockingFixedPoolExecutor(THREAD_COUNT);
        executor.submit(tasks);
    }

}
