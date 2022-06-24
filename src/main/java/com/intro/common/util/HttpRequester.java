package com.intro.common.util;


import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Custom http requesting lib, so I don't have to include the entirety of apache http client in the mod
 */
public class HttpRequester {

    public static HttpResponse fetch(HttpRequest request) throws IOException {
        String formattedUrl = request.url();
        if(!request.queryParams().isEmpty()) {
            formattedUrl += "?";
            formattedUrl += getParamsString(request.queryParams());
        }


        HttpURLConnection connection = (HttpURLConnection) new URL(formattedUrl).openConnection();
        connection.setRequestMethod(request.method());
        connection.setRequestProperty("User-Agent", "JFetch/1.0");

        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        connection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String strCurrentLine;
        while ((strCurrentLine = br.readLine()) != null) {
            response.append(strCurrentLine);
        }
        return new HttpResponse(connection.getResponseCode(), response.toString());
    }

    public static HttpResponse fetchWithJson(HttpRequest request, String json) throws IOException {
        String formattedUrl = request.url();
        if(!request.queryParams().isEmpty()) {
            formattedUrl += "?";
            formattedUrl += getParamsString(request.queryParams());
        }


        HttpURLConnection connection = (HttpURLConnection) new URL(formattedUrl).openConnection();
        connection.setRequestMethod(request.method());
        connection.setRequestProperty("User-Agent", "JFetch/1.0");

        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        connection.setDoOutput(true);
        connection.connect();

        connection.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        connection.getOutputStream().flush();

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
        connection.setRequestProperty("User-Agent", "JFetch/1.0");
        connection.setRequestMethod(request.method());
        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        connection.connect();

        InputStream stream = connection.getInputStream();

        byte[] bytes = stream.readAllBytes();
        System.out.println(Arrays.toString(bytes));
        // can't use ByteBuffer.wrap() because it makes the buffer have a funky pointer that weirds out NativeImage.read()
        return new BinaryHttpResponse(connection.getResponseCode(), ByteBuffer.allocateDirect(bytes.length).put(bytes));
    }


   public static HttpResponse uploadFile(HttpRequest request, ByteBuffer fileBuffer, String fileName) throws IOException {
       return uploadFileWithOtherFormData(request, fileBuffer, fileName, ImmutableMap.of());
   }


    /**
     * @param request Http request
     * @param fileBuffer Data of file to upload
     * @param fileName Name of the file to upload
     * @param otherTextFormData A mutable map of other form data
     * @return Server response
     */
    public static HttpResponse uploadFileWithOtherFormData(HttpRequest request, ByteBuffer fileBuffer, String fileName, Map<String, String> otherTextFormData) throws IOException {
        String formattedUrl = request.url();
        if(!request.queryParams().isEmpty()) {
            formattedUrl += "?";
            formattedUrl += getParamsString(request.queryParams());
        }


        HttpURLConnection connection = (HttpURLConnection) new URL(formattedUrl).openConnection();
        connection.setRequestMethod(request.method());

        connection.setRequestProperty("User-Agent", "JFetch/1.0");

        MultiPartRequestBuilder builder = new MultiPartRequestBuilder();

        builder.addFileSection(fileName, fileBuffer);

        for(String key : otherTextFormData.keySet()) {
            builder.addTextSection(key, otherTextFormData.get(key));
        }

        byte[] multipartOutput = ArrayUtils.toPrimitive(builder.build(request.headers));

        for(Map.Entry<String, String> entry : request.headers().entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }


        connection.setDoOutput(true);

        connection.connect();

        OutputStream outputStream = connection.getOutputStream();

        outputStream.write(multipartOutput);
        outputStream.flush();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String strCurrentLine;
        while ((strCurrentLine = br.readLine()) != null) {
            response.append(strCurrentLine);
        }
        return new HttpResponse(connection.getResponseCode(), response.toString());
    }


    private static class MultiPartRequestBuilder {

        private final HashSet<BinaryFormSection> fileFormSections = new HashSet<>();

        private final HashSet<TextFormSection> textFormSections = new HashSet<>();

        public void addFileSection(String fileName, ByteBuffer data) {
            fileFormSections.add(new BinaryFormSection(fileName, data));
        }

        public void addTextSection(String name, String data) {
            textFormSections.add(new TextFormSection(name, data));
        }

        public Byte[] build(Map<String, String> headersToAppendTo) throws IOException {
            String boundary = System.currentTimeMillis() + "--";
            headersToAppendTo.put("Content-Type", "multipart/form-data; boundary=" + boundary);

            ArrayList<Byte> outputData = new ArrayList<>();

            addMultipartLine("--" + boundary, outputData);

            for(TextFormSection section : textFormSections) {
                addMultipartLine("Content-Disposition: form-data; name=\"" + section.name + "\"", outputData);
                addMultipartLine("Content-Type: text/plain; charset=utf-8", outputData);
                addMultipartLine("", outputData);
                addMultipartLine(section.data, outputData);
                addMultipartLine("--" + boundary, outputData);
            }

            int fileCount = 0;

            for(BinaryFormSection section : fileFormSections) {
                addMultipartLine("Content-Disposition: form-data; name=\"" + "file" + fileCount + "\"; filename=\"" + section.fileName + "\"", outputData);
                addMultipartLine("Content-Type: " + Files.probeContentType(new File(section.fileName).toPath()), outputData);
                addMultipartLine("", outputData);
                addBinaryMultipartLine(section.data, outputData);
                addMultipartLine("", outputData);
                addMultipartLine("--" + boundary, outputData);
            }

            return outputData.toArray(new Byte[0]);
        }

        private void addMultipartLine(String line, List<Byte> existingData) {
            Byte[] lineBytes = ArrayUtils.toObject(line.getBytes(StandardCharsets.UTF_8));
            existingData.addAll(List.of((lineBytes)));
            existingData.addAll(List.of(ArrayUtils.toObject("\r\n".getBytes())));
        }

        private void addBinaryMultipartLine(ByteBuffer data, List<Byte> existingData) {
            existingData.addAll(List.of(ArrayUtils.toObject(data.array())));
            existingData.addAll(List.of(ArrayUtils.toObject("\r\n".getBytes())));
        }

    }

    private record BinaryFormSection(String fileName, ByteBuffer data) {}

    private record TextFormSection(String name, String data) {}


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

    public record HttpRequest(String url, String method, Map<String, String> headers, Map<String, String> queryParams) {}

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
