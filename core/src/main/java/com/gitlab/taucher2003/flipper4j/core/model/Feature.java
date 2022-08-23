package com.gitlab.taucher2003.flipper4j.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Feature {
    private final String key;
    private final FeatureState state;
    private final List<FeatureGate> gates;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Feature(@JsonProperty("key") String key, @JsonProperty("state") FeatureState state, @JsonProperty("gates") List<FeatureGate> gates) {
        this.key = key;
        this.state = state;
        this.gates = gates;
    }

    public String getKey() {
        return key;
    }

    public FeatureState getState() {
        return state;
    }

    public List<FeatureGate> getGates() {
        return gates;
    }

    public boolean isEnabled(FlipperIdentifier identifier) {
        var context = new EvaluationContext(identifier, key);
        for (var gate : gates) {
            if(gate.isEnabled(context)) {
                return true;
            }
        }
        return false;
    }
}
