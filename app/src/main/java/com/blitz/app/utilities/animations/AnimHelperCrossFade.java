package com.blitz.app.utilities.animations;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.image.BlitzImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by mrkcsc on 8/20/14. Copyright 2014 Blitz Studios
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

        // Set the transition.
        setTransitionDrawable(imageView.getDrawable(),
                imageView.getResources().getDrawable(resId), imageView);
    }

    /**
     * Cross fade image resource, except it is
     * a remote URL.
     *
     * @param imageView Target image view.
     * @param imageUrl Target image url.
     */
    @SuppressWarnings("unused")
    public static void setImageUrl(final BlitzImageView imageView, final String imageUrl) {

        // Prepend the CDN url.
        String fullImageUrl = AppConfig.getCDNUrl() + imageUrl;

        Target target = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                // Turn our bitmap into a drawable.
                BitmapDrawable drawableTo = new BitmapDrawable(imageView.getResources(), bitmap);

                // Set the transition.
                setTransitionDrawable(imageView.getDrawable(), drawableTo, imageView);

                // Set url for caching purposes.
                imageView.setImageUrl(imageUrl, null, true);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };

        // Load the image url and do cross fade on completion.
        Picasso.with(imageView.getContext()).load(fullImageUrl).into(target);
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Creates and sets a cross fade transition drawable
     * into an image view.
     *
     * @param drawableFrom Drawable from.
     * @param drawableTo Drawable to.
     *
     * @param targetImageView Target image view.
     */
    private static void setTransitionDrawable(Drawable drawableFrom,
                                              Drawable drawableTo, ImageView targetImageView) {

        // Create transition drawable.
        TransitionDrawable td = getTransitionDrawable(drawableFrom, drawableTo);

        // Set the transition drawable.
        targetImageView.setImageDrawable(td);

        // Enable cross fade.
        td.startTransition(getConfigAnimTimeStandard(targetImageView.getContext()));
    }

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
