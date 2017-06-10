package com.romanb.expresslike;

import com.romanb.net.*;

public class ExpresslikeApp implements ExpresslikeRequestHandler, Server {

    private ExpresslikeRequestHandler expresslikeRequestHandler;
    private Server server;
    private TemplateEngine templateEngine;

    public ExpresslikeApp() {
    }

    public ExpresslikeApp(ExpresslikeRequestHandler expresslikeRequestHandler, Server server) {
        this.expresslikeRequestHandler = expresslikeRequestHandler;
        this.server = server;
    }

    public static ExpresslikeApp listen(int port, ExpresslikeRequestHandler expresslikeRequestHandler) {
        return new ExpresslikeApp(expresslikeRequestHandler, new HttpServer(port, expresslikeRequestHandler));
    }

    @Override
    public void start() {
        templateEngine = new ExpresslikeTemplateEngine();
        server.start();
    }

    @Override
    public void close() throws Exception {
        server.close();
    }

    @Override
    public void handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
        expresslikeRequestHandler.handleRequest(httpRequest, httpResponse);
    }

    @Override
    public void addHandler(String path, RequestHandler handler, HttpMethod httpMethod) {
        expresslikeRequestHandler.addHandler(path, handler, httpMethod);
    }

    @Override
    public ExpresslikeRequestHandler addGet(String path, RequestHandler handler) {
        return expresslikeRequestHandler.addGet(path, handler);
    }

    @Override
    public ExpresslikeRequestHandler addDelete(String path, RequestHandler handler) {
        return expresslikeRequestHandler.addDelete(path, handler);
    }

    @Override
    public ExpresslikeRequestHandler addPut(String path, RequestHandler handler) {
        return expresslikeRequestHandler.addPut(path, handler);
    }

    @Override
    public ExpresslikeRequestHandler addPost(String path, RequestHandler handler) {
        return expresslikeRequestHandler.addPost(path, handler);
    }

    @Override
    public ExpresslikeRequestHandler addStatic(String path) {
        return expresslikeRequestHandler.addStatic(path);
    }

    @Override
    public ExpresslikeRequestHandler addElse(RequestHandler handler) {
        return expresslikeRequestHandler.addElse(handler);
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
