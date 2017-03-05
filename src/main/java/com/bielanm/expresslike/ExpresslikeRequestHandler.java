package com.bielanm.expresslike;

import com.bielanm.net.*;
import com.bielanm.net.exceptions.HttpExeption;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface ExpresslikeRequestHandler extends RequestHandler {

    public void addHandler(String path, RequestHandler handler, HttpMethod httpMethod);
    public ExpresslikeRequestHandler addGet(String path, RequestHandler handler);
    public ExpresslikeRequestHandler addDelete(String path, RequestHandler handler);
    public ExpresslikeRequestHandler addPut(String path, RequestHandler handler);
    public ExpresslikeRequestHandler addPost(String path, RequestHandler handler);
    public ExpresslikeRequestHandler addStatic(String path);
    public ExpresslikeRequestHandler addElse(RequestHandler handler);

}
