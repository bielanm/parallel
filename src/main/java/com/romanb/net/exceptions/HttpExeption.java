package com.romanb.net.exceptions;


import com.romanb.net.HttpStatus;

public class HttpExeption extends Exception {

    public final HttpStatus httpStatus;

    public HttpExeption(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
