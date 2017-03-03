package com.bielanm.net.exceptions;


import com.bielanm.net.HttpStatus;

public class HttpExeption extends Exception {

    public final HttpStatus httpStatus;

    public HttpExeption(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
