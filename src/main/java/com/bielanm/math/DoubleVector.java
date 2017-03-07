package com.bielanm.math;

import com.bielanm.util.NotNullArrayList;
import com.bielanm.util.Randomizer;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoubleVector extends NotNullArrayList<Double> implements Vector<Double>{

    public static final int MAX_ELEMENT = 10;
    public static final Randomizer rnd = new Randomizer();

    public DoubleVector() {
    }

    public DoubleVector(Collection<Double> collection) {
        super(collection);
    }

    @Override
    public Double multiply(Vector<Double> vector) {
        checkValid(vector);
        Double result = Double.valueOf(0);
        for(int i = 0; i < size(); i++) {
            result += multiply(get(i), vector.get(i));
        }
        return result;
    }

    private Double multiply(Double val1, Double val2){
        return val1.doubleValue()*val2.doubleValue();
    }

    private void checkValid(Vector vector) {
        if(size() != vector.size() || !(vector instanceof IntVector))
            throw new IllegalArgumentException("Vectors should have same size and IntVector type!");
    }

    public static DoubleVector newRandom(int n) {
        return newRandom(n, MAX_ELEMENT);
    }

    public static DoubleVector newRandom(int n, int maxElement) {
        DoubleVector vector = new DoubleVector();
        Stream.iterate(0, i -> {

            vector.add(Double.valueOf(rnd.nextInt(maxElement)));
            return i;
        }).limit(n + 1).collect(Collectors.toList());
        return vector;
    }

    @Override
    public String toString() {
        return stream().map(i -> i.toString()).collect(Collectors.joining(", "));
    }

    public void multiply(Double koef) {
        for (int i = 0; i < size(); i++) {
            set(i, get(i)*koef);
        }
    }

    public void subtraction(DoubleVector vector) {
        for (int i = 0; i < size(); i++) {
            set(i, get(i) - vector.get(i));
        }
    }
}
