package com.bielanm.net;

import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.FixedPoolExecutorImpl;
import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.expresslike.ExpresslikeRequestHandler;

public class HttpServer extends SocketServer {

    public static final int POOL_EXECUTOR_SIZE = 10;

    public HttpServer(int port, RequestHandler requestHandler) {
        super(port);
        PoolExecutor poolExecutor = Cuncurent.simpleFixedPoolExecutor(POOL_EXECUTOR_SIZE);
        ConnectionHandler connectionHandler = new HttpConnectionHandler(requestHandler, poolExecutor);
        setСonnectionHandler(connectionHandler);
    }
}
