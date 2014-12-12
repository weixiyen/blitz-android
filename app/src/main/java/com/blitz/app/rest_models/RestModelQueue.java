package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.libraries.general.gson.MGGson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by mrkcsc on 7/20/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelQueue extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("success") @Getter private boolean success;

    // endregion

    // region REST Calls
    // ============================================================================================================

    /**
     * Add user to draft queue.  Requires a draft
     * key to be set.
     */
    public static void queueUp(Activity activity,
                               @NonNull String draftKey,
                               @NonNull RestResult<RestModelQueue> callback) {

        // Construct POST body.
        JsonObject body = MGGson.addProperty("draft_key", draftKey);

        // Make api call.
        restAPI.queue_post(body, new RestAPICallback<>(activity,
                callback::onSuccess, null));
    }

    /**
     * Remove user from the draft queue.  Requires a
     * draft key to be set.
     */
    public static void leaveQueue(Activity activity,
                                  @NonNull String draftKey,
                                  @NonNull RestResult<RestModelQueue> callback) {

        // Make api call.
        restAPI.queue_delete(draftKey, new RestAPICallback<>(activity,
                callback::onSuccess, null));
    }

    /**
     * Confirm user match in the draft queue. Requires
     * a draft key to be set.
     */
    public static void confirmQueue(Activity activity,
                             @NonNull String draftKey,
                             @NonNull RestResult<RestModelQueue> callback) {

        // Construct PUT body.
        JsonObject body = MGGson.addProperty("draft_key", draftKey);

        // Make api call.
        restAPI.queue_put(body, new RestAPICallback<>(activity,
                callback::onSuccess, null));
    }

    // endregion
}