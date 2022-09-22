package com.gitlab.taucher2003.flipper4j.adapter.http.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.taucher2003.flipper4j.adapter.http.FlipperHttpAdapter;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FlipperConfigurator {

    private long fetchInterval = 15;
    private TimeUnit fetchIntervalUnit = TimeUnit.SECONDS;
    private ObjectMapper objectMapper;
    private String baseUrl;
    private final HttpClient.Builder httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.of(1, ChronoUnit.MINUTES));

    public FlipperConfigurator setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public FlipperConfigurator configureObjectMapper(Consumer<ObjectMapper> configurator) {
        setObjectMapper(new ObjectMapper());
        configurator.accept(objectMapper);
        return this;
    }

    public FlipperConfigurator setFetchInterval(long fetchInterval) {
        this.fetchInterval = fetchInterval;
        return this;
    }

    public FlipperConfigurator setFetchIntervalUnit(TimeUnit fetchIntervalUnit) {
        this.fetchIntervalUnit = fetchIntervalUnit;
        return this;
    }

    public FlipperConfigurator setBaseUrl(String baseUrl) {
        URI.create(baseUrl); // only validate
        this.baseUrl = baseUrl;
        return this;
    }

    public FlipperConfigurator configureHttpClient(Consumer<HttpClient.Builder> configurator) {
        configurator.accept(httpClient);
        return this;
    }

    public FlipperHttpAdapter build() {
        if(baseUrl == null) {
            throw new IllegalArgumentException("Configuration can't be built without base url");
        }
        if(objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

        var config = new FlipperConfiguration(
                fetchInterval,
                fetchIntervalUnit,
                objectMapper,
                baseUrl,
                httpClient.build()
        );

        return new FlipperHttpAdapter(config);
    }
}
