package com.bielanm;


import com.bielanm.util.MPIUtil;
import com.bielanm.util.coursework.Integrator;
import com.bielanm.util.coursework.Pair;
import com.bielanm.util.coursework.Valueable;
import mpi.Datatype;
import mpi.MPI;
import mpi.Status;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CourseWork {

    private static final double A = 1;
    private static final double a = 1;

    private static final int intervals = 100;
    private static final double xstart = 0;
    private static final double xend = 1;
    private static final double tstart = 0;
    private static final double tend = 1;
    public static void main(String[] args) {
        MPI.Init(args);
        int size = MPI.COMM_WORLD.Size();
        if(size != 2)
            throw new IllegalArgumentException("Program require 2 process");

        int currentProcess = MPI.COMM_WORLD.Rank();

        if(currentProcess == 0) {
            if(xend - xstart != tend - tstart) throw new NotImplementedException();

            double xstep = (xend - xstart)/intervals;
            double tstep = (tend - tstart)/intervals;

            Valueable solution = (params) -> 1/(1 + A*Math.exp(params[0]/Math.sqrt(2) + a*params[1] - params[1]/2));
            Double[] x = new Double[intervals + 1];
            Double[] t = new Double[intervals + 1];
            Double[] boundary = new Double[intervals + 1];
            Double[] start = new Double[intervals + 1];
            Double[] end = new Double[intervals + 1];

            Supplier<IntStream> range = () -> IntStream.range(0, intervals + 1);
            range.get().boxed().forEach(i -> {
                t[i] = tstart + i*tstep;
                x[i] = xstart + i*xstep;
                boundary[i] = solution.value(xstart + i*xstep, tstart);
                start[i] = solution.value(xstart, tstart + i*tstep);
                end[i] = solution.value(xend, tstart  + i*tstep);
            });

            System.out.println();
            Pair<Valueable, Valueable> equation = new Pair<>(
                    (params) -> -params[0]*(tstep/(xstep*xstep)) + params[1]*(1 + 2*(tstep/(xstep*xstep))) - params[2]*(tstep/(xstep*xstep)),
                    (params) -> -params[0]*(tstep - 1) + tstep*params[0]*params[0]*params[0]
            );
            Double[][] solutions = Integrator.solveImplicity(x, t, boundary, start, end, equation);

            System.out.println("Розв'язок:");
            for (int i = 0; i < solutions.length; i++) {
                for (int j = 0; j < solutions[i].length; j++) {
                    System.out.print(solutions[i][j] + " ");
                }
                System.out.println();
            }

            System.out.println("Похибка для кожної з точок");
            for (int i = 0; i <= intervals; i++) {
                for (int j = 0; j <= intervals; j++) {
                    System.out.print((solution.value(xstart + j*xstep, tstart + tstep*i) - solutions[i][j]) + " ");
                }
                System.out.println();
            }
        } else if(currentProcess == 1) {

            int iteration = MPIUtil.readPossibleInt(0)[0];
            for (int j = 0; j < iteration; j++) {
                double[] straightLeft = MPIUtil.readPossibleDouble(0);
                int sizeOne = straightLeft.length / 4;
                Double[] A = new Double[sizeOne];
                Double[] B = new Double[sizeOne];
                Double[] C = new Double[sizeOne];
                Double[] b = new Double[sizeOne];
                IntStream.range(0, sizeOne).boxed().forEach(i -> {
                    A[i] = Double.valueOf(straightLeft[i]);
                    B[i] = Double.valueOf(straightLeft[i + sizeOne]);
                    C[i] = Double.valueOf(straightLeft[i + sizeOne * 2]);
                    b[i] = Double.valueOf(straightLeft[i + sizeOne * 3]);
                });


                Integrator.TridiagonalEntity straightSolution = Integrator.straightLeft(A, B, C, b);
                Double[] response = Stream
                        .concat(Arrays.stream(straightSolution.getAlpha()), Arrays.stream(straightSolution.getBetta()))
                        .toArray(Double[]::new);
                double[] bufresponse = IntStream.range(0, response.length).mapToDouble(i -> response[i]).toArray();
                MPIUtil.sendDouble(0, bufresponse);

                double[] xcenterbuf = MPIUtil.readPossibleDouble(0);
                double xcenter = xcenterbuf[0];

                Double[] solution = Integrator.backLeft(straightSolution, xcenter);
                MPIUtil.sendDouble(0, IntStream.range(0, solution.length).mapToDouble(i -> solution[i]).toArray());
            }
        }
        MPI.Finalize();
    }

}
