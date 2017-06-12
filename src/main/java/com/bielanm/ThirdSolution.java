package com.bielanm;


import com.bielanm.util.Randomizer;
import com.bielanm.util.third.Condition;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bielanm.util.third.LabUtil.*;

public class ThirdSolution {

    public static final int APP_MAXINT = 50;
    public static final int DATA_SIZE = 10000;

    public static final int POOL_SIZE = 4;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

        Randomizer randomizer = new Randomizer();
        int[] data = IntStream.range(0, DATA_SIZE).map(i -> APP_MAXINT).map(randomizer::nextInt).toArray();
        System.out.println(IntStream.of(data).boxed().map(i -> i.toString()).collect(Collectors.joining(", ")));

        long time = System.currentTimeMillis();
        System.out.println("Sum:\t\t\t " + IntStream.of(data).sum() +
                ", execution time: " + (System.currentTimeMillis() - time));

        time  = System.currentTimeMillis();
        System.out.println("Parallelize sum: " + parallelizeSum(IntStream.of(data).boxed(), executor) +
                ", execution time: " + (System.currentTimeMillis() - time));

        System.out.println();
        Condition<Integer> condition = (x) -> x > 3*APP_MAXINT/4;
        time = System.currentTimeMillis();
        System.out.println("Condition check:\t\t\t " +
                IntStream.of(data).boxed().map(condition::apply)
                        .map(i -> i.toString()).collect(Collectors.joining(", ")) +
                ", execution time: " + (System.currentTimeMillis() - time));
        time  = System.currentTimeMillis();
        System.out.println("Parallelize condition check: " +
                Arrays.asList(parallelizeByCondition(data, (x) -> x > 3*APP_MAXINT/4, executor))
                .stream().map(i -> i.toString()).collect(Collectors.joining(", ")) +
                ", execution time: " + (System.currentTimeMillis() - time));

        System.out.println();
        time = System.currentTimeMillis();
        System.out.println("Max index:\t\t\t   " +
                IntStream.range(0, data.length)
                        .reduce(0, (maxIndex, current) -> data[maxIndex] < data[current] ? current : maxIndex) +
                ", execution time: " + (System.currentTimeMillis() - time));
        time  = System.currentTimeMillis();
        System.out.println("Parallelize max index: " + parallelizeMax(data, executor) +
                ", execution time: " + (System.currentTimeMillis() - time));

        System.out.println();
        time = System.currentTimeMillis();
        System.out.println("Min index:\t\t\t   " +
                IntStream.range(0, data.length)
                        .reduce(0, (maxIndex, current) -> data[maxIndex] > data[current] ? current : maxIndex) +
                ", execution time: " + (System.currentTimeMillis() - time));

        time  = System.currentTimeMillis();
        System.out.println("Parallelize min index: " + parallelizeMin(data, executor) +
                ", execution time: " + (System.currentTimeMillis() - time));

        System.out.println();
        time = System.currentTimeMillis();
        System.out.println("Checksum:\t\t\t   " +
                IntStream.of(data).reduce(0, (checksum, current) -> checksum ^ current) +
                ", execution time: " + (System.currentTimeMillis() - time));

        time  = System.currentTimeMillis();
        System.out.println("Parallelize min index: " + checksum(data, executor) +
                ", execution time: " + (System.currentTimeMillis() - time));

        executor.shutdown();
    }


}
