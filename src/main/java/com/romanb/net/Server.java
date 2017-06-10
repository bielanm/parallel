package com.romanb.net;

public interface Server extends AutoCloseable {

    void start();
    void close() throws Exception;

}
