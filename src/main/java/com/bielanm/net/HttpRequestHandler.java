package com.bielanm.net;

import com.bielanm.net.exceptions.HttpExeption;

public class HttpRequestHandler implements RequestHandler {

    RequestDispatcher requestDispatcher;

    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            requestDispatcher.dispatch(httpRequest, httpResponse);
        } catch (HttpExeption httpExeption) {
            httpResponse.setStatus(httpExeption.getHttpStatus());
            httpResponse.end();
        }
    }

}
