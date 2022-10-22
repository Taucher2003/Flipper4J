package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PercentageOfActorsTest {

    @Test
    void withoutActorDisabled() {
        var gate = new PercentageOfActors(0);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn(null);

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withoutActorPartial() {
        var gate = new PercentageOfActors(99);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn(null);

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withoutActorEnabled() {
        var gate = new PercentageOfActors(100);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn(null);

        assertThat(gate.isEnabled(context)).isTrue();
    }

    @Test
    void withActorDisabled() {
        var gate = new PercentageOfActors(0);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:1");
        when(context.getFeatureName()).thenReturn("feature");

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withActorPartialEnabled() {
        var gate = new PercentageOfActors(50);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:5");
        when(context.getFeatureName()).thenReturn("feature");

        assertThat(gate.isEnabled(context)).isTrue();
    }

    @Test
    void withActorPartialDisabled() {
        var gate = new PercentageOfActors(50);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:4");
        when(context.getFeatureName()).thenReturn("feature");

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withActorEnabled() {
        var gate = new PercentageOfActors(100);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:1");
        when(context.getFeatureName()).thenReturn("feature");

        assertThat(gate.isEnabled(context)).isTrue();
    }
}
