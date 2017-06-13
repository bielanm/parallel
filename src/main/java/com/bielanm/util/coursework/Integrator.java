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
        for (int i = 1; i < solution.length; i++) {
            int size = solution[i].length;
            Double[] A = new Double[size-2];
            Double[] B = new Double[size-2];
            Double[] C = new Double[size-2];
            Double[] b = new Double[size-2];

            A[0] = Double.valueOf(0);
            B[0] = abc.value(0, 1, 0, t[i]);
            C[0] = abc.value(0, 0, 1, t[i]);
            b[0] = res.value(solution[i-1][0], t[i-1]) - abc.value(1, 0, 0, t[i])*solution[i][0];
            for (int j = 1; j < solution[i].length - 2; j++) {
                A[j] = abc.value(1, 0, 0, t[i]);
                B[j] = abc.value(0, 1, 0, t[i]);
                C[j] = abc.value(0, 0, 1, t[i]);
                b[j] = res.value(solution[i-1][j-1], t[i-1]);
            }

            A[size-3] = abc.value(1, 0, 0, t[i]);
            B[size-3] = abc.value(0, 1, 0, t[i]);
            C[size-3] = Double.valueOf(0);
            b[size-3] = res.value(solution[i-1][size-1], t[i-1]) - abc.value(0, 0, 1, t[i])*solution[i][size-1];

            Double[] stepsolution = tridiagonalAlgorithm(A, B, C, b);
            for (int j = 0; j < stepsolution.length; j++) {
                solution[i][j+1] = stepsolution[j];
            }
        }

        return solution;
    }

    private static Double[] tridiagonalAlgorithm(Double[] A, Double[] B, Double[] C, Double[] b) {
        int size = A.length;
        int center = size/2;

        Double[] alpha = new Double[size];
        Double[] betta = new Double[size];
        Double[] omega = new Double[size];
        Double[] tetta = new Double[size];


        alpha[0] = -C[0]/B[0];
        betta[0] = b[0]/B[0];
        for (int i = 0; i <= center; i++) {
            alpha[i+1] = -C[i]/(A[i]*alpha[i] + B[i]);
            betta[i+1] = (b[i] - A[i]*betta[i])/(A[i]*alpha[i] + B[i]);
        }

        omega[size-1] = -A[size-1]/B[size-1];
        tetta[size-1] = b[0]/B[0];
        for (int i = size - 1; i >= center; i--) {
            omega[i-1] = -A[i]/(C[i]*omega[i] + B[i]);
            tetta[i-1] = (b[i] - C[i]*tetta[i])/(C[i]*omega[i] + B[i]);
        }

        Double xcenter = (alpha[center+1]*tetta[center+1] + betta[center+1])/(1 - alpha[center+1]*omega[center+1]);

        Double[] solutionMain = new Double[size];
        Double[] solution = new Double[size];

        solution[center] = xcenter;
        solutionMain[center] = xcenter;

        for (int i = center - 1; i >= 0; i--) {
            solutionMain[i] = solutionMain[i+1]*alpha[i+1] +betta[i+1];
        }

        for (int i = center+1; i < size; i++) {
            solution[i] = solution[i-1]*omega[i-1] +tetta[i-1];
        }

        for(int i = center+1; i < size; i++){
            solutionMain[i] = solution[i];
        }
        return solutionMain;
    }

}
