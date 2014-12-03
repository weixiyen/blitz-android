package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by mrkcsc on 8/9/14. Copyright 2014 Blitz Studios
 */
public class RestModelAccessQueue extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SuppressWarnings("unused") @SerializedName("people_ahead") @Getter
    private int peopleAhead;
    @SuppressWarnings("unused") @SerializedName("people_behind") @Getter
    private int peopleBehind;

    @SuppressWarnings("unused") @SerializedName("access_granted") @Getter
    private boolean accessGranted;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Synchronize with the access queue
     * for this device id.
     *
     * @param activity Activity for UI changes.
     * @param callback Callback runnable.
     */
    @SuppressWarnings("unused")
    public static void sync(Activity activity,
                            @NonNull String deviceId,
                            @NonNull RestResult<RestModelAccessQueue> callback) {

        // Make rest call for code.
        restAPI.access_queue_get(deviceId, new RestAPICallback<>(activity,
                callback::onSuccess, null));
    }

    // endregion
}