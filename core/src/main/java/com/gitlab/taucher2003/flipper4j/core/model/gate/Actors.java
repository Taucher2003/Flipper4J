package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Actors implements FeatureGate {
    private final List<String> value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Actors(@JsonProperty("value") List<String> value) {
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
