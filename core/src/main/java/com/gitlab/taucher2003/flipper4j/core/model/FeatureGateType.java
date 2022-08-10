package com.gitlab.taucher2003.flipper4j.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FeatureGateType {
    @JsonProperty("boolean")
    BOOLEAN,
    @JsonProperty("actors")
    ACTORS,
    @JsonProperty("percentage_of_actors")
    PERCENTAGE_OF_ACTORS,
    @JsonProperty("percentage_of_time")
    PERCENTAGE_OF_TIME,
    @JsonProperty("groups")
    GROUPS
}
