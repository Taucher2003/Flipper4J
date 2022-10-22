package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UnsupportedTest {

    @Test
    void isAlwaysDisabled() {
        var gate = new Unsupported();

        var context = mock(EvaluationContext.class);

        assertThat(gate.isEnabled(context)).isFalse();
    }
}
