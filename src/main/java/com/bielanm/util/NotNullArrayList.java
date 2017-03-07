package com.bielanm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class NotNullArrayList<T> extends ArrayList<T> {

    public NotNullArrayList(int i) {
        super(i);
    }

    public NotNullArrayList() {
    }

    public NotNullArrayList(Collection<? extends T> collection) {
        super(collection);
    }

    @Override
    public boolean add(T t) {
        Optional.ofNullable(t).orElseThrow(() -> new NullPointerException());
        return super.add(t);
    }

    @Override
    public T set(int index, T element) {
        Optional.ofNullable(element).orElseThrow(() -> new NullPointerException());
        return super.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        Optional.ofNullable(element).orElseThrow(() -> new NullPointerException());
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if(c.contains(null)) throw new NullPointerException();
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if(c.contains(null)) throw new NullPointerException();
        return super.addAll(index, c);
    }
}
