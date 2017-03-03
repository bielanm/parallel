package com.bielanm.net;

import java.net.URL;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final Map<String, String> headers;
    private final URL url;
    private final String protocol;

    public HttpRequest(HttpMethod method, URL url, String protocol, Map<String, String> headers) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.headers = headers;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public URL getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public static class Builder {

        private HttpMethod method;
        private URL url;
        private Map<String, String> headers;
        private String protocol;

        public Builder withMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder withUrl(URL url) {
            this.url = url;
            return this;
        }

        public Builder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder withProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(method, url, protocol, headers);
        }
    }
}
