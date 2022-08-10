package com.gitlab.taucher2003.flipper4j.core;

import com.gitlab.taucher2003.flipper4j.core.config.FlipperConfiguration;
import com.gitlab.taucher2003.flipper4j.core.config.FlipperConfigurator;
import com.gitlab.taucher2003.flipper4j.core.http.Reader;
import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;

public final class Flipper {

    private final Reader reader;
    private final FeatureRegistry registry;

    private Flipper(Reader reader, FeatureRegistry registry) {
        this.reader = reader;
        this.registry = registry;
    }

    public void shutdown() {
        reader.cancel();
    }

    public void awaitReady() {
        while(!registry.isReady()) {
            Thread.onSpinWait();
        }
    }

    public boolean isEnabled(String feature, FlipperIdentifier identifier) {
        return registry.getFeature(feature).map(f -> f.isEnabled(identifier)).orElse(false);
    }

    public static Flipper create(FlipperConfiguration configuration) {
        var registry = new FeatureRegistry();
        var reader = new Reader(configuration, registry);
        var flipper = new Flipper(reader, registry);
        reader.schedule(configuration.getFetchInterval(), configuration.getFetchIntervalUnit());
        return flipper;
    }

    public static FlipperConfigurator configure() {
        return new FlipperConfigurator();
    }
}
