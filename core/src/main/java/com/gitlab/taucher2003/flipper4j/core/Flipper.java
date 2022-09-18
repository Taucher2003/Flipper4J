package com.gitlab.taucher2003.flipper4j.core;

import com.gitlab.taucher2003.flipper4j.core.config.FlipperConfiguration;
import com.gitlab.taucher2003.flipper4j.core.config.FlipperConfigurator;
import com.gitlab.taucher2003.flipper4j.core.http.Reader;
import com.gitlab.taucher2003.flipper4j.core.http.Writer;
import com.gitlab.taucher2003.flipper4j.core.model.Feature;
import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class Flipper {

    private final Reader reader;
    private final FeatureRegistry registry;
    private final FlipperAdmin admin;

    private Flipper(Reader reader, FeatureRegistry registry, FlipperAdmin admin) {
        this.reader = reader;
        this.registry = registry;
        this.admin = admin;
    }

    public void shutdown() {
        reader.cancel();
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
        return admin;
    }

    public static Flipper create(FlipperConfiguration configuration) {
        var registry = new FeatureRegistry();
        var reader = new Reader(configuration, registry);
        var writer = new Writer(configuration, reader);
        var admin = new FlipperAdmin(configuration, writer);
        var flipper = new Flipper(reader, registry, admin);
        reader.schedule(configuration.getFetchInterval(), configuration.getFetchIntervalUnit());
        return flipper;
    }

    public static FlipperConfigurator configure() {
        return new FlipperConfigurator();
    }
}
