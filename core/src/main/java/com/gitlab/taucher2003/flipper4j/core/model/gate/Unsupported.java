package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGateType;

public class Unsupported extends FeatureGate {
    private final Object value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Unsupported(@JsonProperty("key") FeatureGateType key, @JsonProperty("name") String name, @JsonProperty("value") Object value) {
        super(key, name);
        this.value = value;
    }
}
