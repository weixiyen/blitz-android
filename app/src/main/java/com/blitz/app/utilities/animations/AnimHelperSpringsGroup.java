package com.blitz.app.utilities.animations;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Miguel on 7/30/2014. Copyright 2014 Blitz Studios
 */
public class AnimHelperSpringsGroup {

    //==============================================================================================
    // Constructors
    //==============================================================================================

    // Associated activity.
    private Activity mActivity;

    // List of individual helpers.
    private ArrayList<AnimHelperSprings> mAnimHelperSpringsGroup;

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
    private AnimHelperSpringsGroup() {

    }

    /**
     * Create instance of a helper group.
     *
     * @param activity Target activity.
     */
    private AnimHelperSpringsGroup(Activity activity) {

        // Set activity.
        mActivity = activity;

        // Initialize helper group.
        mAnimHelperSpringsGroup = new ArrayList<AnimHelperSprings>();
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
    public static AnimHelperSpringsGroup from(Activity activity) {

        return new AnimHelperSpringsGroup(activity);
    }

    /**
     * Create a new helper for the group.
     *
     * @param tension Tension.
     * @param friction Friction.
     *
     * @return The instantiated helper.
     */
    public AnimHelperSprings createHelper(int tension, int friction) {

        // Create a new animation helper.
        AnimHelperSprings animHelperSprings = AnimHelperSprings.from(mActivity, tension, friction);

        // Add to group.
        mAnimHelperSpringsGroup.add(animHelperSprings);

        // Add a completion listener.
        animHelperSprings.addOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Increment completion count.
                mAnimCompleteCount++;

                // If all animations are complete.
                if (mAnimCompleteCount == mAnimHelperSpringsGroup.size()) {

                    if (mOnCompleteListener != null) {
                        mOnCompleteListener.run();
                    }
                }
            }
        });

        return animHelperSprings;
    }

    /**
     * Disable the animation helper group.
     */
    public void disable() {

        // Disable each animation helper.
        for (AnimHelperSprings animHelperSprings : mAnimHelperSpringsGroup) {

            animHelperSprings.disable();
        }
    }

    /**
     * Disables and removes all helpers, as
     * well as any associated callbacks.
     */
    public void removeHelpers() {

        // Disable existing helpers.
        disable();

        // Remove completion listener.
        mOnCompleteListener = null;

        // Remove spring helpers.
        mAnimHelperSpringsGroup.clear();
    }

    /**
     * Enable the animation group.
     *
     * @param start If specified, also start it.
     */
    public void enable(boolean start) {

        // Enable each animation helper.
        for (AnimHelperSprings animHelperSprings : mAnimHelperSpringsGroup) {

            animHelperSprings.enable();

            if (start) {

                // Reset the count.
                mAnimCompleteCount = 0;

                // Start after transition.
                animHelperSprings.start(true);
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
        for (AnimHelperSprings animHelperSprings : mAnimHelperSpringsGroup) {

            animHelperSprings.start(false);
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