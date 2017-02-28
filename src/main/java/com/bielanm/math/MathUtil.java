package com.bielanm.math;

import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class MathUtil {

    public static Result multiply(IntVector[] pool1,  IntVector[] pool2) {
        Integer[] value = fillArray(new Integer[pool1.length], (i) -> new Integer(0));
        long time = System.currentTimeMillis();
        for(int i = 0; i < pool1.length; i++){
            value[i] += pool1[i].multiply(pool2[i]);
        }
        return new Result(System.currentTimeMillis() - time, value);
    }

    public static Result multiplyCuncurrent(IntVector[] pool1,  IntVector[] pool2, int POOL_SIZE) throws InterruptedException {
        checkValid(Arrays.asList(pool1), Arrays.asList(pool2));

        Integer[] results = new Integer[pool1.length];
        List<Runnable> tasks = new ArrayList<>(pool1.length);
        for (int i = 0; i < pool1.length; i++) {
            final int index = i;
            tasks.add(() -> results[index] = pool1[index].multiply(pool2[index]));
        }
        
        PoolExecutor executor = Cuncurent.blockingFixedPoolExecutor(POOL_SIZE);
        long time = System.currentTimeMillis();
        executor.submit(tasks);
        return new Result(System.currentTimeMillis() - time, results);
    }

    public static Result multiplyThreading(final IntVector[] pool1, final IntVector[] pool2, int POOL_SIZE) throws InterruptedException {
        checkValid(Arrays.asList(pool1), Arrays.asList(pool2));

        final Integer[] results = new Integer[pool1.length];
        List<Thread> threads = new ArrayList<>(POOL_SIZE);
        int range = pool1.length / POOL_SIZE;
        for (int i = 0; i < POOL_SIZE; i++) {
            final int start = i * range;
            final int end = (i == POOL_SIZE - 1) ? pool1.length : start + range;
            threads.add(new Thread(() -> {
                multiply(start, end, pool1, pool2, results);
            }));
        }

        long time = System.currentTimeMillis();
        threads.stream().forEach(thread -> thread.start());

        for (Thread thread: threads) {
            thread.join();
        }

        return new Result(System.currentTimeMillis() - time, results);
    }

    private static void multiply(int start, int end, IntVector[] v1, IntVector[] v2, Integer[] result) {
        for (int i = start; (i < end) && (i < v1.length); i++) {
            result[i] = v1[i].multiply(v2[i]);
        }
    }

    private static void checkValid(Collection one, Collection second) {
        if(one.size() != second.size())
            throw new IllegalArgumentException("Params should have same size!");
    }

    public static <T> T[] fillArray(T[] array, ArrayElementFactory<T> elementFactory) {
        for (int i = 0; i< array.length; ++i) {
            array[i] = elementFactory.create(i);
        }
        return array;
    }


    private static Integer IntegerToInt(Future<Integer> val) {
        try {
            return val.get();
        } catch (Exception e) {
            return 0;
        }
    }

    public static class Result {
        public final long time;
        public final Integer[] value;

        public Result(long time, Integer[] value) {
            this.time = time;
            this.value = value;
        }
    }

    public interface ArrayElementFactory<T> {
        T create(int i);
    }
}
