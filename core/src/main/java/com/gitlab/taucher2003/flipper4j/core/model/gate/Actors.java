package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGateType;

import java.util.List;

public class Actors extends FeatureGate {
    private final List<String> value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Actors(@JsonProperty("key") FeatureGateType key, @JsonProperty("name") String name, @JsonProperty("value") List<String> value) {
        super(key, name);
        this.value = value;
    }

    public List<String> getValue() {
        return value;
    }
}
