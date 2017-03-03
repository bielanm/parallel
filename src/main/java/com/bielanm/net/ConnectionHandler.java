package com.bielanm.net;

import java.io.InputStream;
import java.io.OutputStream;

public interface ConnectionHandler {

    void handle(InputStream is, OutputStream outs) throws Exception;
    void shutDown();
}
