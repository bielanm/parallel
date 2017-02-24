package com.bielanm.math;

import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MathUtil {

    public static Result multiply(IntVector[] pool1,  IntVector[] pool2) {
        long time = System.currentTimeMillis();
        Integer[] value = fillArray(new Integer[pool1.length], (i) -> new Integer(0));
        for(int i = 0; i < pool1.length; i++){
            value[i] += pool1[i].multiply(pool2[i]);
        }
        return new Result(System.currentTimeMillis() - time, value);
    }

    public static Result multiplyCuncurrent(IntVector[] pool1,  IntVector[] pool2, int POOL_SIZE) throws InterruptedException {
        PoolExecutor executor = Cuncurent.simpleFixedPoolExecutor(POOL_SIZE);
        return null;



//        ExecutorService executor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
//        List<Callable<Integer>> tasks = new ArrayList<>(pool1.length);
//        for(int i = 0; i < pool1.length; i++){
//            final int index = i;
//            tasks.add(() -> pool1[index].multiply(pool2[index]));
//        }
//
//        long time = System.currentTimeMillis();
//        List<Future<Integer>> futures = executor.invokeAll(tasks);
//        time = System.currentTimeMillis() - time;
//
//        Integer result[] = futures
//                .stream()
//                .map(integerFuture -> IntegerToInt(integerFuture))
//                .collect(Collectors.toList())
//                .toArray(new Integer[pool1.length]);
//        executor.shutdown();
//        return new Result(time, result);
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
