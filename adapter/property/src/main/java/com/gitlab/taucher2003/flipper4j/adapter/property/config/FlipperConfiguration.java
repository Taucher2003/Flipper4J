package com.gitlab.taucher2003.flipper4j.adapter.property.config;

public class FlipperConfiguration {
    private final String propertyPrefix;
    private final String actorsSeparator;

    FlipperConfiguration(String propertyPrefix, String actorsSeparator) {
        this.propertyPrefix = propertyPrefix;
        this.actorsSeparator = actorsSeparator;
    }

    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    public String getActorsSeparator() {
        return actorsSeparator;
    }
}
