package com.gitlab.taucher2003.flipper4j.adapter.http.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FlipperConfiguratorTest {

    @Test
    void configureObjectMapper() {
        Consumer<ObjectMapper> consumer = mock(Consumer.class);
        var configurator = new FlipperConfigurator();

        configurator.configureObjectMapper(consumer);

        verify(consumer).accept(any(ObjectMapper.class));
    }

    @Test
    void setBaseUrl() {
        var configurator = new FlipperConfigurator();

        assertThatThrownBy(() -> configurator.setBaseUrl("0.0.0.0:80")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void configureHttpClient() {
        Consumer<HttpClient.Builder> consumer = mock(Consumer.class);
        var configurator = new FlipperConfigurator();

        configurator.configureHttpClient(consumer);

        verify(consumer).accept(any(HttpClient.Builder.class));
    }

    @Test
    void buildWithoutSettings() {
        var configurator = new FlipperConfigurator();

        assertThatThrownBy(configurator::build).hasMessage("Configuration can't be built without base url").isInstanceOf(IllegalArgumentException.class);
    }
}
