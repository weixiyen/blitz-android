package com.blitz.app.utilities.animations;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.blitz.app.utilities.blitz.BlitzDelay;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;

/**
 * Created by mrkcsc on 7/29/14. Copyright 2014 Blitz Studios
 */
public class AnimHelperSprings extends AnimHelper {

    // region Member Variables
    // =============================================================================================

    // Parent activity.
    private Activity mActivity;

    // Spring animator.
    private Spring mSpring;

    // Views this helper controls.
    private ArrayList<AnimHelperSpringsView> mViews;

    // Are we initialized.
    private boolean mInitialized;

    // Are we waiting on initialization.
    private boolean mEnablePendingInitialization;
    private boolean mStartPendingInitialization;

    // Window dimensions.
    private int mWindowWidth;
    private int mWindowHeight;

    // Callback for when complete.
    private ArrayList<Runnable> mOnCompleteListeners;

    // endregion

    // region Constructors
    // =============================================================================================

    /**
     * Unused private constructor.
     */
    @SuppressWarnings("unused")
    private AnimHelperSprings() {

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
    private AnimHelperSprings(Activity activity, int tension, int friction) {

        // First configure.
        configure(activity, tension, friction);

        // Initialize.
        initialize();
    }

    // endregion

    // region Public Methods
    // =============================================================================================

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
    @SuppressWarnings("unused")
    public static AnimHelperSprings from(Activity activity, int tension, int friction) {

        return new AnimHelperSprings(activity, tension, friction);
    }

    /**
     * Add a animation helper view.
     *
     * @param helperView Helper view.
     *
     * @return Current instance.
     */
    @SuppressWarnings("unused")
    public AnimHelperSprings addHelperView(AnimHelperSpringsView helperView) {

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
    public AnimHelperSprings addHelperViews(ArrayList<AnimHelperSpringsView> helperViews) {

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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public void enable() {

        if (mInitialized) {

            // Try to initialize the views and
            // set their initial positions.
            tryInitializeViews(mViews);

            // Add a spring listener.
            mSpring.addListener(new SpringListener() {

                /**
                 * Called when the spring value updates.
                 *
                 * @param spring Spring object.
                 */
                @Override
                public void onSpringUpdate(Spring spring) {

                    // Animate each view on update.
                    for (AnimHelperSpringsView view : mViews) {

                        view.animateWithSpring(mSpring);
                    }
                }

                /**
                 * Called when spring is now at rest (1.0 value).
                 *
                 * @param spring Spring object.
                 */
                @Override
                public void onSpringAtRest(Spring spring) {

                    if (mOnCompleteListeners != null) {

                        // Run any associated callbacks.
                        for (Runnable runnable : mOnCompleteListeners) {

                            runnable.run();
                        }
                    }
                }

                @Override
                public void onSpringActivate      (Spring spring) { }

                @Override
                public void onSpringEndStateChange(Spring spring) { }
            });
        } else {

            // Delay the enable until initialized.
            mEnablePendingInitialization = true;
        }
    }

    /**
     * Start spring animation.
     */
    @SuppressWarnings("unused")
    public void start(boolean afterScreenTransition) {
        if (mInitialized) {

            int delay = 0;

            // At least transition time.
            if (delay < getConfigAnimTimeStandard()) {
                delay = getConfigAnimTimeStandard() + 100;
            }

            // Action that starts animation.
            Runnable startAnimation =  new Runnable() {

                @Override
                public void run() {

                    // Set new end value to start.
                    mSpring.setEndValue(1);
                }
            };

            if (afterScreenTransition) {

                // Start spring on delay.
                BlitzDelay.postDelayed(startAnimation, delay);
            } else {
                startAnimation.run();
            }
        } else {

            // Start when initialized.
            mStartPendingInitialization = true;
        }
    }

    /**
     * By default start animations after
     * the screen transition.
     */
    @SuppressWarnings("unused")
    public void start() {
        start(true);
    }

    /**
     * Set a listener that whens when animation is done.
     *
     * @param onCompleteListener Callback to run.
     */
    @SuppressWarnings("unused")
    public void addOnCompleteListener(Runnable onCompleteListener) {

        if (mOnCompleteListeners == null) {
            mOnCompleteListeners = new ArrayList<Runnable>();
        }

        mOnCompleteListeners.add(onCompleteListener);
    }

    // endregion

    // region Private Methods
    // =============================================================================================

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
        mViews = new ArrayList<AnimHelperSpringsView>();
    }

    /**
     * Initialize animation helper object. Should be
     * called on activity creation.
     */
    @SuppressWarnings("deprecation")
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

                        // Start if pending.
                        if (mStartPendingInitialization) {
                            mStartPendingInitialization = false;

                            start();
                        }

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });

        // Request a layout.
        rootView.requestLayout();
    }

    /**
     * Initialize a collection of helper views.  Attempts
     * to set their coordinate information and then
     * initializes their animation state.
     *
     * @param helperViews Helper views list.
     */
    private void tryInitializeViews(ArrayList<AnimHelperSpringsView> helperViews) {

        if (helperViews != null && mInitialized) {

            for (AnimHelperSpringsView view : helperViews) {

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
    private void tryInitializeView(AnimHelperSpringsView helperView) {

        if (helperView != null && mInitialized) {
            helperView.setCoordinates(mWindowWidth, mWindowHeight);
        }
    }

    // endregion
}