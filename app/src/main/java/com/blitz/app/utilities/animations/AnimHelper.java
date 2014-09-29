package com.blitz.app.utilities.animations;

import android.content.Context;

import com.blitz.app.R;

/**
 * Created by mrkcsc on 8/20/14. Copyright 2014 Blitz Studios
 */
public class AnimHelper {

    // region Protected Methods
    // =============================================================================================

    /**
     * Fetch the standard animation time, used
     * throughout animation helper classes.
     *
     * @param context Context is needed.
     *
     * @return Standard animation time.
     */
    public static int getConfigAnimTimeStandard(Context context) {

        // Time to transition an activity.
        return context.getResources().getInteger(R.integer.config_screen_translation_time);
    }

    // endregion
}