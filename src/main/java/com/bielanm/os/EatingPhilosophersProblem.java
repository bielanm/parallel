package com.bielanm.os;

import com.bielanm.cuncurency.AtomicInteger;
import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.os.eatingphilosophers.Fork;
import com.bielanm.os.eatingphilosophers.Philosopher;

import java.util.ArrayList;
import java.util.List;

public class EatingPhilosophersProblem {

    public static final int CYCLES_COUNT = 100;
    public static final int PHILOSOPHER_COUNT = 3;

    public static final int THREAD_COUNT = 3;

    public static void main(String[] args) {

        List<Runnable> lifeCycles = new ArrayList<>();
        List<Philosopher> philosophers = new ArrayList<>();
        List<Fork> forks = new ArrayList<>();

        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            forks.add(new Fork());
        }
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            final Philosopher philosopher = new Philosopher(forks.get(i), forks.get((i+1)%PHILOSOPHER_COUNT));
            philosophers.add(philosopher);
        }
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            final Philosopher philosopher = philosophers.get(i);
            System.out.println(philosopher);
            lifeCycles.add(() -> {
                AtomicInteger count = new AtomicInteger(0);
                try {
                    while (CYCLES_COUNT >= count.incrementAndGet()) {
                        System.out.println(philosopher.getName() + " start " + count.get() + " lifecycle.");
                        philosopher.eat();
                        philosopher.thinking();
                        System.out.println(philosopher.getName() + " end " + count.get() + " lifecycle.");
                    }
                } catch (InterruptedException exc) {}
            });
        }

        PoolExecutor executor = Cuncurent.blockingFixedPoolExecutor(THREAD_COUNT);
        executor.submit(lifeCycles);
        System.out.println("End");
    }

}
