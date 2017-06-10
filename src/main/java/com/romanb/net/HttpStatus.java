package com.romanb.net;

public enum HttpStatus {

    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    FOUND(302),
    ERROR(500);

    public final int code;

    HttpStatus(int code) {
        this.code = code;
    }
}
