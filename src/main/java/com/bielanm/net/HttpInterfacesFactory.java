package com.bielanm.net;

import java.io.PrintWriter;

public class HttpInterfacesFactory {

    private final HttpRequestParser httpRequestParser;


    public HttpInterfacesFactory(HttpRequestParser httpRequestParser) {
        this.httpRequestParser = httpRequestParser;
    }

    public HttpRequest createHttpRequest(String request) {
        return null;
    }

    public HttpResponse createHttpResponse(PrintWriter writer) {
        return null;
    }
}
