package com.romanb.net;

import java.io.IOException;
import java.net.Socket;

public interface ConnectionHandler extends AutoCloseable {

    void handle(Socket socket) throws IOException;

    @Override
    void close() throws Exception;
}
