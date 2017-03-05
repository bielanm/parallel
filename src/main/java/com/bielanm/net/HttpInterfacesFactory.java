package com.bielanm.net;

import com.bielanm.net.exceptions.HttpFormatException;

import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class HttpInterfacesFactory {

    public static HttpRequest createHttpRequest(String request) throws HttpFormatException {
        try {
            Matcher matcher = Pattern.compile("[GET|HEAD|POST|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH]{1}.*HTTP.*\r", Pattern.CASE_INSENSITIVE)
                    .matcher(request);
            String methodUri = Optional.ofNullable(matcher.find() ? matcher.group() : null)
                    .orElseThrow(() -> new IllegalArgumentException("Not http request"));
            matcher = Pattern.compile("GET|HEAD|POST|PUT|DELETE|CONNECT|OPTIONS|TRACE|PATCH", Pattern.CASE_INSENSITIVE)
                    .matcher(methodUri);
            matcher.find();
            String method = matcher.group();

            Map<String, String> headers = Arrays.asList(request.split("\r\n")).stream()
                    .filter(line -> line.matches(".*: .*"))
                    .collect(Collectors.toMap(line -> line.replaceFirst(":.*", "").trim(), line -> line.replaceFirst("[^:]*:", "").trim()));

            URL url = new URL("http://" + headers.get("host") + methodUri.replaceFirst(method, "").replaceFirst("HTTP.*", "").trim());
            matcher = Pattern.compile("HTTP.*\r", Pattern.CASE_INSENSITIVE)
                    .matcher(methodUri);
            matcher.find();
            String protocol = matcher.group().trim();

            return HttpRequest.newBuilder()
                    .withMethod(HttpMethod.valueOf(method.toUpperCase()))
                    .withUrl(url)
                    .withProtocol(protocol)
                    .withHeaders(headers)
                    .build();
        } catch (Exception e) {
            throw new HttpFormatException();
        }
    }


    public static HttpResponse createHttpResponse(PrintWriter writer, HttpRequest httpRequest) {
        HttpResponse response = new HttpResponse(writer);
        response.init(httpRequest);
        return response;
    }
}
