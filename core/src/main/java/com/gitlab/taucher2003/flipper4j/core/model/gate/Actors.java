package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

import java.util.List;

public class Actors extends FeatureGate {
    private final List<String> value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Actors(@JsonProperty("name") String name, @JsonProperty("value") List<String> value) {
        super(name);
        this.value = value;
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        var id = context.flipperId();
        if(id == null) {
            return false;
        }
        return value.contains(id);
    }
}
