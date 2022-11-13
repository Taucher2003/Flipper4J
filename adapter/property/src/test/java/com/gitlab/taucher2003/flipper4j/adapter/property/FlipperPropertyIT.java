package com.gitlab.taucher2003.flipper4j.adapter.property;

import com.gitlab.taucher2003.flipper4j.adapter.property.config.FlipperConfigurator;
import com.gitlab.taucher2003.flipper4j.core.Flipper;
import com.gitlab.taucher2003.flipper4j.core.FlipperIT;
import com.gitlab.taucher2003.flipper4j.core.model.FlipperIdentifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FlipperPropertyIT extends FlipperIT {
    @Override
    protected Flipper createFlipper() {
        return new FlipperConfigurator()
                .build()
                .createFlipper();
    }

    @Override
    protected Flipper createBrokenFlipper() {
        return null;
    }

    @Test
    void enablingWorksWithoutFeatureCreationBefore() {
        FlipperIdentifier actor = () -> "user";

        flipper.admin().enableFeatureActor("test_flag", actor).join();

        assertThat(flipper.getFeatureNames()).containsExactly("test_flag");
    }

    @Test
    void disablingWorksWithoutFeatureCreationBefore() {
        FlipperIdentifier actor = () -> "user";

        flipper.admin().disableFeatureActor("test_flag", actor).join();

        assertThat(flipper.getFeatureNames()).containsExactly("test_flag");
    }

    @Test
    void throwsForInvalidConfiguration() {
        var gate = "flipper4j.invalid.gate";

        System.setProperty(gate, "true");

        assertThatThrownBy(() -> flipper.reload()).hasMessage("No gate found for '"+ gate + "'").isInstanceOf(IllegalArgumentException.class);

        System.clearProperty(gate);
    }
}
