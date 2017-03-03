package com.bielanm;

import com.bielanm.cuncurency.Cuncurent;
import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.net.ConnectionHandler;
import com.bielanm.net.HttpConnectionHandler;
import com.bielanm.net.HttpRequestHandler;
import com.bielanm.net.RequestHandler;
import com.bielanm.net.Server;
import com.bielanm.net.SocketServer;

public class FifthSolution {

    public static final int POOL_EXECUTOR_SIZE = 10;

    public static void main(String[] args) {
        PoolExecutor poolExecutor = Cuncurent.blockingFixedPoolExecutor(POOL_EXECUTOR_SIZE);
        RequestHandler requestHandler = new HttpRequestHandler();
        ConnectionHandler connectionHandler = new HttpConnectionHandler(requestHandler, poolExecutor);

        Server server = new SocketServer(8181, connectionHandler);
        server.start();
    }

}
