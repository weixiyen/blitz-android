package com.blitz.app.utilities.animations;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.blitz.app.R;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;

/**
 * Created by mrkcsc on 7/29/14.
 */
public class AnimationHelper {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Parent activity.
    private Activity mActivity;

    // Spring animator.
    private Spring mSpring;

    // Views this helper controls.
    private ArrayList<AnimationHelperView> mViews;

    // Are we initialized.
    private boolean mInitialized;

    // Are we waiting on initialization.
    private boolean mEnablePendingInitialization;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Initialize an animation helper class.
     *
     * @param tension Associated tension.
     * @param friction Associated friction.
     */
    public AnimationHelper(int tension, int friction) {

        // Create a spring config with provided parameters.
        SpringConfig springConfig = SpringConfig
                .fromOrigamiTensionAndFriction(tension, friction);

        mSpring = SpringSystem
                .create()
                .createSpring()
                .setSpringConfig(springConfig)
                .setAtRest();

        // Initialize list of animation views.
        mViews = new ArrayList<AnimationHelperView>();
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================


    /**
     * Initialize animation helper object. Should be
     * called on activity creation.
     *
     * @param activity Target activity. We must wait until
     *                 the views are rendered before we can
     *                 initialize them.
     */
    public void initialize(Activity activity) {

        // Save activity.
        mActivity = activity;

        // Fetch associated root view.
        final View rootView = ((FrameLayout)mActivity.findViewById
                (android.R.id.content)).getChildAt(0);

        // Wait for layout pass to finish before setting coordinate.
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(

                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        // Now initialized.
                        mInitialized = true;

                        // Enable if pending.
                        if (mEnablePendingInitialization) {
                            mEnablePendingInitialization = false;

                            enable();
                        }

                        // Try to initialize.
                        tryInitializeViews(mViews);

                        // Remove the listener.
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @SuppressWarnings("unused")
    public void addHelperView(AnimationHelperView helperView) {

        // Try initialize.
        tryInitializeView(helperView);

        // Add.
        mViews.add(helperView);
    }

    @SuppressWarnings("unused")
    public void addHelperViews(ArrayList<AnimationHelperView> helperViews) {

        // Try initialize.
        tryInitializeViews(helperViews);

        // Add.
        mViews.addAll(helperViews);
    }

    public void disable() {

        // Clear listeners and reset.
        mSpring.removeAllListeners();
        mSpring.setEndValue(0);
    }


    public void enable() {

        if (!mInitialized) {

            // Delay the enable until initialized.
            mEnablePendingInitialization = true;

            return;
        }

        // Time to transition an activity.
        int screenTransitionTime = mActivity.getResources()
                .getInteger(R.integer.config_screen_translation_time);

        int delay = 0;

        // At least transition time.
        if (delay < screenTransitionTime) {
            delay = screenTransitionTime + 100;
        }

        // Try to initialize the views and
        // set their initial positions.
        tryInitializeViews(mViews);

        // Add a spring listener.
        mSpring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {

                // Animate each view on update.
                for (AnimationHelperView view : mViews) {

                    view.translateY(mSpring);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // Set new end value to start.
                mSpring.setEndValue(1);
            }
        }, delay);
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Initialize a collection of helper views.  Attempts
     * to set their coordinate information and then
     * initializes their animation state.
     *
     * @param helperViews Helper views list.
     */
    private void tryInitializeViews(ArrayList<AnimationHelperView> helperViews) {

        if (helperViews != null && mInitialized) {

            for (AnimationHelperView view : helperViews) {

                // Try initialize each view.
                tryInitializeView(view);
            }
        }
    }

    /**
     * Initialize a helper views.  Attempts
     * to set coordinate information and then
     * initializes the animation state.
     *
     * @param helperView Helper view.
     */
    private void tryInitializeView(AnimationHelperView helperView) {

        if (helperView != null && mInitialized) {
            helperView.setCoordinates();
        }
    }
}