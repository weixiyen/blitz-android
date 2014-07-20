package com.blitz.app.utilities.logging;

import android.util.Log;

/**
 * Created by mrkcsc on 7/20/14.
 */
public class LogHelper {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Logging tag.
    private static final String TAG = "Blitz";

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Simple logging shorthand.
     *
     * @param log Log statement.
     */
    public static void log(String log) {
        Log.i(TAG, log);
    }
}