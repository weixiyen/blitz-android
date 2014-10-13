package com.blitz.app.rest_models;

import android.app.Activity;

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

    // region REST Methods
    // =============================================================================================

    /**
     * Fetch the current users preferences.
     */
    public static void sync(Activity activity, final RestModelCallback<RestModelPreferences> callback) {

        RestAPICallback<RestModelPreferences> operation =
                new RestAPICallback<RestModelPreferences>(activity) {

                    @Override
                    public void success(RestModelPreferences jsonObject) {

                        if (callback != null) {
                            callback.onSuccess(jsonObject);
                        }
                    }
                };

        // Make api call.
        mRestAPI.preferences_get(operation);
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Fetch active queue (time based).
     *
     * @return Active queue.
     */
    @SuppressWarnings("unused")
    public String getCurrentActiveQueue() {

        return mCurrentActiveQueue;
    }

    /**
     * Get current drafting week.
     *
     * @return Current drafting week.
     */
    @SuppressWarnings("unused")
    public int getCurrentWeek() {

        return mCurrentWeek;
    }

    /**
     * Get current drafting year.
     *
     * @return Current drafting year.
     */
    @SuppressWarnings("unused")
    public int getCurrentYear() {

        return mCurrentYear;
    }

    /**
     * Is the queue available.
     *
     * @return Yes or no.
     */
    @SuppressWarnings("unused")
    public boolean getIsQueueAvailable() {

        return mQueueAvailable;
    }

    // endregion
}