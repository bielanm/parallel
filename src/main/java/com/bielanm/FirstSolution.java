package com.bielanm;


import com.bielanm.math.MathUtil;
import com.bielanm.util.Randomizer;
import com.bielanm.math.IntVector;

import java.util.Arrays;

public class FirstSolution {
    private static final Randomizer rnd = new Randomizer();

    private static final int SIZE = 1000;
    private static final int VECTOR_SIZE = 100000;
    private static final int MAX = 1000;
    private static final int POOL_SIZE = 4;


    public static void main(String[] args) throws InterruptedException {
        IntVector[] pool1 = fillPool(new IntVector[SIZE]);
        IntVector[] pool2 = fillPool(new IntVector[SIZE]);

        MathUtil.Result result = MathUtil.multiplyCuncurrent(pool1, pool2, POOL_SIZE);
        System.out.println("Result: " + result.value + ".");
        System.out.println("Cuncurrent vector multiplication take " + result.time + " miliseconds");
        result = MathUtil.multiply(pool1, pool2);
        System.out.println("Result: " + result.value + ".");
        System.out.println("Sequential vector multiplication take " + result.time + " miliseconds");
    }

    private static IntVector[] fillPool(IntVector[] pool){
        for (int i = 0; i < pool.length ; i++) {
            IntVector vector = new IntVector();
            vector.addAll(Arrays.asList(fill(new Integer[VECTOR_SIZE], MAX)));
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
