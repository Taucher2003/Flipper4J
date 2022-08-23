package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGateType;

public class Boolean extends FeatureGate {
    private final boolean value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Boolean(@JsonProperty("key") FeatureGateType key, @JsonProperty("name") String name, @JsonProperty("value") boolean value) {
        super(key, name);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        return value;
    }
}
