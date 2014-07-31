package com.blitz.app.utilities.animations;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Miguel on 7/30/2014.
 */
public class AnimHelperGroup {

    //==============================================================================================
    // Constructors
    //==============================================================================================

    // Associated activity.
    private Activity mActivity;

    // List of individual helpers.
    private ArrayList<AnimHelper> mAnimHelperGroup;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Private constructor.
     */
    @SuppressWarnings("unused")
    private AnimHelperGroup() {

    }

    /**
     * Create instance of a helper group.
     *
     * @param activity Target activity.
     */
    private AnimHelperGroup(Activity activity) {

        // Set activity.
        mActivity = activity;

        // Initialize helper group.
        mAnimHelperGroup = new ArrayList<AnimHelper>();
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Static instantiate.
     *
     * @param activity Target activity.
     *
     * @return Instance of a helper group.
     */
    public static AnimHelperGroup from(Activity activity) {

        return new AnimHelperGroup(activity);
    }

    /**
     * Create a new helper for the group.
     *
     * @param tension Tension.
     * @param friction Friction.
     *
     * @return The instantiated helper.
     */
    public AnimHelper createHelper(int tension, int friction) {

        // Create a new animation helper.
        AnimHelper animHelper = AnimHelper.from(mActivity, tension, friction);

        // Add to group.
        mAnimHelperGroup.add(animHelper);

        return animHelper;
    }

    /**
     * Disable the animation helper group.
     */
    public void disable() {

        // Disable each animation helper.
        for (AnimHelper animHelper : mAnimHelperGroup) {

            animHelper.disable();
        }
    }

    /**
     * Enable the animation helper group.
     */
    public void enable() {

        // Enable each animation helper.
        for (AnimHelper animHelper : mAnimHelperGroup) {

            animHelper.enable();
        }
    }
}