package com.bielanm;

import com.bielanm.net.ConnectionHandler;
import com.bielanm.net.HttpConnectionHandler;
import com.bielanm.net.HttpInterfacesFactory;
import com.bielanm.net.HttpRequestParser;
import com.bielanm.net.Server;
import com.bielanm.net.SocketServer;

public class FifthSolution {

    public static void main(String[] args) {
        HttpRequestParser httpRequestParser = new HttpRequestParser();
        HttpInterfacesFactory httpInterfacesFactory = new HttpInterfacesFactory(httpRequestParser);
        ConnectionHandler connectionHandler = new HttpConnectionHandler(null, httpInterfacesFactory);

        Server server = new SocketServer(8181, connectionHandler);
        server.start();
    }

}
