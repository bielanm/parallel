package com.bielanm.net;

import com.bielanm.cuncurency.PoolExecutor;
import com.bielanm.net.exceptions.HttpFormatException;

import java.io.*;

import static java.util.Objects.nonNull;

public class HttpConnectionHandler implements ConnectionHandler {

    private final RequestHandler requestHandler;
    private final PoolExecutor poolExecutor;

    public HttpConnectionHandler(RequestHandler requestHandler, PoolExecutor poolExecutor) {
        this.requestHandler = requestHandler;
        this.poolExecutor = poolExecutor;
    }


    @Override
    public void handle(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter printWriter = new PrintWriter(outputStream);

        StringBuilder request = new StringBuilder();

        String line = null;
        while (nonNull(line = bufferedReader.readLine())) {
            request.append(line).append("\n");
        }

        try {
            final HttpRequest httpRequest = HttpInterfacesFactory.createHttpRequest(request.toString());
            final HttpResponse httpResponse = HttpInterfacesFactory.createHttpResponse(printWriter, httpRequest);
            poolExecutor.submit(() -> requestHandler.handleRequest(httpRequest, httpResponse));
        } catch (HttpFormatException exc) {
            printWriter.close();
        }
    }

    @Override
    public void shutDown() {
    }
}
