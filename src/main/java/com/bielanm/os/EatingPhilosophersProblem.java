package com.bielanm.os;

import com.bielanm.cuncurency.AtomicInt;
import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.os.eatingphilosophers.Fork;
import com.bielanm.os.eatingphilosophers.Philosopher;
import com.bielanm.util.Randomizer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EatingPhilosophersProblem {

    private static final Randomizer randomizer = new Randomizer();

    public static final int CYCLES_COUNT = 100;
    public static final int PHILOSOPHER_COUNT = 5;

    public static final int THREAD_COUNT = 5;

    public static void main(String[] args) {

        List<Runnable> lifeCycles = new CopyOnWriteArrayList<>();
        List<Philosopher> philosophers = new CopyOnWriteArrayList<>();
        List<Fork> forks = new CopyOnWriteArrayList<>();

        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            forks.add(new Fork());
        }
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            final Philosopher philosopher = new Philosopher(forks.get(i), forks.get((i+1)%PHILOSOPHER_COUNT));
            philosophers.add(philosopher);
        }
//        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
//            final Philosopher philosopher = philosophers.get(i);
//            System.out.println(philosopher);
//            lifeCycles.add(() -> {
//                AtomicInt count = new AtomicInt(0);
//                try {
//                    while (CYCLES_COUNT >= count.incrementAndGet()) {
//                        System.out.println(philosopher.getName() + " start " + count.get() + " lifecycle.");
//                        philosopher.eat();
//                        philosopher.thinking();
//                        System.out.println(philosopher.getName() + " end " + count.get() + " lifecycle.");
//                    }
//                } catch (InterruptedException exc) {}
//            });
//        }

        AtomicInt count = new AtomicInt(0);
        while (CYCLES_COUNT/4 >= count.incrementAndGet()) {
            for (int j = 0; j < philosophers.size(); j++) {
                final Philosopher philosopher1 = philosophers.get(j);
                final int n = count.get();
                lifeCycles.add(() -> {
                    System.out.println(philosopher1.getName() + " start " + n + " lifecycle.");
                    try {
                        philosopher1.eat();
                        philosopher1.thinking();

                    } catch (InterruptedException e) {}
                    System.out.println(philosopher1.getName() + " end " + n + " lifecycle.");
                });
            }
        }

        PoolExecutor executor = Cuncurent.blockingFixedPoolExecutor(THREAD_COUNT);
        executor.submit(lifeCycles);
        System.out.println("End");
    }

}
