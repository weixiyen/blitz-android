package com.blitz.app.utilities.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by mrkcsc on 8/21/14. Copyright 2014 Blitz Studios
 */
public class AnimHelperFade extends AnimHelper {

    // region Public Methods
    // =============================================================================================

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

        // Set the initial alpha.
        view.setAlpha(alphaFrom);

        // Create a new alpha animator on the view.
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", alphaFrom, alphaTo);

        // Set the default duration.
        alphaAnimator.setDuration(duration);

        // Set animation end callback.
        alphaAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                // Set final visibility.
                view.setVisibility(visibility);

                // Restore alpha.
                view.setAlpha(initialAlpha);
            }
        });

        alphaAnimator.start();
    }

    /**
     * Sets the visibility of a view but layers a
     * fade animation into it to make it
     * more appealing.
     *
     * @param view Target view.
     * @param visibility Target visibility.
     * @param forceTransition If set it will force the animation
     *                        even if going from same visibility state.
     */
    public static void setVisibility(final View view, final int visibility, boolean forceTransition) {

        setVisibility(view, visibility,
                getConfigAnimTimeStandard(view.getContext()), forceTransition);
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
    public static void setVisibility(final View view, final int visibility, int duration) {

        setVisibility(view, visibility, duration, false);
    }

    /**
     * By default do not force transition.
     */
    @SuppressWarnings("unused")
    public static void setVisibility(final View view, final int visibility) {

        setVisibility(view, visibility,
                getConfigAnimTimeStandard(view.getContext()));
    }

    // endregion
}