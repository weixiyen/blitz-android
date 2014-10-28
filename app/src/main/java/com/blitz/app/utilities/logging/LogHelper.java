package com.blitz.app.utilities.logging;

import android.util.Log;

/**
 * Created by mrkcsc on 7/20/14. Copyright 2014 Blitz Studios
 */
public class LogHelper {

    // region Member Variables
    // ============================================================================================================

    // Logging tag.
    private static final String TAG = "Blitz";

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Simple logging shorthand.
     *
     * @param log Log statement.
     */
    public static void log(String log) {
        Log.e(TAG, log);
    }

    // endregion
}