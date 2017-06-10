package com.romanb.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer extends SocketServer {

    public static final int POOL_EXECUTOR_SIZE = 3;

    public HttpServer(int port, RequestHandler requestHandler) {
        super(port);
        ExecutorService poolExecutor = Executors.newFixedThreadPool(POOL_EXECUTOR_SIZE);
        setСonnectionHandler(new HttpConnectionHandler(requestHandler, poolExecutor));
    }
}
