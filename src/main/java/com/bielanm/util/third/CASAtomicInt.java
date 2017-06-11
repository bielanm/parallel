package com.bielanm.util.third;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class CASAtomicInt extends AtomicInteger {


    public CASAtomicInt(int i) {
        super(i);
    }

    public int CASaddAndGet(int value) {
        int current;
        do {
            current = get();
        } while (!compareAndSet(current, current + value));

        return current;
    }

    public int updateByCondition(int value, Condition<Integer> condition) {
        while (true) {
            int current = get();
            if(!condition.apply(current)) {
                return current;
            } else {
                if(compareAndSet(current, value)) {
                    return value;
                }
            }
        }
    }

    public int updateMap(Function<Integer, Integer> mapper) {
        while (true) {
            int current = get();
            if(compareAndSet(current, mapper.apply(current))) {
                return current;
            }
        }
    }

}
