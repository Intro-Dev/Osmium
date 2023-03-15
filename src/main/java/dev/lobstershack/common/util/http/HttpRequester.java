package dev.lobstershack.common.util.http;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for apache http client so I don't go completely insane
 */
public class HttpRequester {

    private final static CloseableHttpClient instance = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();


    public static HttpResponse fetch(CustomizableHttpRequest request) throws IOException {
        HttpRequestBase base = request.getApacheHttpRequest();
        HttpEntity requestEntity = request.getEntity();
        if(base instanceof HttpEntityEnclosingRequestBase requestBase) {
            requestBase.setEntity(requestEntity);
        }
        CloseableHttpResponse response = instance.execute(base);
        byte[] bytes = response.getEntity().getContent().readAllBytes();
        ByteBuffer responseBuffer = ByteBuffer.wrap(bytes);
        HashMap<String, String> responseHeaders = new HashMap<>();
        for(Header header : response.getAllHeaders()) {
            responseHeaders.put(header.getName(), header.getValue());
        }
        int statusCode = response.getStatusLine().getStatusCode();
        EntityUtils.consume(response.getEntity());
        EntityUtils.consume(requestEntity);
        return new HttpResponse(statusCode, responseBuffer, responseHeaders);
    }


    /**
     * credit to https://www.baeldung.com/java-http-request
     */
    private static String getParamsString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    protected record CustomizableHttpRequest(String url, String method,
                                             Map<String, String> headers,
                                             Map<String, String> queryParams,
                                             ByteBuffer requestContent) {

        public String getFormattedUrl() {
            String formattedUrl = url;
            if (!queryParams.isEmpty()) {
                formattedUrl += "?";
                formattedUrl += getParamsString(queryParams);
            }
            return formattedUrl;
        }

        public HttpRequestBase getApacheHttpRequest() {
            String formattedUrl = getFormattedUrl();
            HttpRequestBase httpRequest = switch (method) {
                case "GET" -> new HttpGet(formattedUrl);
                case "POST" -> new HttpPost(formattedUrl);
                case "PUT" -> new HttpPut(formattedUrl);
                default -> throw new IllegalStateException("Invalid method supplied: " + method);
            };
            for (Map.Entry<String, String> h : headers.entrySet()) {
                httpRequest.setHeader(h.getKey(), h.getValue());
            }
            return httpRequest;
        }

        public HttpEntity getEntity() {
            return new ByteArrayEntity(requestContent.array());
        }
    }

}
