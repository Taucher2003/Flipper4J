package com.gitlab.taucher2003.flipper4j.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;

public class FlipperConfiguration {
    private final long fetchInterval;
    private final TimeUnit fetchIntervalUnit;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final HttpClient httpClient;

    FlipperConfiguration(
            long fetchInterval,
            TimeUnit fetchIntervalUnit,
            ObjectMapper objectMapper,
            String baseUrl,
            HttpClient httpClient
    ) {
        this.fetchInterval = fetchInterval;
        this.fetchIntervalUnit = fetchIntervalUnit;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
    }

    public long getFetchInterval() {
        return fetchInterval;
    }

    public TimeUnit getFetchIntervalUnit() {
        return fetchIntervalUnit;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
