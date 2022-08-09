package com.gitlab.taucher2003.flipper4j.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FeatureState {
    @JsonProperty("on")
    ON,
    @JsonProperty("conditional")
    CONDITIONAL,
    @JsonProperty("off")
    OFF
}
