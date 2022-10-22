package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Unsupported implements FeatureGate {

    @Override
    public boolean isEnabled(EvaluationContext context) {
        return false;
    }
}
