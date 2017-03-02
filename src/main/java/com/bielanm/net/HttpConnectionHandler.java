package com.bielanm.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static java.util.Objects.nonNull;

public class HttpConnectionHandler implements ConnectionHandler {

    private final HttpRequestHandler httpRequestHandler;
    private final HttpInterfacesFactory httpInterfacesFactory;

    public HttpConnectionHandler(HttpRequestHandler httpRequestHandler, HttpInterfacesFactory httpInterfacesFactory) {
        this.httpRequestHandler = httpRequestHandler;
        this.httpInterfacesFactory = httpInterfacesFactory;
    }


    @Override
    public void handle(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter printWriter = new PrintWriter(outputStream);

        StringBuilder request = new StringBuilder();

        String line = null;
        while (nonNull(line = bufferedReader.readLine())) {
            request.append(line).append("\n");
        }

        HttpRequest httpRequest = httpInterfacesFactory.createHttpRequest(request.toString());
        HttpResponse httpResponse = httpInterfacesFactory.createHttpResponse(printWriter);
        httpRequestHandler.handleRequest(httpRequest, httpResponse);
    }

    @Override
    public void shutDown() {
    }
}
