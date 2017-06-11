package com.bielanm.os.eatingphilosophers;

import com.bielanm.cuncurency.AtomicInt;
import com.bielanm.util.Randomizer;

public class Philosopher {

    private static final Randomizer random = new Randomizer();
    private static final long SLEEP_TIME_MILLIS = 10;
    private static final long EAT_TIME_MILLIS = 50;

    private final String name;

    private final Fork leftFork;
    private final Fork rightFork;

    private static AtomicInt counter = new AtomicInt(0);

    public Philosopher(String name, Fork leftFork, Fork rightFork) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public Philosopher(Fork leftFork, Fork rightFork) {
        this("Philosopher_" + counter.incrementAndGet(), leftFork, rightFork);
    }

    public void eatWithPropablyDeadLock() throws InterruptedException {
        System.out.println(getName() +" try to take " + leftFork.getName() + ".");
        synchronized (leftFork) {
            System.out.println(getName() +" already take " + leftFork.getName() + ".");
            System.out.println(getName() +" try to take " + rightFork.getName() + ".");
            synchronized (rightFork) {
                System.out.println(getName() +" already take " + rightFork.getName() + ".");
                System.out.println(getName() +" start eating.");
                Thread.sleep(Math.abs(random.nextLong() % EAT_TIME_MILLIS));
                System.out.println(getName() +" end eating.");
            }
        }
    }

    public void eat() throws InterruptedException {
        Fork first, second;
        if(leftFork.getNumber() > rightFork.getNumber()) {
            first = leftFork;
            second = rightFork;
        } else {
            first = rightFork;
            second = leftFork;
        }
        System.out.println(getName() +" try to take " + first.getName() + ".");
        synchronized (first) {
            System.out.println(getName() +" already take " + first.getName() + ".");
            System.out.println(getName() +" try to take " + second.getName() + ".");
            synchronized (second) {
                System.out.println(getName() +" already take " + second.getName() + ".");
                System.out.println(getName() +" start eating.");
                Thread.sleep(Math.abs(random.nextLong() % EAT_TIME_MILLIS));
                System.out.println(getName() +" end eating.");
            }
        }
    }

    public void thinking() throws InterruptedException {
        System.out.println(getName() +" start thinking.");
        Thread.sleep(Math.abs(random.nextLong() % SLEEP_TIME_MILLIS));
        System.out.println(getName() +" end thinking.");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + "(left fork: " + leftFork.getName() + ", right fork: " + rightFork.getName() + ")";
    }
}
