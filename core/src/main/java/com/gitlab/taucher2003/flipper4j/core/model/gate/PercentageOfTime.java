package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

import java.util.concurrent.ThreadLocalRandom;

public class PercentageOfTime extends FeatureGate {
    private final double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PercentageOfTime(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        super(name);
        this.value = value;
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        return ThreadLocalRandom.current().nextInt(100) < value;
    }
}
