package com.bielanm.net;

import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.net.exceptions.HttpFormatException;
import com.sun.corba.se.impl.legacy.connection.SocketFactoryContactInfoListImpl;

import java.io.*;
import java.net.Socket;

import static java.util.Objects.nonNull;

public class HttpConnectionHandler implements ConnectionHandler {

    private final RequestHandler requestHandler;
    private final PoolExecutor poolExecutor;

    public HttpConnectionHandler(RequestHandler requestHandler, PoolExecutor poolExecutor) {
        this.requestHandler = requestHandler;
        this.poolExecutor = poolExecutor;
    }


    @Override
    public void handle(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);

        int length = inputStream.available();
        byte[] requestBody = new byte[length];
        inputStream.read(requestBody);
        String request = new String(requestBody);
        System.out.println("Request:\n" + request);
        final HttpRequest httpRequest = HttpInterfacesFactory.createHttpRequest(request);
        final HttpResponse httpResponse = HttpInterfacesFactory.createHttpResponse(printWriter, httpRequest);
        poolExecutor.submit(() -> {
            try(Socket close = socket) {
                requestHandler.handleRequest(httpRequest, httpResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void close() throws Exception {
        poolExecutor.shutdown();
    }
}
