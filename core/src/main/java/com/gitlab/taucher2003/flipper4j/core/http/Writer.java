package com.gitlab.taucher2003.flipper4j.core.http;

import com.gitlab.taucher2003.flipper4j.core.config.FlipperConfiguration;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class Writer {

    private final FlipperConfiguration configuration;
    private final Reader reader;

    public Writer(FlipperConfiguration configuration, Reader reader) {
        this.configuration = configuration;
        this.reader = reader;
    }

    public CompletableFuture<Void> execute(String route, Method method) {
        return execute(route, method, HttpRequest.BodyPublishers.noBody());
    }

    public CompletableFuture<Void> execute(String route, Method method, HttpRequest.BodyPublisher body) {
        var request = method.mapper.apply(HttpRequest.newBuilder(), body)
                .uri(URI.create(configuration.getBaseUrl() + route))
                .header("Content-Type", "application/json")
                .build();

        return configuration.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(ignored -> reader.updateLocalState());
    }

    public enum Method {
        POST((request, body) -> request.POST(body)),
        DELETE((request, body) -> request.method("DELETE", body));

        private final BiFunction<HttpRequest.Builder, HttpRequest.BodyPublisher, HttpRequest.Builder> mapper;

        Method(BiFunction<HttpRequest.Builder, HttpRequest.BodyPublisher, HttpRequest.Builder> mapper) {
            this.mapper = mapper;
        }
    }
}
