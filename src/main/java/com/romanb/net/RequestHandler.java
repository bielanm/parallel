package com.romanb.net;

public interface RequestHandler {

    void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse);
}
