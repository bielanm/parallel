package com.romanb.exceptions;

public class OverflowException extends RuntimeException {
    public OverflowException() {
    }

    public OverflowException(String s) {
        super(s);
    }

    public OverflowException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OverflowException(Throwable throwable) {
        super(throwable);
    }
}
