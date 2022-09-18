package com.gitlab.taucher2003.flipper4j.core.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gitlab.taucher2003.flipper4j.core.model.gate.Actors;
import com.gitlab.taucher2003.flipper4j.core.model.gate.PercentageOfActors;
import com.gitlab.taucher2003.flipper4j.core.model.gate.PercentageOfTime;
import com.gitlab.taucher2003.flipper4j.core.model.gate.Unsupported;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "key", defaultImpl = Unsupported.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = com.gitlab.taucher2003.flipper4j.core.model.gate.Boolean.class, name = "boolean"),
        @JsonSubTypes.Type(value = Actors.class, name = "actors"),
        @JsonSubTypes.Type(value = PercentageOfActors.class, name = "percentage_of_actors"),
        @JsonSubTypes.Type(value = PercentageOfTime.class, name = "percentage_of_time")
})
public abstract class FeatureGate {
    private final String name;

    public FeatureGate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean isEnabled(EvaluationContext context);
}
