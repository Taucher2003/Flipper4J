package com.gitlab.taucher2003.flipper4j.adapter.property;

import com.gitlab.taucher2003.flipper4j.core.adapter.FlipperAdmin;
import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;

import java.util.concurrent.CompletableFuture;

import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIX_ACTORS;
import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIX_BOOLEAN;
import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIX_PERCENTAGE_OF_ACTORS;
import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIX_PERCENTAGE_OF_TIME;
import static java.lang.String.format;

public class FlipperPropertyAdmin implements FlipperAdmin {

    private final Writer writer;

    FlipperPropertyAdmin(Writer writer) {
        this.writer = writer;
    }

    @Override
    public CompletableFuture<Void> createFeature(String name) {
        return writer.setValue(format("%s.%s", name, GATE_SUFFIX_BOOLEAN), String.valueOf(false));
    }

    @Override
    public CompletableFuture<Void> deleteFeature(String name) {
        return CompletableFuture.allOf(
                writer.removeValue(format("%s.%s", name, GATE_SUFFIX_BOOLEAN)),
                writer.removeValue(format("%s.%s", name, GATE_SUFFIX_ACTORS)),
                writer.removeValue(format("%s.%s", name, GATE_SUFFIX_PERCENTAGE_OF_ACTORS)),
                writer.removeValue(format("%s.%s", name, GATE_SUFFIX_PERCENTAGE_OF_TIME))
        );
    }

    @Override
    public CompletableFuture<Void> clearFeature(String name) {
        var deletion = deleteFeature(name);
        return CompletableFuture.allOf(deletion, writer.setValue(format("%s.%s", name, GATE_SUFFIX_BOOLEAN), String.valueOf(false)));
    }

    @Override
    public CompletableFuture<Void> enableFeatureBoolean(String name) {
        return writer.setValue(format("%s.%s", name, GATE_SUFFIX_BOOLEAN), String.valueOf(true));
    }

    @Override
    public CompletableFuture<Void> disableFeatureBoolean(String name) {
        return writer.setValue(format("%s.%s", name, GATE_SUFFIX_BOOLEAN), String.valueOf(false));
    }

    @Override
    public CompletableFuture<Void> enableFeatureActor(String name, FlipperIdentifier identifier) {
        return writer.addToProperty(format("%s.%s", name, GATE_SUFFIX_ACTORS), identifier.flipperId());
    }

    @Override
    public CompletableFuture<Void> disableFeatureActor(String name, FlipperIdentifier identifier) {
        return writer.removeFromProperty(format("%s.%s", name, GATE_SUFFIX_ACTORS), identifier.flipperId());
    }

    @Override
    public CompletableFuture<Void> enableFeatureActorPercentage(String name, int percentage) {
        return writer.setValue(format("%s.%s", name, GATE_SUFFIX_PERCENTAGE_OF_ACTORS), String.valueOf(percentage));
    }

    @Override
    public CompletableFuture<Void> disableFeatureActorPercentage(String name) {
        return writer.removeValue(format("%s.%s", name, GATE_SUFFIX_PERCENTAGE_OF_ACTORS));
    }

    @Override
    public CompletableFuture<Void> enableFeatureTimePercentage(String name, int percentage) {
        return writer.setValue(format("%s.%s", name, GATE_SUFFIX_PERCENTAGE_OF_TIME), String.valueOf(percentage));
    }

    @Override
    public CompletableFuture<Void> disableFeatureTimePercentage(String name) {
        return writer.removeValue(format("%s.%s", name, GATE_SUFFIX_PERCENTAGE_OF_TIME));
    }
}
