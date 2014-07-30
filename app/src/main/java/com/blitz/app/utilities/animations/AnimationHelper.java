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
     *
     * @param activity Target activity. We must wait until
     *                 the views are rendered before we can
     *                 initialize them.
     */
    public AnimationHelper(Activity activity, int tension, int friction) {

        // First configure.
        configure(activity, tension, friction);

        // Initialize.
        initialize();
    }

    /**
     * Initialize an animation helper class
     * with some default values.
     *
     * @param activity Target activity. We must wait until
     *                 the views are rendered before we can
     *                 initialize them.
     */
    public AnimationHelper(Activity activity) {

        // Default tension and friction.
        new AnimationHelper(activity, 40, 1);
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Add a animation helper view.
     *
     * @param helperView Helper view.
     */
    @SuppressWarnings("unused")
    public void addHelperView(AnimationHelperView helperView) {

        // Try initialize.
        tryInitializeView(helperView);

        // Add.
        mViews.add(helperView);
    }

    /**
     * Add a list of animation helper views.
     *
     * @param helperViews Helper views list.
     */
    @SuppressWarnings("unused")
    public void addHelperViews(ArrayList<AnimationHelperView> helperViews) {

        // Try initialize.
        tryInitializeViews(helperViews);

        // Add.
        mViews.addAll(helperViews);
    }

    /**
     * Disable the animation helper, also
     * resets the animation state.
     */
    public void disable() {

        // Clear listeners and reset.
        mSpring.removeAllListeners();
        mSpring.setEndValue(0);
    }

    /**
     * Enable the animation helper.  Initializes
     * and then plays animations.  Must wait
     * until activity has rendered all views
     * before starting.
     */
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
     * Configure this helper.
     *
     * @param activity Target activity.
     * @param tension Desired tension.
     * @param friction Desired friction.
     */
    private void configure(Activity activity, int tension, int friction) {

        // Save activity.
        mActivity = activity;

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

    /**
     * Initialize animation helper object. Should be
     * called on activity creation.
     */
    private void initialize() {

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