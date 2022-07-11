package com.intro.common.util.http;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MultiPartRequestBuilder {

    private String method = "POST";
    private final HashMap<String, String> parameters = new HashMap<>();
    private final HashMap<String, String> headers = new HashMap<>();
    private String url = "";

    private final HashSet<BinaryFormSection> fileFormSections = new HashSet<>();

    private final HashSet<TextFormSection> textFormSections = new HashSet<>();

    public MultiPartRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public MultiPartRequestBuilder method(String method) {
        this.method = method;
        return this;
    }

    public MultiPartRequestBuilder parameter(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public MultiPartRequestBuilder parameters(HashMap<String, String> p) {
        parameters.putAll(p);
        return this;
    }

    public MultiPartRequestBuilder header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public MultiPartRequestBuilder headers(HashMap<String, String> h) {
        headers.putAll(h);
        return this;
    }


    public MultiPartRequestBuilder addFileSection(String fileName, ByteBuffer data) {
        fileFormSections.add(new BinaryFormSection(fileName, data));
        return this;
    }

    public MultiPartRequestBuilder addTextSection(String name, String data) {
        textFormSections.add(new TextFormSection(name, data));
        return this;
    }

    public HttpRequester.CustomizableHttpRequest build() throws IOException {
        String boundary = System.currentTimeMillis() + "--";

        ArrayList<Byte> outputData = new ArrayList<>();

        addMultipartLine("--" + boundary, outputData);

        for (TextFormSection section : textFormSections) {
            addMultipartLine("Content-Disposition: form-data; name=\"" + section.name + "\"", outputData);
            addMultipartLine("Content-Type: text/plain; charset=utf-8", outputData);
            addMultipartLine("", outputData);
            addMultipartLine(section.data, outputData);
            addMultipartLine("--" + boundary, outputData);
        }

        int fileCount = 0;

        for (BinaryFormSection section : fileFormSections) {
            addMultipartLine("Content-Disposition: form-data; name=\"" + "file" + fileCount + "\"; filename=\"" + section.fileName + "\"", outputData);
            addMultipartLine("Content-Type: " + Files.probeContentType(new File(section.fileName).toPath()), outputData);
            addMultipartLine("", outputData);
            addBinaryMultipartLine(section.data, outputData);
            addMultipartLine("", outputData);
            addMultipartLine("--" + boundary, outputData);
        }
        ByteBuffer buffer = ByteBuffer.allocate(outputData.size());
        buffer.put(ArrayUtils.toPrimitive(outputData.toArray(new Byte[0])));
        header("Content-Type", "multipart/form-data; boundary=" + boundary);
        return new HttpRequester.CustomizableHttpRequest(url, method, headers, parameters, buffer);
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

    private record BinaryFormSection(String fileName, ByteBuffer data) {}

    private record TextFormSection(String name, String data) {}

}
