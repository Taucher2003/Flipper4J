package com.gitlab.taucher2003.flipper4j.adapter.property.config;

import com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter;

public class FlipperConfigurator {

    private String propertyPrefix = "flipper4j";
    private String actorsSeparator = ",";

    public void setPropertyPrefix(String propertyPrefix) {
        this.propertyPrefix = propertyPrefix;
    }

    public void setActorsSeparator(String actorsSeparator) {
        this.actorsSeparator = actorsSeparator;
    }

    private FlipperConfiguration buildConfiguration() {
        return new FlipperConfiguration(propertyPrefix, actorsSeparator);
    }

    public FlipperPropertyAdapter build() {
        return new FlipperPropertyAdapter(buildConfiguration());
    }
}
