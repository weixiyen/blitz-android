package com.blitz.app.utilities.animations;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

/**
 * Created by mrkcsc on 7/29/14.
 */
public class AnimationHelper {

    // TODO: Annotation for runtime mapping of view rect (top, dims, etc).

    /**
     * Create a spring animation object.
     *
     * @param tension Associated tension.
     * @param friction Associated friction.
     *
     * @return Initialize spring object.
     */
    public static Spring createSpring(int tension, int friction) {

        // Create a spring config with provided parameters.
        SpringConfig springConfig = SpringConfig
                .fromOrigamiTensionAndFriction(tension, friction);

        return SpringSystem
                .create()
                .createSpring()
                .setSpringConfig(springConfig)
                .setAtRest();
    }

    public void disable() {

    }

    public void enable() {

    }

    public void enable(int delay) {

    }
}