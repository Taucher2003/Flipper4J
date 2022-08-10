package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGateType;

public class PercentageOfActors extends FeatureGate {
    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PercentageOfActors(@JsonProperty("key") FeatureGateType key, @JsonProperty("name") String name, @JsonProperty("value") String value) {
        super(key, name);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
