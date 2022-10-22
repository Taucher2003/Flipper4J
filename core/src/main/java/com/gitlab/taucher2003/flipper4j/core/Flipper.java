package com.gitlab.taucher2003.flipper4j.core;

import com.gitlab.taucher2003.flipper4j.core.adapter.FlipperAdapter;
import com.gitlab.taucher2003.flipper4j.core.adapter.FlipperAdmin;
import com.gitlab.taucher2003.flipper4j.core.model.Feature;
import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class Flipper {

    private final FlipperAdapter adapter;
    private final FeatureRegistry registry;

    private Flipper(FlipperAdapter adapter, FeatureRegistry registry) {
        this.adapter = adapter;
        this.registry = registry;
    }

    public void shutdown() {
        adapter.shutdown();
    }

    public void awaitReady() {
        while(!registry.isReady()) {
            Thread.onSpinWait();
        }
    }

    public void awaitReady(int duration, TimeUnit timeUnit) {
        var waitingStart = System.currentTimeMillis();
        while(
                !registry.isReady()
                && System.currentTimeMillis() - waitingStart < timeUnit.toMillis(duration)
        ) {
            Thread.onSpinWait();
        }
    }

    public boolean isReady() {
        return registry.isReady();
    }

    public boolean isEnabled(String feature) {
        return isEnabled(feature, null);
    }

    public boolean isEnabled(String feature, FlipperIdentifier identifier) {
        return registry.getFeature(feature).map(f -> f.isEnabled(identifier)).orElse(false);
    }

    public Optional<Feature> getFeature(String feature) {
        return registry.getFeature(feature);
    }

    public FlipperAdmin admin() {
        return adapter.admin();
    }

    public static Flipper create(FlipperAdapter adapter) {
        var registry = new FeatureRegistry();
        adapter.setup(registry);
        var flipper = new Flipper(adapter, registry);
        adapter.start();
        return flipper;
    }
}
