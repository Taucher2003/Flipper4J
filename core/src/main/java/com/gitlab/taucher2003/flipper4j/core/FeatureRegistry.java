package com.gitlab.taucher2003.flipper4j.core;

import com.gitlab.taucher2003.flipper4j.core.model.Feature;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FeatureRegistry {

    private Map<String, Feature> features;

    public void setFeatures(Collection<Feature> features) {
        this.features = features.stream().collect(Collectors.toMap(Feature::getKey, Function.identity()));
    }

    public Optional<Feature> getFeature(String key) {
        return Optional.ofNullable(features.get(key));
    }

    public boolean isReady() {
        return features != null;
    }

    public Map<String, Feature> getFeatures() {
        return Collections.unmodifiableMap(features);
    }
}
