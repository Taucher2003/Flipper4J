package com.gitlab.taucher2003.flipper4j.core.adapter;

import com.gitlab.taucher2003.flipper4j.core.FeatureRegistry;
import com.gitlab.taucher2003.flipper4j.core.Flipper;

public interface FlipperAdapter {

    void setup(FeatureRegistry registry);
    void start();
    void shutdown();

    FlipperAdmin admin();

    default Flipper createFlipper() {
        return Flipper.create(this);
    }
}
