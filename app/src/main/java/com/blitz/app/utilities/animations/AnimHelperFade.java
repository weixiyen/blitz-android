package com.blitz.app.utilities.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by mrkcsc on 8/21/14. Copyright 2014 Blitz Studios
 */
public class AnimHelperFade extends AnimHelper {

    // region Public Methods
    // =============================================================================================

    /**
     * By default do not force transition.
     */
    @SuppressWarnings("unused")
    public static void setVisibility(final View view, final int visibility) {

        setVisibility(view, visibility,
                getConfigAnimTimeStandard());
    }

    /**
     * Sets the visibility of a view but layers a
     * fade animation into it to make it
     * more appealing.
     *
     * @param view Target view.
     * @param visibility Target visibility.
     * @param callback Completion callback.
     */
    @SuppressWarnings("unused")
    public static void setVisibility(final View view, final int visibility, Runnable callback) {

        setVisibility(view, visibility, getConfigAnimTimeStandard(), false, false, callback);
    }

    /**
     * Sets the visibility of a view but layers a
     * fade animation into it to make it
     * more appealing.
     *
     * @param view Target view.
     * @param visibility Target visibility.
     */
    @SuppressWarnings("unused")
    public static void setVisibility(final View view, final int visibility,
                                     int duration) {

        setVisibility(view, visibility, duration, false);
    }

    /**
     * Sets the visibility of a view but layers a
     * fade animation into it to make it
     * more appealing.
     *
     * @param view Target view.
     * @param visibility Target visibility.
     * @param duration Transition time.
     * @param forceTransition If set it will force the animation
     *                        even if going from same visibility state.
     */
    @SuppressWarnings("unused")
    public static void setVisibility(final View view, final int visibility,
                                     int duration, boolean forceTransition)  {

        setVisibility(view, visibility, duration, forceTransition, false);
    }

    /**
     * Sets the visibility of a view but layers a
     * fade animation into it to make it
     * more appealing.
     *
     * @param view Target view.
     * @param visibility Target visibility.
     * @param duration Transition time.
     * @param forceTransition If set it will force the animation
     *                        even if going from same visibility state.
     */
    @SuppressWarnings("unused")
    public static void setVisibility(View view, int visibility, int duration,
                                     boolean forceTransition, boolean preserveAlpha) {

        setVisibility(view, visibility, duration, forceTransition, preserveAlpha, null);
    }

     /**
      * Sets the visibility of a view but layers a
      * fade animation into it to make it
      * more appealing.
      *
      * @param view Target view.
      * @param visibility Target visibility.
      * @param duration Transition time.
      * @param forceTransition If set it will force the animation
      *                        even if going from same visibility state.
      * @param callback Completion callback.
      */
    @SuppressWarnings("unused")
    public static void setVisibility(final View view, final int visibility,
                                     int duration, boolean forceTransition,
                                     final boolean preserveAlpha, final Runnable callback) {

        // Don't animate if already set.
        if (view.getVisibility() == visibility && !forceTransition) {

            return;
        }

        // Fetch initial alpha state.
        final float initialAlpha = view.getAlpha();

        // Ensure view is visible.
        view.setVisibility(View.VISIBLE);

        float alphaFrom;
        float alphaTo;

        // Initialize alpha.
        switch (visibility) {

            case View.VISIBLE:
                alphaFrom = 0.0f;
                alphaTo = 1.0f;

                break;
            case View.INVISIBLE:
            case View.GONE:
                alphaFrom = 1.0f;
                alphaTo = 0.0f;

                break;

            default:

                // Developer error if this happens.
                throw new RuntimeException("Anim helper, invalid visibility flag provided.");
        }

        // Set the alpha animation.
        setAlpha(view, alphaFrom, alphaTo, duration, new Runnable() {

            @Override
            public void run() {

                // Set final visibility.
                view.setVisibility(visibility);

                if (preserveAlpha) {

                    // Restore alpha.
                    view.setAlpha(initialAlpha);
                }

                if (callback != null) {
                    callback.run();
                }
            }
        });
    }

    /**
     * Set alpha of a view with animation.
     *
     * @param view Target view.
     * @param alphaFrom Target alpha from.
     * @param alphaTo Target alpha to.
     */
    @SuppressWarnings("unused")
    public static void setAlpha(final View view, final float alphaFrom, final float alphaTo) {

        setAlpha(view, alphaFrom, alphaTo, getConfigAnimTimeStandard());
    }

    /**
     * Set alpha of a view with animation.
     *
     * @param view Target view.
     * @param alphaFrom Target alpha from.
     * @param alphaTo Target alpha to.
     * @param duration Transition time.
     */
    @SuppressWarnings("unused")
    public static void setAlpha(final View view, final float alphaFrom, final float alphaTo,
                                int duration) {

        setAlpha(view, alphaFrom, alphaTo, duration, null);
    }

    /**
     * Set alpha of a view with animation.
     *
     * @param view Target view.
     * @param alphaFrom Target alpha from.
     * @param alphaTo Target alpha to.
     * @param duration Transition time.
     * @param callback Completion callback.
     */
    @SuppressWarnings("unused")
    public static void setAlpha(final View view, final float alphaFrom, final float alphaTo,
                                int duration, final Runnable callback) {

        // Set the initial alpha.
        view.setAlpha(alphaFrom);

        // Create a new alpha animator on the view.
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", alphaFrom, alphaTo);

        // Set the default duration.
        alphaAnimator.setDuration(duration);

        // Set ease in and ease out interpolator.
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        // Set animation end callback.
        alphaAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (callback != null) {
                    callback.run();
                }
            }
        });

        alphaAnimator.start();
    }

    // endregion
}