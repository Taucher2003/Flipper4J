package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Boolean implements FeatureGate {
    private final boolean value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Boolean(@JsonProperty("value") boolean value) {
        this.value = value;
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        return value;
    }
}
