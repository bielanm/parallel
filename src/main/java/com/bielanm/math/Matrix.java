package com.bielanm.math;

import com.bielanm.util.Randomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Matrix {

    public static final int MAX_ELEMENT = 10;

    public static final Randomizer rnd = new Randomizer();

    List<DoubleVector> rows;
    Double det;

    public Matrix(List<DoubleVector> rows) {
        this.rows = rows;
    }

    public Double getDet() {
        return Optional.ofNullable(det)
                .orElseGet(() -> {
                    Double det = 1d;
                    this.det = det;
                    return det; //TODO
                });
    }

    public Integer getOrder() {
        return rows.size();
    }

    public static Matrix newRandom(int n) {
        List<DoubleVector> rows = new ArrayList<>();
        Stream.iterate(0, i -> {
            rows.add(DoubleVector.newRandom(n, rnd.nextInt(MAX_ELEMENT)));
            return i;
        }).limit(n + 1).collect(Collectors.toList());
        return new Matrix(rows);
    }

    public static Matrix invertibleNewRandom(int n) {
        while (true) {
            Matrix matrix = newRandom(n);
            if(matrix.getDet() != 0) {
                return matrix;
            }
        }
    }

    @Override
    public String toString() {
        return rows
                .stream()
                .map(vector -> vector.toString())
                .collect(Collectors.joining("\n"));
    }
}
