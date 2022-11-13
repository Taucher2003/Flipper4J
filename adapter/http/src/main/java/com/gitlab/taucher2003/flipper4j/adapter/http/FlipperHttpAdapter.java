package com.gitlab.taucher2003.flipper4j.adapter.http;

import com.gitlab.taucher2003.flipper4j.adapter.http.config.FlipperConfiguration;
import com.gitlab.taucher2003.flipper4j.core.FeatureRegistry;
import com.gitlab.taucher2003.flipper4j.core.adapter.FlipperAdapter;
import com.gitlab.taucher2003.flipper4j.core.adapter.FlipperAdmin;

public class FlipperHttpAdapter implements FlipperAdapter {

    private final FlipperConfiguration configuration;

    private Reader reader;
    private FlipperAdmin admin;

    public FlipperHttpAdapter(FlipperConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setup(FeatureRegistry registry) {
        reader = new Reader(configuration, registry);
        var writer = new Writer(configuration, reader);
        admin = new FlipperHttpAdmin(configuration, writer);
    }

    @Override
    public void start() {
        reader.schedule(configuration.getFetchInterval(), configuration.getFetchIntervalUnit());
    }

    @Override
    public void shutdown() {
        reader.cancel();
    }

    @Override
    public void reload() {
        reader.updateLocalState();
    }

    @Override
    public FlipperAdmin admin() {
        return admin;
    }
}
