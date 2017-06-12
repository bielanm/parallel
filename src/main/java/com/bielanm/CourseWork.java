package com.bielanm;


import com.bielanm.util.coursework.Integrator;
import com.bielanm.util.coursework.Pair;
import com.bielanm.util.coursework.Valueable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CourseWork {

    private static final double A = 1;
    private static final double a = 1;

    private static final int intervals = 10;
    private static final double xstart = 0;
    private static final double xend = 1;
    private static final double tstart = 0;
    private static final double tend = 1;
    public static void main(String[] args) {
        if(xend - xstart != tend - tstart) throw new NotImplementedException();

        double xstep = (xend - xstart)/intervals;
        double tstep = (tend - tstart)/intervals;

        Valueable solution = (params) -> 1 + A*Math.exp(params[0]/Math.sqrt(2) + a*params[1] - params[1]/2);
        Double[] x = new Double[intervals + 1];
        Double[] t = new Double[intervals + 1];
        Double[] boundary = new Double[intervals + 1];
        Double[] start = new Double[intervals + 1];
        Double[] end = new Double[intervals + 1];
        Supplier<IntStream> range = () -> IntStream.range(0, intervals + 1);
        range.get().boxed().forEach(i -> x[i] = xstart + i*xstep);
        range.get().boxed().forEach(i -> t[i] = tstart + i*tstep);
        range.get().boxed().forEach(i -> boundary[i] = solution.value(xstart + i*xstep, 0));
        range.get().boxed().forEach(i -> start[i] = solution.value(xstart, tstart + i*tstep));
        range.get().boxed().forEach(i -> end[i] = solution.value(xend, tstart  + i*tstep));

        System.out.println();
        Pair<Valueable, Valueable> equation = new Pair<>(
                (params) -> params[0]*(params[3]/(xstep*xstep)) - params[1]*(1 + 2*(params[3]/(xstep*xstep))) + params[2]*(params[3]/(xstep*xstep)),
                (params) -> params[0]*(params[1] + 1) - params[0]*params[0]*params[0]
        );
        Integrator.solveImplicity(x, t, boundary, start, end, equation);

    }

}
