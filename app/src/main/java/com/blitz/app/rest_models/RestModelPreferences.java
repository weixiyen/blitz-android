package com.blitz.app.rest_models;

import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Miguel Gaeta on 6/26/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("unused")
public class RestModelPreferences extends RestModel {

    // region Member Variables
    // =============================================================================================

    // Current year.
    @SerializedName("current_year")
    private int mCurrentYear;

    // Current draft week.
    @SerializedName("current_week")
    private int mCurrentWeek;

    // Is a queue available.
    @SerializedName("queue_available")
    private boolean mQueueAvailable;

    // What queue are we in.
    @SerializedName("current_active_queue")
    private String mCurrentActiveQueue;

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Fetch active queue (time based).
     *
     * @return Active queue.
     */
    public String currentActiveQueue() {
        return mCurrentActiveQueue;
    }

    public int getCurrentWeek() {
        return mCurrentWeek;
    }

    public int getCurrentYear() {
        return mCurrentYear;
    }

    /**
     * Sync user preferences.
     */
    public static void sync(RestAPICallback<RestModelPreferences> callback) {

        // Make api call.
        mRestAPI.preferences_get(callback);
    }

    public static RestModelPreferences defaultPreferences() {

        RestModelPreferences preferences = new RestModelPreferences();

        if(AppConfig.isProduction()) {
            preferences.mCurrentYear = 2014;
            preferences.mCurrentWeek = 1;
        } else {
            preferences.mCurrentYear = 2013;
            preferences.mCurrentWeek = 5 + (int)(Math.random() * ((10 - 5) + 1));
        }

        preferences.mQueueAvailable = true;
        preferences.mCurrentActiveQueue = "football_heads_up_draft_free";

        return preferences;
    }
}