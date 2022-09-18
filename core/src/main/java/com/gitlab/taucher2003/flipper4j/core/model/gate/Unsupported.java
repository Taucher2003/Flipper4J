package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

public class Unsupported extends FeatureGate {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Unsupported(
            @JsonProperty("name") String name,
            @SuppressWarnings("unused") @JsonProperty("value") Object value
    ) {
        super(name);
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        return false;
    }
}
