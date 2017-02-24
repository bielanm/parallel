package com.bielanm.math;

import java.util.List;

public interface Vector<T extends Number> extends List<T> {

    T multiply(Vector<T> vector);

}
