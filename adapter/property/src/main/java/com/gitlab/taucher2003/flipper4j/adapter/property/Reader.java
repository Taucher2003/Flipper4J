package com.gitlab.taucher2003.flipper4j.adapter.property;

import com.gitlab.taucher2003.flipper4j.adapter.property.config.FlipperConfiguration;
import com.gitlab.taucher2003.flipper4j.core.FeatureRegistry;
import com.gitlab.taucher2003.flipper4j.core.model.Feature;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;
import com.gitlab.taucher2003.flipper4j.core.model.gate.Actors;
import com.gitlab.taucher2003.flipper4j.core.model.gate.Boolean;
import com.gitlab.taucher2003.flipper4j.core.model.gate.PercentageOfActors;
import com.gitlab.taucher2003.flipper4j.core.model.gate.PercentageOfTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIXES;
import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIX_ACTORS;
import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIX_BOOLEAN;
import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIX_PERCENTAGE_OF_ACTORS;
import static com.gitlab.taucher2003.flipper4j.adapter.property.FlipperPropertyAdapter.GATE_SUFFIX_PERCENTAGE_OF_TIME;

public class Reader {
    private final FlipperConfiguration configuration;
    private final FeatureRegistry registry;

    public Reader(FlipperConfiguration configuration, FeatureRegistry registry) {
        this.configuration = configuration;
        this.registry = registry;
    }

    void updateLocalState() {
        var features = retrieveFeatures();
        registry.setFeatures(features);
    }

    public List<Feature> retrieveFeatures() {
        return System.getProperties()
                .entrySet()
                .stream()
                .map(entry -> Map.entry((String) entry.getKey(), (String) entry.getValue()))
                .filter(entry -> entry.getKey().startsWith(configuration.getPropertyPrefix() + "."))
                .collect(
                        HashMap::new,
                        (BiConsumer<Map<String, Map<String, String>>, ? super Map.Entry<String, String>>) (a, b) -> {
                            var featureName = extractFeatureName(b.getKey());
                            a.putIfAbsent(featureName, new HashMap<>());
                            a.get(featureName).put(extractToggleName(b.getKey()), b.getValue());
                        },
                        Map::putAll
                )
                .entrySet()
                .stream()
                .map(this::convertFeatureGates)
                .map(this::convertFeatures)
                .collect(Collectors.toList());
    }

    private String extractFeatureName(String name) {
        return GATE_SUFFIXES.stream().reduce(
                name.replace(configuration.getPropertyPrefix() + ".", ""),
                (acc, cur) -> acc.replace("." + cur, "")
        );
    }

    private String extractToggleName(String name) {
        return GATE_SUFFIXES.stream()
                .filter(gate -> name.endsWith("." + gate))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No gate found for '%s'", name)));
    }

    private Map.Entry<String, List<FeatureGate>> convertFeatureGates(Map.Entry<String, Map<String, String>> entry) {
        return Map.entry(
                entry.getKey(),
                entry.getValue()
                        .entrySet()
                        .stream()
                        .map(this::gateFromEntry)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    private FeatureGate gateFromEntry(Map.Entry<String, String> entry) {
        var gateName = entry.getKey();
        FeatureGate gate = null;

        switch (gateName) {
            case GATE_SUFFIX_BOOLEAN:
                gate = new Boolean("true".equals(entry.getValue()));
                break;
            case GATE_SUFFIX_ACTORS:
                gate = new Actors(entry.getValue().split(configuration.getActorsSeparator()));
                break;
            case GATE_SUFFIX_PERCENTAGE_OF_ACTORS:
                gate = new PercentageOfActors(Double.parseDouble(entry.getValue()));
                break;
            case GATE_SUFFIX_PERCENTAGE_OF_TIME:
                gate = new PercentageOfTime(Double.parseDouble(entry.getValue()));
                break;
        }

        return gate;
    }

    private Feature convertFeatures(Map.Entry<String, List<FeatureGate>> entry) {
        return new Feature(entry.getKey(), entry.getValue());
    }
}
