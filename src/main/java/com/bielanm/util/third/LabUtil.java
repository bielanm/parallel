package com.bielanm.util.third;


import com.bielanm.util.InterruptedContext;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LabUtil {

    public static int parallelizeSum(Stream<Integer> data, ExecutorService executorService) {
        final CASAtomicInt atomic = new CASAtomicInt(0);
        InterruptedContext.execute(() -> executorService.invokeAll(data
                .map(value -> (Callable<Integer>) () -> atomic.CASaddAndGet(value))
                .collect(Collectors.toList())));

        return atomic.get();
    }

    public static Boolean[] parallelizeByCondition(int[] data, Condition<Integer> condition, ExecutorService executorService) throws InterruptedException {
        List<Boolean> list = executorService.invokeAll(IntStream.of(data).boxed()
                .map(value -> (Callable<Boolean>) () -> condition.apply(value))
                .collect(Collectors.toList()))
                .stream().sequential().map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        return Boolean.FALSE;
                    }
                }).collect(Collectors.toList());

        Boolean[] result = new Boolean[list.size()];
        list.toArray(result);

        return result;
    }

    public static int parallelizeMax(int[] data, ExecutorService executorService) {
        final CASAtomicInt maxIndex = new CASAtomicInt(0);
        InterruptedContext.execute(() -> {
            executorService.invokeAll(IntStream.range(0, Long.valueOf(data.length).intValue()).boxed()
                    .map(index -> (Callable<Integer>) () -> maxIndex.updateByCondition(index, max -> data[max] < data[index]))
                    .collect(Collectors.toList()));
        });

        return maxIndex.get();
    }

    public static int parallelizeMin(int[] data, ExecutorService executorService) {
        final CASAtomicInt minIndex = new CASAtomicInt(0);
        InterruptedContext.execute(() -> executorService
                .invokeAll(IntStream.range(0, Long.valueOf(data.length).intValue()).boxed()
                        .map(index -> (Callable<Integer>) () -> minIndex.updateByCondition(index, max -> data[max] > data[index]))
                        .collect(Collectors.toList())));

        return minIndex.get();
    }

    public static int checksum(int[] data, ExecutorService executorService) {
        final CASAtomicInt checksum = new CASAtomicInt(0);
        InterruptedContext.execute(() -> {
            executorService
                    .invokeAll(IntStream.of(data).boxed()
                            .map(value -> (Callable<Integer>) () -> checksum.updateMap(current -> current ^ value))
                            .collect(Collectors.toList()));
        });

        return checksum.get();
    }

}
