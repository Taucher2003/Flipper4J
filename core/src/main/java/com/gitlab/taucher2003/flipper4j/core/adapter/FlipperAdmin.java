package com.gitlab.taucher2003.flipper4j.core.adapter;

import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;

import java.util.concurrent.CompletableFuture;

public interface FlipperAdmin {

    CompletableFuture<Void> createFeature(String name);

    CompletableFuture<Void> deleteFeature(String name);

    CompletableFuture<Void> clearFeature(String name);

    CompletableFuture<Void> enableFeatureBoolean(String name);

    CompletableFuture<Void> disableFeatureBoolean(String name);

    CompletableFuture<Void> enableFeatureActor(String name, FlipperIdentifier identifier);

    CompletableFuture<Void> disableFeatureActor(String name, FlipperIdentifier identifier);

    CompletableFuture<Void> enableFeatureActorPercentage(String name, int percentage);

    CompletableFuture<Void> disableFeatureActorPercentage(String name);

    CompletableFuture<Void> enableFeatureTimePercentage(String name, int percentage);

    CompletableFuture<Void> disableFeatureTimePercentage(String name);
}
