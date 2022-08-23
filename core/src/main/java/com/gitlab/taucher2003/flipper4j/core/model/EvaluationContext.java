package com.gitlab.taucher2003.flipper4j.core.model;

public class EvaluationContext implements FlipperIdentifier {
    private final FlipperIdentifier identifier;
    private final String featureName;

    EvaluationContext(FlipperIdentifier identifier, String featureName) {
        this.identifier = identifier;
        this.featureName = featureName;
    }


    @Override
    public String flipperId() {
        if(identifier == null) {
            return null;
        }
        return identifier.flipperId();
    }

    public String getFeatureName() {
        return featureName;
    }
}
