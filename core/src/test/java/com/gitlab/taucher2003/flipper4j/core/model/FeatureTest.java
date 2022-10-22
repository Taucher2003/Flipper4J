package com.gitlab.taucher2003.flipper4j.core.model;

import com.gitlab.taucher2003.flipper4j.core.model.gate.Actors;
import com.gitlab.taucher2003.flipper4j.core.model.gate.Boolean;
import com.gitlab.taucher2003.flipper4j.core.model.gate.PercentageOfActors;
import com.gitlab.taucher2003.flipper4j.core.model.gate.PercentageOfTime;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FeatureTest {

    @Test
    void callsAllGates() {
        var booleanGate = mock(Boolean.class);
        when(booleanGate.isEnabled(any())).thenReturn(false);

        var actorsGate = mock(Actors.class);
        when(actorsGate.isEnabled(any())).thenReturn(false);

        var percentageOfActorsGate = mock(PercentageOfActors.class);
        when(percentageOfActorsGate.isEnabled(any())).thenReturn(false);

        var percentageOfTimeGate = mock(PercentageOfTime.class);
        when(percentageOfTimeGate.isEnabled(any())).thenReturn(false);

        var feature = new Feature("key", List.of(booleanGate, actorsGate, percentageOfActorsGate, percentageOfTimeGate));
        FlipperIdentifier identifier = () -> "actor:1";

        assertThat(feature.isEnabled(identifier)).isFalse();

        List.of(booleanGate, actorsGate, percentageOfActorsGate, percentageOfTimeGate).forEach(gate -> {
            var captor = ArgumentCaptor.forClass(EvaluationContext.class);
            verify(gate).isEnabled(captor.capture());
            assertThat(captor.getValue().getFeatureName()).isEqualTo("key");
            assertThat(captor.getValue().flipperId()).isEqualTo("actor:1");
        });
    }

    @Test
    void returnsEarly() {
        var booleanGate = mock(Boolean.class);
        when(booleanGate.isEnabled(any())).thenReturn(false);

        var actorsGate = mock(Actors.class);
        when(actorsGate.isEnabled(any())).thenReturn(true);

        var percentageOfActorsGate = mock(PercentageOfActors.class);
        var percentageOfTimeGate = mock(PercentageOfTime.class);

        var feature = new Feature("key", List.of(booleanGate, actorsGate, percentageOfActorsGate, percentageOfTimeGate));
        FlipperIdentifier identifier = () -> "actor:1";

        assertThat(feature.isEnabled(identifier)).isTrue();

        List.of(booleanGate, actorsGate).forEach(gate -> {
            var captor = ArgumentCaptor.forClass(EvaluationContext.class);
            verify(gate).isEnabled(captor.capture());
            assertThat(captor.getValue().getFeatureName()).isEqualTo("key");
            assertThat(captor.getValue().flipperId()).isEqualTo("actor:1");
        });

        List.of(percentageOfActorsGate, percentageOfTimeGate).forEach(Mockito::verifyNoInteractions);
    }

    @Test
    void canHandleNullActor() {
        var booleanGate = mock(Boolean.class);
        when(booleanGate.isEnabled(any())).thenReturn(false);

        var feature = new Feature("key", List.of(booleanGate));
        assertThat(feature.isEnabled(null)).isFalse();

        var captor = ArgumentCaptor.forClass(EvaluationContext.class);
        verify(booleanGate).isEnabled(captor.capture());
        assertThat(captor.getValue().getFeatureName()).isEqualTo("key");
        assertThat(captor.getValue().flipperId()).isNull();
    }
}
