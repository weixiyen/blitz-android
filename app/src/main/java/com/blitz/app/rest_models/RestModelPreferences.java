package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.utilities.app.AppConfig;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Miguel Gaeta on 6/26/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelPreferences extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("current_year") @Getter         private int currentYear;
    @SerializedName("current_week") @Getter         private int currentWeek;
    @SerializedName("queue_available") @Getter      private boolean queueAvailable;
    @SerializedName("current_active_queue") @Getter private String currentActiveQueue;

    // endregion

    // region REST Methods
    // ============================================================================================================

    /**
     * Fetch the current users preferences.
     */
    @SuppressWarnings("unused")
    public static void sync(Activity activity, @NonNull RestResult<RestModelPreferences> callback) {

        // Make api call.
        restAPI.preferences_get(new RestAPICallback<>(activity, result -> {

            if (AppConfig.isDraftSimulationEnabled()) {

                // Drafts always on.
                result.queueAvailable = true;

                // Testing queue.
                result.currentActiveQueue = "football_heads_up_draft_free";
            }

            callback.onSuccess(result);

        }, null));
    }

    // endregion
}