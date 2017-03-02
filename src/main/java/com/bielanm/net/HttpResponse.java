package com.bielanm.net;

import java.io.PrintWriter;

public class HttpResponse {

    private final PrintWriter writer;

    public HttpResponse(PrintWriter writer) {
        this.writer = writer;
    }

    public void send(Object body) {
        writer.write(body.toString());
    }

    public void end() {
        writer.flush();
        writer.close();
    }
}
