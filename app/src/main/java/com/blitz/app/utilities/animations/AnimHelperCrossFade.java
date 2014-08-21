package com.blitz.app.utilities.animations;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by mrkcsc on 8/20/14.
 */
public class AnimHelperCrossFade extends AnimHelper {

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Cross fade background resource.
     */
    @SuppressWarnings("unused,deprecation")
    public static void setBackgroundResource(View view, int resId) {

        TransitionDrawable td = getTransitionDrawable(view.getBackground(),
                view.getResources().getDrawable(resId));

        // Use SDK appropriate background call.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            view.setBackground(td);
        } else {
            view.setBackgroundDrawable(td);
        }

        // Start transition.
        td.startTransition(getConfigAnimTimeStandard(view.getContext()));
    }

    /**
     * Cross fade image resource.
     */
    @SuppressWarnings("unused")
    public static void setImageResource(ImageView imageView, int resId) {

        TransitionDrawable td = getTransitionDrawable(imageView.getDrawable(),
                imageView.getResources().getDrawable(resId));

        // Set the transition drawable.
        imageView.setImageDrawable(td);

        // Enable cross fade.
        td.startTransition(getConfigAnimTimeStandard(imageView.getContext()));
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Shorthand for making a transition drawable.
     *
     * @param drawable1 Origin drawable.
     * @param drawable2 Destination drawable.
     *
     * @return Instantiated transition drawable.
     */
    private static TransitionDrawable getTransitionDrawable(Drawable drawable1, Drawable drawable2) {

        // Create a transition from the current background to new one.
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {

                drawable1, drawable2
        });

        // Enable cross fade.
        transitionDrawable.setCrossFadeEnabled(true);

        return transitionDrawable;
    }
}
