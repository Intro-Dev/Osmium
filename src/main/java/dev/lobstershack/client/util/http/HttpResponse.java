package dev.lobstershack.client.util.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class HttpResponse {
    private final int statusCode;
    private final HashMap<String, String> responseHeaders;
    private final ByteBuffer binaryContent;

    public HttpResponse(int statusCode, ByteBuffer binaryContent, HashMap<String, String> responseHeaders) {
        this.statusCode = statusCode;
        this.binaryContent = binaryContent;
        this.responseHeaders = responseHeaders;
    }

    public JsonElement getAsJson() {
        return JsonParser.parseString(getAsString());
    }

    public ByteBuffer getAsBinary() {
        return binaryContent;
    }

    public String getAsString() {
        return new String(binaryContent.array());
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HashMap<String, String> getResponseHeaders() {
        return responseHeaders;
    }
}
