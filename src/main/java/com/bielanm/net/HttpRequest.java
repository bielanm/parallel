package com.bielanm.net;

import java.nio.file.Path;

public class HttpRequest {

    private HttpMethod method;
    private Path path;

    public HttpRequest(HttpMethod method, Path path) {
        this.method = method;
        this.path = path;
    }
}
