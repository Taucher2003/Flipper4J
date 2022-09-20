package com.gitlab.taucher2003.flipper4j.core.adapter;

import com.gitlab.taucher2003.flipper4j.core.FeatureRegistry;

public interface FlipperAdapter {

    void setup(FeatureRegistry registry);
    void start();
    void shutdown();

    FlipperAdmin admin();
}
