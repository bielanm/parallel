package com.bielanm.util.third;

@FunctionalInterface
public interface Condition<T> {
    boolean apply(T value);
}
