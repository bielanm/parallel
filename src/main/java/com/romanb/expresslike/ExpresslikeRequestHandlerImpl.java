package com.romanb.expresslike;

import com.romanb.net.*;
import com.romanb.net.exceptions.HttpExeption;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class ExpresslikeRequestHandlerImpl implements ExpresslikeRequestHandler {

    private Map<String, Map<HttpMethod, RequestHandler>> handlerMapper;
    private RequestHandler staticHandler;
    private RequestHandler alternative;

    public ExpresslikeRequestHandlerImpl() {
        this.handlerMapper = new HashMap<>();
    }

    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
        if(httpRequest.getUrl().getPath().matches(".*\\.\\w*$")) {
            staticHandler.handleRequest(httpRequest, httpResponse);
        } else {
            try {
                ofNullable(ofNullable(ofNullable(handlerMapper.get(httpRequest.getUrl().getPath()))
                        .orElseGet(() -> Collections.emptyMap()).get(httpRequest.getMethod()))
                        .orElseGet(() -> alternative))
                        .orElseThrow(() -> new HttpExeption(HttpStatus.NOT_FOUND))
                        .handleRequest(httpRequest, httpResponse);
            } catch (HttpExeption httpExeption) {
                    httpResponse.setStatus(httpExeption.getHttpStatus());
                    httpResponse.end();
            }
        }
    }

    @Override
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

    @Override
    public ExpresslikeRequestHandler addGet(String path, RequestHandler handler) {
        addHandler(path, handler, HttpMethod.GET);
        return this;
    }

    @Override
    public ExpresslikeRequestHandler addDelete(String path, RequestHandler handler) {
        addHandler(path, handler, HttpMethod.DELETE);
        return this;
    }

    @Override
    public ExpresslikeRequestHandler addPut(String path, RequestHandler handler) {
        addHandler(path, handler, HttpMethod.PUT);
        return this;
    }

    @Override
    public ExpresslikeRequestHandler addPost(String path, RequestHandler handler) {
        addHandler(path, handler, HttpMethod.POST);
        return this;
    }

    @Override
    public ExpresslikeRequestHandler addStatic(String path) {
        staticHandler = (httpRequest, httpResponse) -> {
            try {
                String body = Files.newBufferedReader(Paths.get("src/main/resources/" + path + httpRequest.getUrl().getPath()))
                        .lines()
                        .collect(Collectors.joining());
                httpResponse.sendBody(body);
            } catch (FileNotFoundException e) {
                httpResponse.setStatus(HttpStatus.NOT_FOUND);
            } catch (IOException e) {
                httpResponse.setStatus(HttpStatus.BAD_REQUEST);
            }
            httpResponse.end();
        };
        return this;
    }

    @Override
    public ExpresslikeRequestHandler addElse(RequestHandler handler) {
        alternative = handler;
        return this;
    }
}