package com.bielanm.math;

import com.bielanm.FourthSolution;
import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;
import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class MathUtil {

    public static final double EPS = 0.000001;
    private static PoolExecutor poolExecutor = Cuncurent.blockingFixedPoolExecutor(FourthSolution.THREAD_COUNT);

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

    public static Double determinant(Matrix matrix) {
        Matrix copy = Matrix.copy(matrix);
        MathUtil.straightCalc(copy, DoubleVector.newRandom(matrix.getOrder()));
        MathUtil.backCalc(copy, DoubleVector.newRandom(matrix.getOrder()));

        Double det = 1d;
        for (int i = 0; i < copy.getOrder(); i++) {
            det *= copy.getElem(i, i);
        }
        return det;
    }

    public static void straightCalc(Matrix matrix, DoubleVector vector) {
        List<DoubleVector> rows = matrix.getRows();
        List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < matrix.getOrder(); i++) {
            final int columnNumber = i;
            useMainElementByColumn(matrix, vector, columnNumber);
            DoubleVector start = rows.get(columnNumber);
            for (int j = columnNumber + 1; j < matrix.getOrder(); j++) {
                final int row = j;
                final DoubleVector currentVector = rows.get(row);
                if (currentVector.get(columnNumber) != 0) {
                    final Double koef = currentVector.get(columnNumber)/start.get(columnNumber);
                    tasks.add(() -> {
                        rows.set(row, subtraction(currentVector, multiply(start, koef)));
                        vector.set(row, vector.get(row) - vector.get(columnNumber)*koef);
                    });
                }
            }
            poolExecutor.submit(tasks);
            tasks.clear();
        }
    }

    public static void straightCalcMPI(Matrix matrix, DoubleVector vector) {
        int pid = MPI.COMM_WORLD.Rank();

        List<DoubleVector> rows = matrix.getRows();
        List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < matrix.getOrder() - 1; i++) {
            int processCount = MPI.COMM_WORLD.Size();
            double[] toSend = null;
            int[] sizes = null;
            int[] elementsPerProcess = null;
            if(pid == 0) {
                int count = matrix.getOrder() - i - 1;
                useMainElementByColumn(matrix, vector, i);
                DoubleVector start = rows.get(i);
                toSend = new double[(count + 1)* count / 2];
                sizes = new int[count];
                int k = 0;
                int sizesIndex = 0;
                for (int j = i + 1; j < matrix.getOrder(); j++) {
                    int rowSize = matrix.getOrder() - j;
                    DoubleVector currentVector = rows.get(j);
                    double[] row = currentVector.stream().mapToDouble(Double::doubleValue).toArray();
                    System.arraycopy(row, j, toSend, k, rowSize);
                    k += rowSize;
                    sizes[sizesIndex++] = rowSize;
                }

                int sizeIteration = count;
                elementsPerProcess = new int[MPI.COMM_WORLD.Size()];
                int min = count/MPI.COMM_WORLD.Size();
                Arrays.fill(elementsPerProcess, min);
                for (int j = 0; j < count - min*MPI.COMM_WORLD.Size(); j++) {
                    elementsPerProcess[j] = elementsPerProcess[j] + 1;
                }
                System.out.println("No Dno");
            }

            MPI.COMM_WORLD.Scatterv(toSend, 0, elementsPerProcess, sizes, MPI.DOUBLE, null, 0, 0, MPI.DOUBLE, 0);

            System.out.println("Dno");
        }
    }

    private static void useMainElementByColumn(Matrix matrix, DoubleVector vector, int columnNumber) {
        int max = columnNumber;
        for (int i = columnNumber + 1; i < matrix.getOrder(); i++) {
            double next = matrix.getElem(i, columnNumber);
            if(next > matrix.getElem(max, columnNumber)) max = i;
        }

        List<DoubleVector> rows = matrix.getRows();
        DoubleVector toMove = rows.get(columnNumber);
        rows.set(columnNumber, rows.get(max));
        rows.set(max, toMove);

        Double temp = vector.get(columnNumber);
        vector.set(columnNumber, vector.get(max));
        vector.set(max, temp);
    }

    public static void backCalc(Matrix matrix, DoubleVector vector) {
        List<DoubleVector> rows = matrix.getRows();
        List<Runnable> tasks = new ArrayList<>();

        for (int i = matrix.getOrder() - 1; i >= 0; i--) {
            final int columnNumber = i;
            DoubleVector start = rows.get(columnNumber);
            for (int j = columnNumber - 1; j >= 0; j--) {
                final int row = j;
                final DoubleVector currentVector = rows.get(row);
                if (currentVector.get(columnNumber) != 0) continue;
                final Double koef = currentVector.get(columnNumber)/start.get(columnNumber);

                tasks.add(() -> {
                    rows.set(row, subtraction(currentVector, multiply(start, koef)));
                    vector.set(row, vector.get(row) - vector.get(columnNumber)*koef);
                });
            }
            poolExecutor.submit(tasks);
            tasks.clear();
        }
    }

    public static void backCalcMPI(Matrix matrix, DoubleVector vector) {
        List<DoubleVector> rows = matrix.getRows();
        List<Runnable> tasks = new ArrayList<>();

        for (int i = matrix.getOrder() - 1; i >= 0; i--) {
            final int columnNumber = i;
            DoubleVector start = rows.get(columnNumber);
            for (int j = columnNumber - 1; j >= 0; j--) {
                final int row = j;
                final DoubleVector currentVector = rows.get(row);
                if (currentVector.get(columnNumber) != 0) {
                    final Double koef = currentVector.get(columnNumber)/start.get(columnNumber);
                    tasks.add(() -> {
                        rows.set(row, subtraction(currentVector, multiply(start, koef)));
                        vector.set(row, vector.get(row) - vector.get(columnNumber)*koef);
                    });
                }
            }
            poolExecutor.submit(tasks);
            tasks.clear();
        }
    }

    public static void normalize(Matrix matrix, DoubleVector vector) {
        List<DoubleVector> rows = matrix.getRows();
        for (int i = 0; i < matrix.getOrder(); i++) {
            DoubleVector current = rows.get(i);
            Double elem = current.get(i);
            vector.set(i, vector.get(i)/elem);
            current.set(i, elem/elem);
        }
    }

    public static DoubleVector subtraction(DoubleVector v1, DoubleVector v2){
        DoubleVector v = new DoubleVector(v1);
        v.subtraction(v2);
        return v;
    }

    public static DoubleVector multiply(DoubleVector v1, Double v2){
        DoubleVector v = new DoubleVector(v1);
        v.multiply(v2);
        return v;
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
