package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGateType;

public class Boolean extends FeatureGate {
    private final java.lang.Boolean value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Boolean(@JsonProperty("key") FeatureGateType key, @JsonProperty("name") String name, @JsonProperty("value") java.lang.Boolean value) {
        super(key, name);
        this.value = value;
    }
}
