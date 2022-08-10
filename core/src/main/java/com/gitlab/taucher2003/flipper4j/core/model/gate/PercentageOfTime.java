package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGateType;

public class PercentageOfTime extends FeatureGate {
    private final double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PercentageOfTime(@JsonProperty("key") FeatureGateType key, @JsonProperty("name") String name, @JsonProperty("value") double value) {
        super(key, name);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        return System.currentTimeMillis() % 100 <= value;
    }
}
