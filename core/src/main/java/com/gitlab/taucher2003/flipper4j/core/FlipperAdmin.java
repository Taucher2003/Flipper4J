package com.gitlab.taucher2003.flipper4j.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gitlab.taucher2003.flipper4j.core.config.FlipperConfiguration;
import com.gitlab.taucher2003.flipper4j.core.http.Writer;
import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;

import java.net.http.HttpRequest;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;

public class FlipperAdmin {

    private final FlipperConfiguration configuration;
    private final Writer writer;

    FlipperAdmin(FlipperConfiguration configuration, Writer writer) {
        this.configuration = configuration;
        this.writer = writer;
    }

    public CompletableFuture<Void> createFeature(String name) {
        return writer.execute("/features", Writer.Method.POST, publisher(Map.of("name", name)));
    }

    public CompletableFuture<Void> deleteFeature(String name) {
        return writer.execute(format("/features/%s", name), Writer.Method.DELETE);
    }

    public CompletableFuture<Void> clearFeature(String name) {
        return writer.execute(format("/features/%s/clear", name), Writer.Method.DELETE);
    }

    public CompletableFuture<Void> enableFeatureBoolean(String name) {
        return writer.execute(format("/features/%s/boolean", name), Writer.Method.POST);
    }

    public CompletableFuture<Void> disableFeatureBoolean(String name) {
        return writer.execute(format("/features/%s/boolean", name), Writer.Method.DELETE);
    }

    public CompletableFuture<Void> enableFeatureActor(String name, FlipperIdentifier identifier) {
        return writer.execute(format("/features/%s/actors", name), Writer.Method.POST, publisher(Map.of("flipper_id", identifier.flipperId())));
    }

    public CompletableFuture<Void> disableFeatureActor(String name, FlipperIdentifier identifier) {
        return writer.execute(format("/features/%s/actors", name), Writer.Method.DELETE, publisher(Map.of("flipper_id", identifier.flipperId())));
    }

    public CompletableFuture<Void> enableFeatureActorPercentage(String name, int percentage) {
        return writer.execute(format("/features/%s/percentage_of_actors", name), Writer.Method.POST, publisher(Map.of("percentage", percentage)));
    }

    public CompletableFuture<Void> disableFeatureActorPercentage(String name) {
        return writer.execute(format("/features/%s/percentage_of_actors", name), Writer.Method.DELETE);
    }

    public CompletableFuture<Void> enableFeatureTimePercentage(String name, int percentage) {
        return writer.execute(format("/features/%s/percentage_of_time", name), Writer.Method.POST, publisher(Map.of("percentage", percentage)));
    }

    public CompletableFuture<Void> disableFeatureTimePercentage(String name) {
        return writer.execute(format("/features/%s/percentage_of_time", name), Writer.Method.DELETE);
    }

    private HttpRequest.BodyPublisher publisher(Map<String, Object> values) {
        try {
            return HttpRequest.BodyPublishers.ofString(configuration.getObjectMapper().writeValueAsString(values));
        } catch (JsonProcessingException e) {
            throw new JsonMappingException(e);
        }
    }

    public static final class JsonMappingException extends RuntimeException {
        private JsonMappingException(Throwable cause) {
            super(cause);
        }
    }
}
