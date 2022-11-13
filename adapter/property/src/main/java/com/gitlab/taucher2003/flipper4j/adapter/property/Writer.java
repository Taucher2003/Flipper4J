package com.gitlab.taucher2003.flipper4j.adapter.property;

import com.gitlab.taucher2003.flipper4j.adapter.property.config.FlipperConfiguration;

import java.util.concurrent.CompletableFuture;

public class Writer {

    private final FlipperConfiguration configuration;
    private final Reader reader;

    public Writer(FlipperConfiguration configuration, Reader reader) {
        this.configuration = configuration;
        this.reader = reader;
    }

    public CompletableFuture<Void> setValue(String property, String value) {
        System.setProperty(configuration.getPropertyPrefix() + "." + property, value);
        reader.updateLocalState();
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> removeValue(String property) {
        System.clearProperty(configuration.getPropertyPrefix() + "." + property);
        reader.updateLocalState();
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> addToProperty(String property, CharSequence value) {
        var propertyValue = System.getProperty(configuration.getPropertyPrefix() + "." + property);
        if(propertyValue == null) {
            propertyValue = "";
        }
        return setValue(property, propertyValue + configuration.getActorsSeparator() + value);
    }

    public CompletableFuture<Void> removeFromProperty(String property, CharSequence value) {
        var propertyValue = System.getProperty(configuration.getPropertyPrefix() + "." + property);
        if(propertyValue == null) {
            propertyValue = "";
        }
        return setValue(property, propertyValue.replace(value, ""));
    }
}
