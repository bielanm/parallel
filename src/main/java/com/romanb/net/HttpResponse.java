package com.romanb.net;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import static com.romanb.net.HttpStatus.*;

public class HttpResponse {

    private final PrintWriter writer;
    private HttpStatus status;
    private String httpProtocol;
    private Map<String, String> headers;
    private String body;

    public HttpResponse(PrintWriter writer) {
        this.writer = writer;
    }

    public void init(HttpRequest request) {
        httpProtocol = request.getProtocol();
        headers = new HashMap<>();
        headers.putAll(request.getHeaders().entrySet().stream()
                .filter((e) -> e.getKey().equalsIgnoreCase("cache-control") ||
                               e.getKey().equalsIgnoreCase("connection"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public void addHeaders(String key, String value) {
        headers.put(key, value);
    }

    public void sendHtml(String body) {
        addHeaders("Content-Type", "text/html; charset=utf-8");
        sendBody(body);
    }

    public void sendBody(String body) {
        addHeaders("Content-Length", String.valueOf(body.length()));
        this.body = body;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void end() {
        StringBuilder response = new StringBuilder();
        status = Objects.isNull(status) ? OK : status;
        response.append(httpProtocol + " " + status.code + " " + status.toString()).append("\r\n");
        headers.forEach((k, v) -> response.append(String.join(": ", k, v)).append("\r\n"));
        response.append("\r\n");
        Optional.ofNullable(body).ifPresent(body -> response.append(body));
        System.out.println("Response:\n" + response.toString());
        writer.write(response.toString());
        writer.flush();
    }

    public void end(String body) {
        sendBody(body);
        end();
    }

    public void redirect(String path) {
        StringBuilder response = new StringBuilder();
        response.append(httpProtocol + " " + FOUND.code + " " + FOUND.toString()).append("\r\n");
        response.append("Location: " + path).append("\n");
        response.append("\r\n");
        writer.write(response.toString());
        writer.flush();
    }
}
