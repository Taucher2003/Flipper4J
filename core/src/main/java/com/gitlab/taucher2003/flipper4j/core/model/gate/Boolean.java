package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

public class Boolean extends FeatureGate {
    private final boolean value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Boolean(@JsonProperty("name") String name, @JsonProperty("value") boolean value) {
        super(name);
        this.value = value;
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        return value;
    }
}
