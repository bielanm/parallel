package com.bielanm;

import com.bielanm.cuncurency.AtomicInt;
import com.bielanm.util.Randomizer;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ZeroCount {

    private static final Randomizer rnd = new Randomizer();

    private static final int SIZE_TEST = 10000;
    private static final int MAX_VALUE = 10;


    public static void main(String[] args) throws InterruptedException {
        Integer[] vector1 = fill(new Integer[SIZE_TEST], MAX_VALUE);
        System.out.println(Stream.of(vector1).map(integer -> integer.toString()).collect(Collectors.joining(" ")));
        System.out.println("Result 1 thread: " +
                Stream.of(vector1).filter(v -> v == 0).count());
        System.out.println("Result parallel stream: " +
                Arrays.asList(vector1).parallelStream().filter((v) -> v == 0).count());
    }

    private static Integer[] fill(Integer[] arr, int max){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rnd.nextInt(max);
        }
        return arr;
    }
}
