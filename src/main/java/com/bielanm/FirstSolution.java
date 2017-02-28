package com.bielanm;


import com.bielanm.math.MathUtil;
import com.bielanm.util.Randomizer;
import com.bielanm.math.IntVector;

import java.util.Arrays;

public class FirstSolution {
    private static final Randomizer rnd = new Randomizer();

    private static final int SIZE_TEST = 10000;
    private static final int VECTOR_SIZE = 10;
    private static final int MAX_VALUE = 10;
    private static final int POOL_SIZE = 2;

    private static final int KERNEL_COUNT = 2;


    public static void main(String[] args) throws InterruptedException {
        IntVector[] pool1 = fillPool(new IntVector[SIZE_TEST]);
        IntVector[] pool2 = fillPool(new IntVector[SIZE_TEST]);

        perfomeForThreadSizes(pool1, pool2, POOL_SIZE);
        perfomeForThreadSizes(pool1, pool2, POOL_SIZE*2);
        perfomeForThreadSizes(pool1, pool2, POOL_SIZE*4);

    }

    private static void perfomeForThreadSizes(IntVector[] pool1, IntVector[] pool2, int poolSize) throws InterruptedException {
        System.out.println("Test count: " + pool1.length + ", vector size " + VECTOR_SIZE + ".");
        MathUtil.Result result1 = MathUtil.multiply(pool1, pool2);
        System.out.println("Sequential vector multiplication take " + result1.time + " miliseconds");
        MathUtil.Result result2 = MathUtil.multiplyThreading(pool1, pool2, POOL_SIZE);
        System.out.println("Cuncurrent with simple threading vector multiplication take " + result2.time + " miliseconds");
        MathUtil.Result result3 = MathUtil.multiplyCuncurrent(pool1, pool2, POOL_SIZE);
        System.out.println("Cuncurrent vector multiplication take " + result3.time + " miliseconds");

        double S = 1.0*result1.time/result3.time;
        double E = S/KERNEL_COUNT;

        System.out.println("S = " + S + ", E = " + E + " for " + poolSize + " threads.");


    }


    private static IntVector[] fillPool(IntVector[] pool){
        for (int i = 0; i < pool.length ; i++) {
            IntVector vector = new IntVector();
            Integer[] arr = fill(new Integer[VECTOR_SIZE], MAX_VALUE);
            for (Integer val: arr) {
                vector.add(val);
            }
            pool[i] = vector;
        }
        return pool;
    }

    private static Integer[] fill(Integer[] arr, int max){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rnd.nextInt(max);
        }
        return arr;
    }

}
