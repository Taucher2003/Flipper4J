package com.gitlab.taucher2003.flipper4j.adapter.http;

import com.gitlab.taucher2003.flipper4j.adapter.http.config.FlipperConfiguration;
import com.gitlab.taucher2003.flipper4j.core.FeatureRegistry;
import com.gitlab.taucher2003.flipper4j.core.model.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Reader {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(r -> {
        var thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    private final FlipperConfiguration configuration;
    private final FeatureRegistry registry;
    private ScheduledFuture<?> scheduledFuture;

    public Reader(FlipperConfiguration configuration, FeatureRegistry registry) {
        this.configuration = configuration;
        this.registry = registry;
    }

    public void schedule(long time, TimeUnit unit) {
        cancel();
        scheduledFuture = EXECUTOR_SERVICE.scheduleAtFixedRate(this::updateLocalState, 0, time, unit);
    }

    void updateLocalState() {
        try {
            var features = retrieveFeatures();
            registry.setFeatures(features);
        } catch (IOException e) {
            LOGGER.error("Unexpected IOException while retrieving features", e);
        } catch (InterruptedException e) {
            LOGGER.error("Retrieving Thread has been interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Throwable e) {
            LOGGER.error("Unexpected Throwable while retrieving features", e);
        }
    }

    public void cancel() {
        if(scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    public List<Feature> retrieveFeatures() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(configuration.getBaseUrl() + "/features"))
                .GET()
                .build();
        var response = configuration.getHttpClient().send(request, HttpResponse.BodyHandlers.ofInputStream());
        return configuration.getObjectMapper().readValue(response.body(), Wrapper.Feature.class).features;
    }
}
