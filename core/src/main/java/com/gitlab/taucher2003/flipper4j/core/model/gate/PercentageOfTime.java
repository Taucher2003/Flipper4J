package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

import java.util.concurrent.ThreadLocalRandom;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PercentageOfTime implements FeatureGate {
    private final double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PercentageOfTime(@JsonProperty("value") double value) {
        this.value = value;
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        return ThreadLocalRandom.current().nextInt(100) < value;
    }
}
