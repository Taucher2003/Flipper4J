package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BooleanTest {

    @Test
    void withoutActorDisabled() {
        var gate = new Boolean(false);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn(null);

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withActorDisabled() {
        var gate = new Boolean(false);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:1");

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withoutActorEnabled() {
        var gate = new Boolean(true);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn(null);

        assertThat(gate.isEnabled(context)).isTrue();
    }

    @Test
    void withActorEnabled() {
        var gate = new Boolean(true);

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:1");

        assertThat(gate.isEnabled(context)).isTrue();
    }
}
