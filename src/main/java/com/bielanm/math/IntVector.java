package com.bielanm.math;

import com.bielanm.util.NotNullArrayList;
import com.bielanm.util.Randomizer;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntVector extends NotNullArrayList<Integer> implements Vector<Integer>{

    public static final int MAX_ELEMENT = 10;
    public static final Randomizer rnd = new Randomizer();

    @Override
    public Integer multiply(Vector<Integer> vector) {
        checkValid(vector);
        Integer result = Integer.valueOf(0);
        for(int i = 0; i < size(); i++) {
            result += multiply(get(i), vector.get(i));
        }
        return result;
    }

    private Integer multiply(Integer val1, Integer val2){
        return val1.intValue()*val2.intValue();
    }

    private void checkValid(Vector vector) {
        if(size() != vector.size() || !(vector instanceof IntVector))
            throw new IllegalArgumentException("Vectors should have same size and IntVector type!");
    }

    public static IntVector newRandom(int n) {
        return newRandom(n, MAX_ELEMENT);
    }

    public static IntVector newRandom(int n, int maxElement) {
        IntVector vector = new IntVector();
        Stream.iterate(0, i -> {
            vector.add(rnd.nextInt(maxElement));
            return i;
        }).limit(n + 1).collect(Collectors.toList());
        return vector;
    }

    @Override
    public Integer get(int index) {
        return super.get(index);
    }

    @Override
    public String toString() {
        return stream().map(i -> i.toString()).collect(Collectors.joining(", "));
    }
}
