package com.romanb.net;

import com.romanb.net.exceptions.HttpFormatException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
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

            Map<String, String> headers = Arrays.asList(request.split("\r\n"))
                    .stream()
                    .filter(line -> line.matches(".*: .*"))
                    .collect(Collectors.toMap(line -> line.replaceFirst(":.*", "").trim(), line -> line.replaceFirst("[^:]*:", "").trim()));

            URL url = new URL("http://" + headers.get("host") + methodUri.replaceFirst(method, "").replaceFirst("HTTP.*", "").trim());
            matcher = Pattern.compile("HTTP.*\r", Pattern.CASE_INSENSITIVE)
                    .matcher(methodUri);
            matcher.find();
            String protocol = matcher.group().trim();

            Map<String, String> map = Collections.EMPTY_MAP;
            if(method.equals("POST") || method.equals("PUT")) {
                String body = request.replaceAll("[.\\pS\\pP[^\\{]]*\\{", "{").replaceAll("\\}[.\\pS\\pP[^\\{]]*", "}");;
                if(isValidJSON(body)) {
                    map = jsonasMap(body);
                }
            }

            return HttpRequest.newBuilder()
                    .withMethod(HttpMethod.valueOf(method.toUpperCase()))
                    .withUrl(url)
                    .withProtocol(protocol)
                    .withHeaders(headers)
                    .withBody(map)
                    .build();
        } catch (Exception e) {
            throw new HttpFormatException();
        }
    }


    private static Map<String, String> jsonasMap(String json) {
        Map<String,String> map = new HashMap<String,String>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, HashMap.class);
        } catch (IOException e) {
            return Collections.EMPTY_MAP;

        }
    }

    private static boolean isValidJSON(String value) {
        try{
            new ObjectMapper().readTree(value);
            return true;
        } catch(java.io.IOException e){
            return false;
        }
    }

    public static HttpResponse createHttpResponse(PrintWriter writer, HttpRequest httpRequest) {
        HttpResponse response = new HttpResponse(writer);
        response.init(httpRequest);
        return response;
    }
}
