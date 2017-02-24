package com.bielanm.math;

import com.bielanm.util.NotNullArrayList;

public class IntVector extends NotNullArrayList<Integer> implements Vector<Integer>{

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
}
