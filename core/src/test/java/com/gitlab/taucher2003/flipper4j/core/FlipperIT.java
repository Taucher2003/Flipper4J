package com.gitlab.taucher2003.flipper4j.core;

import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@Testcontainers
public abstract class FlipperIT {

    public Flipper flipper;

    protected Flipper createFlipper() {
        return createFlipper(true);
    }

    protected abstract Flipper createFlipper(boolean shouldWork);

    @BeforeEach
    void setUp() {
        flipper = createFlipper();
        flipper.awaitReady(5, TimeUnit.SECONDS);
    }

    @AfterEach
    void tearDown() {
        flipper.shutdown();
    }

    @Test
    @Timeout(5)
    void awaitReady() {
        var flipper = createFlipper();
        flipper.awaitReady();

        assertThat(flipper.isReady()).isTrue();

        flipper.shutdown();
    }

    @Test
    void awaitReadyWithTimeout() {
        var flipper = createFlipper(false);
        var waitStart = System.currentTimeMillis();
        flipper.awaitReady(1, TimeUnit.SECONDS);
        var waitEnd = System.currentTimeMillis();

        flipper.shutdown();

        assertThat(waitEnd - waitStart).isCloseTo(TimeUnit.SECONDS.toMillis(1), Offset.offset(100L));
    }

    @Test
    void deleteFeature() {
        flipper.admin().createFeature("test_flag").join();
        assertThat(flipper.getFeature("test_flag")).isPresent();

        flipper.admin().deleteFeature("test_flag").join();

        assertThat(flipper.getFeature("test_flag")).isEmpty();
    }

    @Test
    void clearFeature() {
        flipper.admin().createFeature("test_flag").join();
        flipper.admin().enableFeatureBoolean("test_flag").join();
        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag")).isTrue();

        flipper.admin().clearFeature("test_flag").join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag")).isFalse();
    }

    @Test
    void disabledFlagWithoutActor() {
        flipper.admin().createFeature("test_flag").join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag")).isFalse();
    }

    @Test
    void disabledFlagWithActor() {
        FlipperIdentifier flipperIdentifier = () -> "actor";

        flipper.admin().createFeature("test_flag").join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag", flipperIdentifier)).isFalse();
    }

    @Test
    void nonExistentFlag() {
        assertThat(flipper.getFeature("non_existent")).isEmpty();
        assertThat(flipper.isEnabled("non_existent")).isFalse();
    }

    @Test
    void booleanEnabledFlagWithoutActor() {
        flipper.admin().createFeature("test_flag").join();
        flipper.admin().enableFeatureBoolean("test_flag").join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag")).isTrue();
    }

    @Test
    void booleanEnabledFlagDisabling() {
        booleanEnabledFlagWithoutActor();
        flipper.admin().disableFeatureBoolean("test_flag").join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag")).isFalse();
    }

    @Test
    void actorEnabledFlagWithoutActor() {
        FlipperIdentifier actor = () -> "actor";

        flipper.admin().createFeature("test_flag").join();
        flipper.admin().enableFeatureActor("test_flag", actor).join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag")).isFalse();
    }

    @Test
    void actorEnabledFlagWithActor() {
        FlipperIdentifier actor = () -> "actor";

        flipper.admin().createFeature("test_flag").join();
        flipper.admin().enableFeatureActor("test_flag", actor).join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag", actor)).isTrue();
    }

    @Test
    void actorEnabledFlagDisabling() {
        FlipperIdentifier actor = () -> "actor";
        actorEnabledFlagWithActor();
        flipper.admin().disableFeatureActor("test_flag", actor).join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag", actor)).isFalse();
    }

    @Test
    void percentageActorEnabledWithActor() {
        FlipperIdentifier actor = () -> "actor:5";

        flipper.admin().createFeature("feature").join();
        flipper.admin().enableFeatureActorPercentage("feature", 50).join();

        assertThat(flipper.getFeature("feature")).isPresent();
        assertThat(flipper.isEnabled("feature", actor)).isTrue();
    }

    @Test
    void percentageActorEnabledDisabling() {
        FlipperIdentifier actor = () -> "actor:5";
        percentageActorEnabledWithActor();
        flipper.admin().disableFeatureActorPercentage("feature").join();

        assertThat(flipper.getFeature("feature")).isPresent();
        assertThat(flipper.isEnabled("feature", actor)).isFalse();
    }

    @Test
    void percentageActorEnabledWithWrongActor() {
        FlipperIdentifier actor = () -> "actor:4";

        flipper.admin().createFeature("feature").join();
        flipper.admin().enableFeatureActorPercentage("feature", 50).join();

        assertThat(flipper.getFeature("feature")).isPresent();
        assertThat(flipper.isEnabled("feature", actor)).isFalse();
    }

    @Test
    void percentageTimeEnabled() {
        flipper.admin().createFeature("test_flag").join();
        flipper.admin().enableFeatureTimePercentage("test_flag", 50).join();

        try(var mockStatic = mockStatic(ThreadLocalRandom.class)) {
            var random = mock(ThreadLocalRandom.class);
            when(random.nextInt(anyInt()))
                    .thenReturn(49)
                    .thenReturn(50);

            when(ThreadLocalRandom.current()).thenReturn(random);

            assertThat(flipper.isEnabled("test_flag")).isTrue();
            assertThat(flipper.isEnabled("test_flag")).isFalse();
        }
    }

    @Test
    void percentageTimeEnabledDisabling() {
        percentageTimeEnabled();
        flipper.admin().disableFeatureTimePercentage("test_flag").join();

        assertThat(flipper.getFeature("test_flag")).isPresent();
        assertThat(flipper.isEnabled("test_flag")).isFalse();
    }
}
