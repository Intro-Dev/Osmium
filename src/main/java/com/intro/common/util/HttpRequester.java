package com.intro.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class HttpRequester {

    public static HttpResponse fetch(HttpRequest request) throws IOException {
        String formattedUrl = request.url();
        if(!request.queryParams().isEmpty()) {
            formattedUrl += "?";
            formattedUrl += getParamsString(request.queryParams());
        }


        HttpURLConnection connection = (HttpURLConnection) new URL(formattedUrl).openConnection();
        connection.setRequestMethod(request.method());
        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setRequestProperty("User-Agent", "JFetch/1.0");

        connection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String strCurrentLine;
        while ((strCurrentLine = br.readLine()) != null) {
            response.append(strCurrentLine);
        }
        return new HttpResponse(connection.getResponseCode(), response.toString());
    }

    public static BinaryHttpResponse fetchBin(HttpRequest request) throws IOException {
        String formattedUrl = request.url();
        if(!request.queryParams().isEmpty()) {
            formattedUrl += "?";
            formattedUrl += getParamsString(request.queryParams());
        }


        HttpURLConnection connection = (HttpURLConnection) new URL(formattedUrl).openConnection();
        connection.setRequestMethod(request.method());
        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setRequestProperty("User-Agent", "JFetch/1.0");

        connection.connect();

        InputStream stream = connection.getInputStream();

        ByteBuffer byteBuffer = ByteBuffer.allocate(stream.available());
        while (stream.available() > 0) {
            byteBuffer.put((byte) stream.read());
        }


        return new BinaryHttpResponse(connection.getResponseCode(), byteBuffer);
    }


   public static HttpResponse uploadFile(HttpRequest request, ShortBuffer fileBuffer, String fileName) throws IOException {
       String formattedUrl = request.url();
       if(!request.queryParams().isEmpty()) {
           formattedUrl += "?";
           formattedUrl += getParamsString(request.queryParams());
       }
       RequestBuilder builder = RequestBuilder.create(request.method()).setUri(formattedUrl);
       for(Map.Entry<String, String> entry : request.headers().entrySet()) {
           builder.addHeader(entry.getKey(), entry.getValue());
       }
       builder.addHeader("User-Agent", "JFetch/1.0");

       CloseableHttpClient httpClient = HttpClients.createDefault();
       MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
       entityBuilder.setMode(HttpMultipartMode.STRICT);
       entityBuilder.addBinaryBody(fileName, new ShortBufferBackedInputStream(fileBuffer), ContentType.IMAGE_PNG, fileName);
       HttpEntity entity = entityBuilder.build();
       builder.setEntity(entity);
       HttpUriRequest multipartRequest = builder.build();
       CloseableHttpResponse response = httpClient.execute(multipartRequest);
       return new HttpResponse(response.getStatusLine().getStatusCode(), new String(response.getEntity().getContent().readAllBytes()));
   }

    public static HttpResponse uploadFileWithOtherFormData(HttpRequest request, ByteBuffer fileBuffer, String fileName, Map<String, String> otherTextFormData) throws IOException {
        String formattedUrl = request.url();
        if(!request.queryParams().isEmpty()) {
            formattedUrl += "?";
            formattedUrl += getParamsString(request.queryParams());
        }
        RequestBuilder builder = RequestBuilder.create(request.method()).setUri(formattedUrl);
        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        builder.addHeader("User-Agent", "JFetch/1.0");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.STRICT);
        entityBuilder.addBinaryBody(fileName, fileBuffer.array(), ContentType.IMAGE_PNG, fileName);
        for(Map.Entry<String, String> entry : otherTextFormData.entrySet()) {
            entityBuilder.addTextBody(entry.getKey(), entry.getValue());
        }


        HttpEntity entity = entityBuilder.build();
        builder.setEntity(entity);
        HttpUriRequest multipartRequest = builder.build();
        System.out.println(new String(entity.getContent().readAllBytes()));
        CloseableHttpResponse response = httpClient.execute(multipartRequest);
        return new HttpResponse(response.getStatusLine().getStatusCode(), new String(response.getEntity().getContent().readAllBytes()));
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

    public static class HttpRequest {
        private String method;
        private String url;
        private Map<String, String> headers;
        private Map<String, String> queryParams;

        public HttpRequest(String url, String method, Map<String, String> headers, Map<String, String> queryParams) {
            this.method = method;
            this.url = url;
            this.headers = headers;
            this.queryParams = queryParams;
        }

        public String method() {
            return method;
        }

        public String url() {
            return url;
        }

        public Map<String, String> headers() {
            return headers;
        }

        public Map<String, String> queryParams() {
            return queryParams;
        }
    }

    public static record HttpResponse(int code, String body) {
        @Override
        public String toString() {
            return "HttpResponse{" +
                    "code=" + code +
                    ", body='" + body + '\'' +
                    '}';
        }
    }

    public static record BinaryHttpResponse(int code, ByteBuffer body) {
        @Override
        public String toString() {
            return "HttpResponse{" +
                    "code=" + code +
                    ", body='" + Arrays.toString(body.array()) + '\'' +
                    '}';
        }
    }
}
