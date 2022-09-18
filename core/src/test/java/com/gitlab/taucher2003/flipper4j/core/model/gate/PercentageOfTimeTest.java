package com.gitlab.taucher2003.flipper4j.core.model.gate;

import com.gitlab.taucher2003.flipper4j.core.model.EvaluationContext;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class PercentageOfTimeTest {

    EvaluationContext context = mock(EvaluationContext.class);

    @Test
    void disabled() {
        var gate = new PercentageOfTime("percentage_of_time", 0);

        assertThat(gate.isEnabled(context)).isFalse();
    }

    @Test
    void enabled() {
        var gate = new PercentageOfTime("percentage_of_time", 100);

        assertThat(gate.isEnabled(context)).isTrue();
    }

    @Test
    void partial() {
        var percent = 50;

        var gate = new PercentageOfTime("percentage_of_time", percent);

        try(var mockStatic = mockStatic(ThreadLocalRandom.class)) {
            var random = mock(ThreadLocalRandom.class);

            var stub = when(random.nextInt(anyInt()));
            for(var i = 0; i<100; i++) {
                stub = stub.thenReturn(i);
            }

            when(ThreadLocalRandom.current()).thenReturn(random);

            for(var i = 0; i<percent; i++) {
                assertThat(gate.isEnabled(context)).as(String.format("Gate is enabled for %s percent", i)).isTrue();
            }
            for(var i = percent; i<100; i++) {
                assertThat(gate.isEnabled(context)).as(String.format("Gate is disabled for %s percent", i)).isFalse();
            }
        }
    }
}
