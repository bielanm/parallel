package com.bielanm;

import com.bielanm.math.DoubleVector;
import com.bielanm.math.Matrix;

public class FourthSolution {

    public static final int SLAU_ORDER = 4;

    public static void main(String[] args) {
        Matrix matrix = Matrix.invertibleNewRandom(4);
        DoubleVector vector = DoubleVector.newRandom(matrix.getOrder());
        System.out.println(matrix);
        System.out.println(vector);

    }

}
