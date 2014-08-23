package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.blitz.app.utilities.rest.RestAPIObject;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 8/9/14.
 */
public class ObjectModelAccessQueue extends ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    private String mDeviceId;

    private int mPeopleAhead;
    private int mPeopleBehind;

    private boolean mAccessGranted;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

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

        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                // Fetch resulting object.
                JsonObject jsonObject = restAPIObject.getJsonObject();

                if (jsonObject != null) {

                    mAccessGranted = jsonObject.get("access_granted").getAsBoolean();
                    mPeopleAhead   = jsonObject.get("people_ahead").getAsInt();
                    mPeopleBehind  = jsonObject.get("people_behind").getAsInt();
                }

                // Code redeemed.
                callback.run();
            }
        };

        // Make rest call for code.
        RestAPIClient.getAPI().access_queue_get(mDeviceId, RestAPICallback.create(operation));
    }
}
