package com.bielanm;

import com.bielanm.math.DoubleVector;
import com.bielanm.math.MathUtil;
import com.bielanm.math.Matrix;
import mpi.MPI;

public class FourthSolution {

    public static final int SLAU_ORDER = 10;
    public static final int THREAD_COUNT = 4;

    public static void main(String[] args) {
        MPI.Init(args);

        Matrix matrix = Matrix.invertibleNewRandom(SLAU_ORDER);
        DoubleVector vector = DoubleVector.newRandom(matrix.getOrder());
        System.out.println("Start matrix:\n" + matrix);
        System.out.println("Start vector:\n" + vector);
        MathUtil.straightCalcMPI(matrix, vector);
        MathUtil.backCalc(matrix, vector);
        MathUtil.normalize(matrix, vector);
        System.out.println("End matrix:\n" + matrix);
        System.out.println("End vector:\n" + vector);

        MPI.Finalize();
    }

}
