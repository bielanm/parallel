package com.bielanm.util.coursework;


public class Pair<T, R>{

    private final T first;
    private final R second;


    public Pair(T first, R second) {
        this.first = first;
        this.second = second;
    }

    public T getKey() {
        return first;
    }

    public R getValue() {
        return second;
    }
}
