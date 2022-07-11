package com.intro.common.util.http;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HttpRequestBuilder {

    private String method = "GET";
    private final HashMap<String, String> parameters = new HashMap<>();
    private final HashMap<String, String> headers = new HashMap<>();
    private String url = "";
    private ByteBuffer requestData;

    public HttpRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public HttpRequestBuilder requestBody(String body) {
        requestData = ByteBuffer.wrap(body.getBytes(StandardCharsets.UTF_8));
        return this;
    }

    public HttpRequestBuilder method(String method) {
        this.method = method;
        return this;
    }

    public HttpRequestBuilder parameter(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public HttpRequestBuilder parameters(HashMap<String, String> p) {
        parameters.putAll(p);
        return this;
    }

    public HttpRequestBuilder header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpRequestBuilder headers(HashMap<String, String> h) {
        headers.putAll(h);
        return this;
    }

    public HttpRequester.CustomizableHttpRequest build() {
        return new HttpRequester.CustomizableHttpRequest(url, method, headers, parameters, requestData);
    }
}
