package com.gitlab.taucher2003.flipper4j.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature {
    private final String key;
    private final List<FeatureGate> gates;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Feature(@JsonProperty("key") String key, @JsonProperty("gates") List<FeatureGate> gates) {
        this.key = key;
        this.gates = gates;
    }

    public String getKey() {
        return key;
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
