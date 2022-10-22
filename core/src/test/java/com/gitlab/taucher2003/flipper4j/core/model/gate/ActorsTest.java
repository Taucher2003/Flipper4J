package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ActorsTest {

    @Test
    void withoutActorDisabled() {
        var gate = new Actors(List.of("actor:1"));

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn(null);

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withoutActorsDisabled() {
        var gate = new Actors(Collections.emptyList());

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:1");

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withActorDisabled() {
        var gate = new Actors(List.of("actor:1"));

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:2");

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void withActorEnabled() {
        var gate = new Actors(List.of("actor:1"));

        var context = mock(EvaluationContext.class);
        when(context.flipperId()).thenReturn("actor:1");

        assertThat(gate.isEnabled(context)).isTrue();
    }
}
