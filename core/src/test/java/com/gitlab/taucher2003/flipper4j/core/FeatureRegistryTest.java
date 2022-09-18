package com.gitlab.taucher2003.flipper4j.core;

import com.gitlab.taucher2003.flipper4j.core.model.Feature;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureState;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FeatureRegistryTest {

    @Test
    void setFeatures() {
        var feature = new Feature("key", FeatureState.OFF, Collections.emptyList());

        var registry = new FeatureRegistry();
        registry.setFeatures(List.of(feature));

        assertThat(registry.getFeatures()).containsEntry("key", feature);
    }

    @Test
    void getFeature() {
        var feature = new Feature("key", FeatureState.OFF, Collections.emptyList());

        var registry = new FeatureRegistry();
        registry.setFeatures(List.of(feature));

        assertThat(registry.getFeature("key")).isPresent();
        assertThat(registry.getFeature("key")).get().isEqualTo(feature);
    }

    @Test
    void isReady() {
        var registry = new FeatureRegistry();

        assertThat(registry.isReady()).isFalse();

        registry.setFeatures(Collections.emptyList());

        assertThat(registry.isReady()).isTrue();
    }
}
