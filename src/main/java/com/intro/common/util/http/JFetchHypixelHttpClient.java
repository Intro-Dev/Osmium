package com.intro.common.util.http;

import net.hypixel.api.http.HypixelHttpClient;
import net.hypixel.api.http.HypixelHttpResponse;
import net.minecraft.Util;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record JFetchHypixelHttpClient(UUID apiKey) implements HypixelHttpClient {

    @Override
    public CompletableFuture<HypixelHttpResponse> makeRequest(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse response = HttpRequester.fetch(new HttpRequestBuilder()
                        .method("GET")
                        .url(url)
                        .build());
                return new HypixelHttpResponse(response.getStatusCode(), response.getAsString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to make hypixel get request: ", e);
            }
        }, Util.ioPool());
    }

    @Override
    public CompletableFuture<HypixelHttpResponse> makeAuthenticatedRequest(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse response = HttpRequester.fetch(new HttpRequestBuilder()
                        .method("GET")
                        .url(url)
                        .header("API-Key", this.apiKey.toString())
                        .build());
                return new HypixelHttpResponse(response.getStatusCode(), response.getAsString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to make hypixel get request: ", e);
            }
        }, Util.ioPool());
    }

    @Override
    public void shutdown() {

    }
}
