package com.bielanm.util.coursework;

import java.util.stream.IntStream;

public class Integrator {

    public static Double[][] solveImplicity(Double[] x, Double[] t, Double[] boundary, Double[] start, Double[] end, Pair<Valueable, Valueable> equation) {
        Double[][] solution = new Double[start.length][boundary.length];
        IntStream.range(0, solution.length).forEach(i -> IntStream.range(0, solution[i].length)
                .forEach(j -> solution[i][j] = Double.valueOf(0)));
        solution[0] = boundary;
        IntStream.range(0, start.length).boxed().forEach(i -> solution[i][0] = start[i]);
        IntStream.range(0, start.length).boxed().forEach(i -> solution[i][boundary.length - 1] = end[i]);


        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }

        Valueable abc = equation.getKey();
        Valueable res = equation.getValue();
        for (int i = 1; i < solution.length - 1; i++) {
            int size = solution[i].length;
            Double[] A = new Double[size];
            Double[] B = new Double[size];
            Double[] C = new Double[size];
            Double[] b = new Double[size];

            A[0] = Double.valueOf(0);
            B[0] = abc.value(0, 1, 0, t[i]);
            C[0] = abc.value(0, 0, 1, t[i]);
            b[0] = res.value(solution[i-1][0], t[i]) - abc.value(1, 0, 0, t[i])*solution[i][0];
            for (int j = 1; j < solution[i].length - 1; j++) {
                A[j] = abc.value(1, 0, 0, t[i]);
                B[j] = abc.value(0, 1, 0, t[i]);
                C[j] = abc.value(0, 0, 1, t[i]);
                b[j] = res.value(solution[i-1][j], t[i]);
            }

            A[size-1] = abc.value(1, 0, 0, t[i]);
            B[size-1] = abc.value(0, 1, 0, t[i]);
            C[size-1] = Double.valueOf(0);
            b[size-1] = res.value(solution[i-1][size-1], t[i]) - abc.value(0, 0, 1, t[i])*solution[i][size-1];

            solution[i] = tridiagonalAlgorithm(A, B, C, b);
        }




        return solution;
    }

    private static Double[] tridiagonalAlgorithm(Double[] A, Double[] B, Double[] C, Double[] b) {
        int size = A.length;
        int center = size/2;

        Double[] alpha = new Double[size];
        Double[] betta = new Double[size];

        alpha[0] = -C[0]/B[0];
        betta[0] = b[0]/B[0];
        for (int i = 1; i < center; i++) {
            alpha[i] = -C[i]/(A[i]*alpha[i] + B[i]);
            betta[i] = (b[i] - A[i]*C[i])/(A[i]*alpha[i] + B[i]);
        }

        alpha[size-1] = -A[0]/B[0];
        betta[size-1] = b[0]/B[0];
        for (int i = center; i < size; i++) {
            alpha[i] = -C[i]/(A[i]*alpha[i] + B[i]);
            betta[i] = (b[i] - A[i]*C[i])/(A[i]*alpha[i] + B[i]);
        }

        return new Double[0];
    }

}
