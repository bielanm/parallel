package com.bielanm.util.coursework;import com.bielanm.util.MPIUtil;import java.util.Arrays;import java.util.stream.IntStream;import java.util.stream.Stream;import static java.lang.Double.valueOf;public class Integrator {    public static Double[][]    F  7solveImplicity(Double[] x, Double[] t, Double[] boundary, Double[] start, Double[] end, Pair<Valueable, Valueable> equation) {        Double[][] solution = new Double[boundary.length][start.length];        IntStream.range(0, solution.length).forEach(i -> IntStream.range(0, solution[i].length)                .forEach(j -> solution[i][j] = valueOf(0)));        IntStream.range(0, start.length).boxed().forEach(i -> solution[0][i] = boundary[i]);        IntStream.range(0, start.length).boxed().forEach(i -> solution[i][0] = start[i]);        IntStream.range(0, start.length).boxed().forEach(i -> solution[i][boundary.length - 1] = end[i]);        System.out.println("Початкові умови:\n");        for (int i = 0; i < solution.length; i++) {            for (int j = 0; j < solution[i].length; j++) {                System.out.print(solution[i][j] + " ");            }            System.out.println();        }        Valueable abc = equation.getKey();        Valueable res = equation.getValue();        MPIUtil.sendInt(1, new int[]{solution.length - 1});        for (int i = 1; i < solution.length; i++) {            int size = solution[i].length;            Double[] A = new Double[size-2];            Double[] B = new Double[size-2];            Double[] C = new Double[size-2];            Double[] b = new Double[size-2];            for (int j = 0; j < size - 2; j++) {                A[j] = abc.value(1, 0, 0);                B[j] = abc.value(0, 1, 0);                C[j] = abc.value(0, 0, 1);                b[j] = res.value(solution[i-1][j]);            }            A[0] = valueOf(0);            C[size-3] = valueOf(0);            b[0] -= abc.value(1, 0, 0)*start[i];            b[size-3] -= abc.value(0, 0, 1)*end[i];            Double[] stepsolution = tridiagonalAlgorithm(A, B, C, b);            for (int j = 0; j < stepsolution.length; j++) {                solution[i][j+1] = stepsolution[j];            }        }        return solution;    }    private static Double[] tridiagonalAlgorithm(Double[] A, Double[] B, Double[] C, Double[] b) {        int size = A.length;        int center = size/2;        int startSize = center + 1;        Double[] Astart = new Double[startSize];        Double[] Bstart = new Double[startSize];        Double[] Cstart = new Double[startSize];        Double[] bstart = new Double[startSize];        IntStream.range(0, startSize).boxed().forEach(i -> {            Astart[i] = A[i];            Bstart[i] = B[i];            Cstart[i] = C[i];            bstart[i] = b[i];        });        int endSize = size - center + 1;        Double[] Aend = new Double[endSize];        Double[] Bend = new Double[endSize];        Double[] Cend = new Double[endSize];        Double[] bend = new Double[endSize];        IntStream.range(center - 1, size).boxed().forEach(i -> {            Aend[i - center + 1] = A[i];            Bend[i - center + 1] = B[i];            Cend[i - center + 1] = C[i];            bend[i - center + 1] = b[i];        });        TridiagonalEntity right = straightRight(Astart, Bstart, Cstart, bstart);        Double[] rightAlpha = right.getAlpha();        Double[] rightBetta = right.getBetta();        Stream<Double> AB = Stream.concat(Arrays.stream(Aend), Arrays.stream(Bend));        Stream<Double> Cb = Stream.concat(Arrays.stream(Cend), Arrays.stream(bend));        Double[] buffer = Stream.concat(AB, Cb).toArray(Double[]::new);        double[] bufstraight = IntStream.range(0, buffer.length).mapToDouble(i -> buffer[i]).toArray();        int length = buffer.length;        MPIUtil.sendDouble(1, bufstraight);        double[] left = MPIUtil.readPossibleDouble(1);        Double[] leftAlpha = new Double[left.length/2];        Double[] leftBetta = new Double[left.length/2];        IntStream.range(0, left.length/2).forEach(i -> leftAlpha[i] = valueOf(left[i]));        IntStream.range(0, left.length/2).forEach(i -> leftBetta[i] = valueOf(left[i + left.length/2]));        Double xcenter = (rightBetta[startSize - 1]*leftAlpha[0] + leftBetta[0])/(1 - rightAlpha[startSize - 1]*leftAlpha[0]);        Double[] solutionRight = backRight(right, xcenter);        MPIUtil.sendDouble(1, new double[]{xcenter});        double[] solutionLeft = MPIUtil.readPossibleDouble(1);        Double[] solution = new Double[size];        for(int i = 0; i < size; i++){            solution[i] = i > center ? solutionLeft[i - center + 1] : solutionRight[i];        }        return solution;    }    public static Double[] backLeft(TridiagonalEntity right, Double xcenter) {        Double[] alpha = right.getAlpha();        Double[] betta = right.getBetta();        int size = alpha.length;        Double[] solution = new Double[size];        solution[1] = xcenter;        solution[0] = (solution[1] - betta[0])/(alpha[0]);        for (int i = 1; i < size - 1; i++) {            solution[i+1] = solution[i]*alpha[i] +betta[i];        }        return solution;    }    public static Double[] backRight(TridiagonalEntity right, Double xcenter) {        Double[] alpha = right.getAlpha();        Double[] betta = right.getBetta();        int size = alpha.length;        Double[] solution = new Double[size];        solution[size - 1] = xcenter;        for (int i = size - 1; i > 0; i--) {            solution[i-1] = solution[i]*alpha[i] +betta[i];        }        return solution;    }    public static TridiagonalEntity straightRight(Double[] A, Double[] B, Double[] C, Double[] b) {        int size = A.length;        Double[] alpha = new Double[size];        Double[] betta = new Double[size];        IntStream.range(0, size).forEach(i -> {            alpha[i] = valueOf(0);            betta[i] = valueOf(0);        });        for (int i = 0; i < size - 1; i++) {            double gamma = (A[i]*alpha[i] + B[i]);            alpha[i+1] = -C[i]/gamma;            betta[i+1] = (b[i] - A[i]*betta[i])/gamma;        }        return TridiagonalEntity.create(alpha, betta);    }    public static TridiagonalEntity straightLeft(Double[] A, Double[] B, Double[] C, Double[] b) {        int size = A.length;        Double[] alpha = new Double[size];        Double[] betta = new Double[size];        IntStream.range(0, size).forEach(i -> {            alpha[i] = valueOf(0);            betta[i] = valueOf(0);        });        for (int i = size - 1; i > 0; i--) {            double gamma = (C[i]*alpha[i] + B[i]);            alpha[i-1] = -A[i]/gamma;            betta[i-1] = (b[i] - C[i]*betta[i])/gamma;        }        return TridiagonalEntity.create(alpha, betta);    }    public static abstract class TridiagonalEntity {        public abstract Double[] getBetta();        public abstract Double[] getAlpha();        static TridiagonalEntity create(Double[] alpha, Double[] betta) {            return new TridiagonalEntity() {                @Override                public Double[] getBetta() {                    return betta;                }                @Override                public Double[] getAlpha() {                    return alpha;                }            };        }    }}