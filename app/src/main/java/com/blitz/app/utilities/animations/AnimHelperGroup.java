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

    // Runs on animations complete.
    private Runnable mOnCompleteListener;

    private int mAnimCompleteCount;

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

        // Add a completion listener.
        animHelper.addOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Increment completion count.
                mAnimCompleteCount++;

                // If all animations are complete.
                if (mAnimCompleteCount == mAnimHelperGroup.size()) {

                    if (mOnCompleteListener != null) {
                        mOnCompleteListener.run();
                    }
                }
            }
        });

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
     * Enable the animation group.
     *
     * @param start If specified, also start it.
     */
    public void enable(boolean start) {

        // Enable each animation helper.
        for (AnimHelper animHelper : mAnimHelperGroup) {

            animHelper.enable();

            if (start) {

                // Reset the count.
                mAnimCompleteCount = 0;

                // Start after transition.
                animHelper.start(true);
            }
        }
    }

    /**
     * By default enable and start.
     */
    public void enable() {
        enable(true);
    }

    /**
     * Start the animations.
     */
    public void start() {

        // Reset the count.
        mAnimCompleteCount = 0;

        // Start each animation helper.
        for (AnimHelper animHelper : mAnimHelperGroup) {

            animHelper.start(false);
        }
    }

    /**
     * Set a callback that runs when all animation
     * helpers have run.
     *
     * @param onCompleteListener Callback that will run.
     */
    @SuppressWarnings("unused")
    public void setOnCompleteListener(Runnable onCompleteListener) {

        mOnCompleteListener = onCompleteListener;
    }
}