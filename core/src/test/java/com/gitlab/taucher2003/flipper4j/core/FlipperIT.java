package com.gitlab.taucher2003.flipper4j.core;

import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@Testcontainers
class FlipperIT {

    public static final String DEFAULT_BACKEND_IMAGE = "registry.gitlab.com/taucher2003-group/flipper4j/test-backend:" + System.getenv().getOrDefault("T_TEST_IMAGE_TAG", "latest");

    @SuppressWarnings("resource")
    @Container
    public GenericContainer<?> flipperApi = new GenericContainer<>(
            DockerImageName.parse(
                    System.getenv().getOrDefault("T_BACKEND_IMAGE", DEFAULT_BACKEND_IMAGE)
            )
    )
            .withExposedPorts(9292)
            .withStartupTimeout(Duration.of(500, ChronoUnit.SECONDS))
            .withStartupAttempts(3);

    public Flipper flipper;

    @BeforeEach
    void setUp() {
        flipper = Flipper.configure()
                .setBaseUrl("http://" + flipperApi.getHost() + ":" + flipperApi.getFirstMappedPort())
                .build();
        flipper.awaitReady(5, TimeUnit.SECONDS);
    }

    @AfterEach
    void tearDown() {
        flipper.shutdown();
    }

    @Test
    void awaitReady() {
        var flipper = Flipper.configure()
                .setBaseUrl("http://" + flipperApi.getHost() + ":" + (flipperApi.getFirstMappedPort() + 1))
                .build();
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
    void percentageActorEnabledWithActor() {
        FlipperIdentifier actor = () -> "actor:5";

        flipper.admin().createFeature("feature").join();
        flipper.admin().enableFeatureActorPercentage("feature", 50).join();

        assertThat(flipper.getFeature("feature")).isPresent();
        assertThat(flipper.isEnabled("feature", actor)).isTrue();
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
}
