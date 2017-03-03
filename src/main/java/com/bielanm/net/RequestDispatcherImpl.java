package com.bielanm.net;

import com.bielanm.net.exceptions.HttpExeption;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestDispatcherImpl implements RequestDispatcher {

    Map<String, Map<HttpMethod, RequestHandler>> handlerMapper;

    public RequestDispatcherImpl() {
        this.handlerMapper = new HashMap<>();
    }

    @Override
    public void dispatch(HttpRequest httpRequest, HttpResponse httpResponse) throws HttpExeption {
        Optional.ofNullable(Optional.ofNullable(handlerMapper.get(httpRequest.getUrl().getPath()))
                .orElseGet(() -> Collections.<HttpMethod, RequestHandler>emptyMap())
                .get(httpRequest.getMethod()))
                .orElseThrow(() -> new HttpExeption(HttpStatus.NOT_FOUND))
                .handleRequest(httpRequest, httpResponse);
    }

    public void addHandler(String path, RequestHandler handler, HttpMethod httpMethod) {
        Map<HttpMethod, RequestHandler> handlerMap = handlerMapper.get(path);

        if(handlerMap != null) {
            handlerMap.put(httpMethod, handler);
        } else {
            handlerMap = new HashMap<>();
            handlerMap.put(httpMethod, handler);
            handlerMapper.put(path, handlerMap);
        }
    }

    public RequestDispatcherImpl addGet(String path, RequestHandler handler) {
        addHandler(path, handler, HttpMethod.GET);
        return this;
    }
    public RequestDispatcherImpl addDelete(String path, RequestHandler handler) {
        addHandler(path, handler, HttpMethod.DELETE);
        return this;
    }
    public RequestDispatcherImpl addPut(String path, RequestHandler handler) {
        addHandler(path, handler, HttpMethod.PUT);
        return this;
    }
    public RequestDispatcherImpl addPost(String path, RequestHandler handler) {
        addHandler(path, handler, HttpMethod.POST);
        return this;
    }

}
