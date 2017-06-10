package com.romanb.net;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class HttpConnectionHandler implements ConnectionHandler {

    private final int HTTP_LENGTH = 10000;

    private final RequestHandler requestHandler;
    private final ExecutorService poolExecutor;

    public HttpConnectionHandler(RequestHandler requestHandler, ExecutorService poolExecutor) {
        this.requestHandler = requestHandler;
        this.poolExecutor = poolExecutor;
    }


    @Override
    public void handle(Socket socket) throws IOException {


        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);

        byte[] bytes = new byte[HTTP_LENGTH];
        int length = inputStream.read(bytes);
        if(length == -1) {
            throw new IOException("Empty stream");
        }
        String request = new String(bytes, 0, length);
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
