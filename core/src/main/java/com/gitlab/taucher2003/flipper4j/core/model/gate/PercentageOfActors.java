package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import com.gitlab.taucher2003.flipper4j.core.model.FeatureGate;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class PercentageOfActors extends FeatureGate {
    private final double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PercentageOfActors(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        super(name);
        this.value = value;
    }

    @Override
    public boolean isEnabled(EvaluationContext context) {
        var id = context.flipperId();
        if(id == null) {
            return value == 100;
        }
        var crc32 = new CRC32();
        crc32.update((context.getFeatureName() + ":" + id).getBytes(StandardCharsets.UTF_8));
        return crc32.getValue() % (100 * 1_000) < value * 1_000;
    }
}
