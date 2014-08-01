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
public class AnimHelper {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Parent activity.
    private Activity mActivity;

    // Spring animator.
    private Spring mSpring;

    // Views this helper controls.
    private ArrayList<AnimHelperView> mViews;

    // Are we initialized.
    private boolean mInitialized;

    // Are we waiting on initialization.
    private boolean mEnablePendingInitialization;

    // Window dimensions.
    private int mWindowWidth;
    private int mWindowHeight;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Unused private constructor.
     */
    @SuppressWarnings("unused")
    private AnimHelper() {

    }

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
    private AnimHelper(Activity activity, int tension, int friction) {

        // First configure.
        configure(activity, tension, friction);

        // Initialize.
        initialize();
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Create a new instance of animation helper from
     * common parameters.
     *
     * @param activity Target activity.
     * @param tension Desired tension.
     * @param friction Associated friction.
     *
     * @return New instance.
     */
    public static AnimHelper from(Activity activity, int tension, int friction) {

        return new AnimHelper(activity, tension, friction);
    }

    /**
     * Add a animation helper view.
     *
     * @param helperView Helper view.
     *
     * @return Current instance.
     */
    @SuppressWarnings("unused")
    public AnimHelper addHelperView(AnimHelperView helperView) {

        // Try initialize.
        tryInitializeView(helperView);

        // Add.
        mViews.add(helperView);

        return this;
    }

    /**
     * Add a list of animation helper views.
     *
     * @param helperViews Helper views list.
     *
     * @return Current instance.
     */
    @SuppressWarnings("unused")
    public AnimHelper addHelperViews(ArrayList<AnimHelperView> helperViews) {

        // Try initialize.
        tryInitializeViews(helperViews);

        // Add.
        mViews.addAll(helperViews);

        return this;
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
                for (AnimHelperView view : mViews) {

                    view.animateWithSpring(mSpring);
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
        mViews = new ArrayList<AnimHelperView>();
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

                        // Set window dimensions.
                        mWindowWidth  = rootView.getWidth();
                        mWindowHeight = rootView.getHeight();

                        // Enable if pending.
                        if (mEnablePendingInitialization) {
                            mEnablePendingInitialization = false;

                            enable();
                        }

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
    private void tryInitializeViews(ArrayList<AnimHelperView> helperViews) {

        if (helperViews != null && mInitialized) {

            for (AnimHelperView view : helperViews) {

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
    private void tryInitializeView(AnimHelperView helperView) {

        if (helperView != null && mInitialized) {
            helperView.setCoordinates(mWindowWidth, mWindowHeight);
        }
    }
}