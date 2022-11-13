package com.gitlab.taucher2003.flipper4j.adapter.property;

import com.gitlab.taucher2003.flipper4j.adapter.property.config.FlipperConfiguration;
import com.gitlab.taucher2003.flipper4j.core.FeatureRegistry;
import com.gitlab.taucher2003.flipper4j.core.adapter.FlipperAdapter;
import com.gitlab.taucher2003.flipper4j.core.adapter.FlipperAdmin;

import java.util.List;

public class FlipperPropertyAdapter implements FlipperAdapter {

    static final String GATE_SUFFIX_BOOLEAN = "boolean";
    static final String GATE_SUFFIX_ACTORS = "actors";
    static final String GATE_SUFFIX_PERCENTAGE_OF_ACTORS = "percentage_of_actors";
    static final String GATE_SUFFIX_PERCENTAGE_OF_TIME = "percentage_of_time";
    static final List<String> GATE_SUFFIXES = List.of(
            GATE_SUFFIX_BOOLEAN,
            GATE_SUFFIX_ACTORS,
            GATE_SUFFIX_PERCENTAGE_OF_ACTORS,
            GATE_SUFFIX_PERCENTAGE_OF_TIME
    );

    private final FlipperConfiguration configuration;

    private Reader reader;
    private FlipperAdmin admin;

    public FlipperPropertyAdapter(FlipperConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setup(FeatureRegistry registry) {
        reader = new Reader(configuration, registry);
        var writer = new Writer(configuration, reader);
        admin = new FlipperPropertyAdmin(writer);
    }

    @Override
    public void start() {
        reader.updateLocalState();
    }

    @Override
    public void shutdown() {
        // no-op
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
