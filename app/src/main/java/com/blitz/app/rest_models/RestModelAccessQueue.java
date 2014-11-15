package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by mrkcsc on 8/9/14. Copyright 2014 Blitz Studios
 */
public class RestModelAccessQueue extends RestModel {

    // region Member Variables
    // ============================================================================================================

    private String mDeviceId;

    private int mPeopleAhead;
    private int mPeopleBehind;

    private boolean mAccessGranted;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Set the device id.
     *
     * @param deviceId Device id.
     */
    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    /**
     * People ahead in queue.
     *
     * @return People ahead.
     */
    public int getPeopleAhead() {
        return mPeopleAhead;
    }

    /**
     * People behind in queue.
     *
     * @return People behind.
     */
    public int getPeopleBehind() {
        return mPeopleBehind;
    }

    /**
     * Get access granted status.
     *
     * @return Is access granted.
     */
    public boolean getAccessGranted() {
        return mAccessGranted;
    }

    /**
     * Synchronize with the access queue
     * for this device id.
     *
     * @param activity Activity for UI changes.
     * @param callback Callback runnable.
     */
    @SuppressWarnings("unused")
    public void sync(Activity activity, @NonNull Runnable callback) {

        // Make rest call for code.
        mRestAPI.access_queue_get(mDeviceId, new RestAPICallback<>(activity, result ->  {

            if (result != null) {

                mAccessGranted = result.get("access_granted").getAsBoolean();

                if (result.get("people_ahead") != null) {
                    mPeopleAhead = result.get("people_ahead").getAsInt();
                }

                if (result.get("people_behind") != null) {
                    mPeopleBehind = result.get("people_behind").getAsInt();
                }
            }

            // Code redeemed.
            callback.run();

        }, null));
    }

    // endregion
}