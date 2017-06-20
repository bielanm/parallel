package com.bielanm;

import com.bielanm.math.IntVector;
import com.bielanm.math.MathUtil;
import com.bielanm.util.Randomizer;
import mpi.MPI;

import java.util.Arrays;
import java.util.IllegalFormatCodePointException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FirstMPI {

    private static final Randomizer rnd = new Randomizer();

    private static final int SIZE_TEST = 10000;
    private static final int VECTOR_SIZE = 10;
    private static final int MAX_VALUE = 10;
    private static final int POOL_SIZE = 2;

    private static final int KERNEL_COUNT = 2;


    public static void main(String[] args) throws InterruptedException {

        if(SIZE_TEST%4 != 0) throw new IllegalArgumentException();

        MPI.Init(args);
        long time = 0;
        int pid = MPI.COMM_WORLD.Rank();
        int sentBufferSize = SIZE_TEST*2;
        int processCount = MPI.COMM_WORLD.Size();
        int elementPerProcess = SIZE_TEST*2/processCount;

        int[] sendBuffer = new int[SIZE_TEST*2];
        int[] reciveBuffer = new int[elementPerProcess];
        if(pid == 0) {
            Integer[] vector1 = fill(new Integer[SIZE_TEST], MAX_VALUE);
            Integer[] vector2 = fill(new Integer[SIZE_TEST], MAX_VALUE);

            time = System.currentTimeMillis();
            int result1Thread = IntStream.range(0, vector1.length).map(i -> vector1[i]*vector2[i]).sum();
            System.out.println("Time: " + (System.currentTimeMillis() - time) + ", result: " + result1Thread);

            time = System.currentTimeMillis();
            int k = 0;
            for (int i = 0; i < SIZE_TEST; i++) {
                sendBuffer[k] = vector1[i];
                sendBuffer[k+1] = vector2[i];
                k+=2;
            }

        }

        MPI.COMM_WORLD.Scatter(sendBuffer, 0, elementPerProcess, MPI.INT, reciveBuffer, 0, elementPerProcess, MPI.INT, 0);

        int result = 0;
        for (int i = 0; i < reciveBuffer.length; i+=2) {
            result += reciveBuffer[i]*reciveBuffer[i+1];
        }

        int[] ret = new int[]{result};
        int[] recieve = new int[1];
        MPI.COMM_WORLD.Reduce(ret, 0, recieve, 0, 1, MPI.INT, MPI.SUM, 0);

        if(pid == 0) {
            System.out.println("MPI time: " + (System.currentTimeMillis() - time) + ", result: " + recieve[0]);
        }

        MPI.Finalize();
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
