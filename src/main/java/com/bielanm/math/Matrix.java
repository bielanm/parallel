package com.bielanm.math;

import com.bielanm.util.Randomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bielanm.math.MathUtil.EPS;

public class Matrix {

    public static final int MAX_ELEMENT = 100;

    public static final Randomizer rnd = new Randomizer();

    List<DoubleVector> rows;
    Double det;

    public Matrix(List<DoubleVector> rows) {
        this.rows = rows;
    }

    public Double getDet() {
        return Optional.ofNullable(det)
                .orElseGet(() -> det = MathUtil.determinant(this));
    }

    public Integer getOrder() {
        return rows.size();
    }

    public List<DoubleVector> getRows() {
        return rows;
    }

    public static Matrix newRandom(int n) {
        List<DoubleVector> rows = new ArrayList<>();
        Stream.iterate(0, i -> {
            rows.add(DoubleVector.newRandom(n, Math.abs(rnd.nextInt(MAX_ELEMENT)) + 1));
            return i;
        }).limit(n + 1).collect(Collectors.toList());
        return new Matrix(rows);
    }

    public static Matrix invertibleNewRandom(int n) {
        while (true) {
            Matrix matrix = newRandom(n);
            if(Math.abs(copy(matrix).getDet()) > EPS) {
                return matrix;
            }
        }
    }


    public Double getElem(int v1, int v2) {
        return rows.get(v1).get(v2);
    }


    @Override
    public String toString() {
        return rows
                .stream()
                .map(vector -> vector.toString())
                .collect(Collectors.joining("\n"));
    }

    public static Matrix copy(Matrix matrix) {
        List<DoubleVector> rows = new ArrayList<>();
        List<DoubleVector> existing = matrix.getRows();
        for (DoubleVector vector: existing) {
            rows.add(new DoubleVector(vector));
        }
        return new Matrix(rows);
    }
}
