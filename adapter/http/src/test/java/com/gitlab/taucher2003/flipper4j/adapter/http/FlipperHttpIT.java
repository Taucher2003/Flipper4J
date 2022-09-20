package com.gitlab.taucher2003.flipper4j.adapter.http;

import com.gitlab.taucher2003.flipper4j.adapter.http.config.FlipperConfigurator;
import com.gitlab.taucher2003.flipper4j.core.Flipper;
import com.gitlab.taucher2003.flipper4j.core.FlipperIT;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class FlipperHttpIT extends FlipperIT {

    public static final String DEFAULT_BACKEND_IMAGE = "registry.gitlab.com/taucher2003-group/flipper4j/test-backend:" + System.getenv().getOrDefault("T_TEST_BACKEND_IMAGE_TAG", "latest");

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

    @Override
    protected Flipper createFlipper(boolean shouldWork) {
        var adapter = new FlipperConfigurator()
                .setBaseUrl("http://" + flipperApi.getHost() + ":" + (flipperApi.getFirstMappedPort() + (shouldWork ? 0 : 1)))
                .build();
        return flipper = Flipper.create(adapter);
    }
}
