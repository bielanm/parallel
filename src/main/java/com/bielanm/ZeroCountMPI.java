package com.bielanm;

import com.bielanm.math.IntVector;
import com.bielanm.util.Randomizer;
import mpi.MPI;

import java.util.stream.IntStream;

public class ZeroCountMPI {

    private static final Randomizer rnd = new Randomizer();

    private static final int SIZE_TEST = 10000;
    private static final int MAX_VALUE = 10;


    public static void main(String[] args) throws InterruptedException {


        MPI.Init(args);
        int pid = MPI.COMM_WORLD.Rank();
        int processCount = MPI.COMM_WORLD.Size();

        if(SIZE_TEST%processCount != 0) throw new IllegalArgumentException();

        int elementPerProcess = SIZE_TEST/processCount;

        int[] sendBuffer = new int[SIZE_TEST];
        int[] reciveBuffer = new int[elementPerProcess];
        if(pid == 0) {
            Integer[] vector1 = fill(new Integer[SIZE_TEST], MAX_VALUE);
            IntStream.range(0, vector1.length).forEach(i -> sendBuffer[i] = vector1[i]);
        }

        MPI.COMM_WORLD.Scatter(sendBuffer, 0, elementPerProcess, MPI.INT, reciveBuffer, 0, elementPerProcess, MPI.INT, 0);

        int result = 0;
        for (int i = 0; i < reciveBuffer.length; i++) {
            if(reciveBuffer[i] == 0) result++;
        }

        int[] ret = new int[]{result};
        int[] recieve = new int[1];
        MPI.COMM_WORLD.Reduce(ret, 0, recieve, 0, 1, MPI.INT, MPI.SUM, 0);

        if(pid == 0) {
            System.out.println("Result: " + recieve[0]);
        }

        MPI.Finalize();
    }

    private static Integer[] fill(Integer[] arr, int max){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rnd.nextInt(max);
        }
        return arr;
    }

}
