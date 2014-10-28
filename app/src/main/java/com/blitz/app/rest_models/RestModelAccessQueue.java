package com.blitz.app.rest_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.google.gson.JsonObject;

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
    public void sync(Activity activity, final Runnable callback) {

        RestAPICallback<JsonObject> operation =
                new RestAPICallback<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

                if (jsonObject != null) {

                    mAccessGranted = jsonObject.get("access_granted").getAsBoolean();

                    if (jsonObject.get("people_ahead") != null) {
                        mPeopleAhead = jsonObject.get("people_ahead").getAsInt();
                    }

                    if (jsonObject.get("people_behind") != null) {
                        mPeopleBehind = jsonObject.get("people_behind").getAsInt();
                    }
                }

                // Code redeemed.
                callback.run();
            }
        };

        // Make rest call for code.
        mRestAPI.access_queue_get(mDeviceId, operation);
    }

    // endregion
}