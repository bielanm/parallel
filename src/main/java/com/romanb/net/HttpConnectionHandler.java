package com.romanb.net;

import com.romanb.cuncurency.PoolExecutor;

import java.io.*;
import java.net.Socket;

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
            try {
                requestHandler.handleRequest(httpRequest, httpResponse);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void close() throws Exception {
        poolExecutor.shutdown();
    }
}
