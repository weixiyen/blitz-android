package com.blitz.app.utilities.animations;

import android.content.Context;

import com.blitz.app.R;

/**
 * Created by mrkcsc on 8/20/14.
 */
public class AnimHelper {

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * Fetch the standard animation time, used
     * throughout animation helper classes.
     *
     * @param context Context is needed.
     *
     * @return Standard animation time.
     */
    protected int getConfigAnimTimeStandard(Context context) {

        // Time to transition an activity.
        return context.getResources().getInteger(R.integer.config_screen_translation_time);
    }
}