package com.bielanm.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface ConnectionHandler extends AutoCloseable {

    void handle(Socket socket) throws IOException;

    @Override
    void close() throws Exception;
}
