package com.romanb.net;

import com.romanb.cuncurency.Cuncurent;
import com.romanb.cuncurency.PoolExecutor;

public class HttpServer extends SocketServer {

    public static final int POOL_EXECUTOR_SIZE = 3;

    public HttpServer(int port, RequestHandler requestHandler) {
        super(port);
        PoolExecutor poolExecutor = Cuncurent.simpleFixedPoolExecutor(POOL_EXECUTOR_SIZE);
        setСonnectionHandler(new HttpConnectionHandler(requestHandler, poolExecutor));
    }
}
