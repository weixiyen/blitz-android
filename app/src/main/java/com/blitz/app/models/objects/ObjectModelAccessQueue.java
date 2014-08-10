package com.blitz.app.models.objects;

import android.app.Activity;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectAccessQueue;

/**
 * Created by mrkcsc on 8/9/14.
 */
public class ObjectModelAccessQueue extends ObjectModel {

    //==============================================================================================
    // Public Methods
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
            public void success() {

                // Fetch resulting object.
                JsonObjectAccessQueue jsonObject = getJsonObject(JsonObjectAccessQueue.class);

                if (jsonObject != null) {

                    mAccessGranted = jsonObject.access_granted;
                    mPeopleAhead   = jsonObject.people_ahead;
                    mPeopleBehind  = jsonObject.people_behind;
                }

                // Code redeemed.
                callback.run();
            }
        };

        // Make rest call for code.
        RestAPIClient.getAPI().access_queue(mDeviceId,
                new RestAPICallback<JsonObjectAccessQueue>(mRestApiObject, operation));
    }
}
