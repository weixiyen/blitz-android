package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIObject;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.google.gson.JsonObject;

/**
 * Created by Miguel Gaeta on 6/26/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("unused")
public class ObjectModelPreferences extends ObjectModel {

    // region Member Variables
    // =============================================================================================

    // Current year.
    private int mCurrentYear;

    // Current draft week.
    private int mCurrentWeek;

    // Is a queue available.
    private boolean mQueueAvailable;

    // What queue are we in.
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

    /**
     * Sync user preferences.
     *
     * @param mActivity Context for loading/error dialogs.
     *
     * @param syncCallback Completion callback.
     */
    public void Sync(Activity mActivity, final SyncCallback syncCallback) {

        // Define operation, call onSync when complete.
        RestAPIOperation operation = new RestAPIOperation(mActivity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                // Fetch json result.
                JsonObject jsonObject = restAPIObject.getJsonObject();

                if (AppConfig.isProduction()) {

                    // Assign from result.
                    mCurrentYear        = jsonObject.get("current_year").getAsInt();
                    mCurrentWeek        = jsonObject.get("current_week").getAsInt();
                    mQueueAvailable     = jsonObject.get("queue_available").getAsBoolean();
                    mCurrentActiveQueue = jsonObject.get("current_active_queue").getAsString();

                } else {

                    // Assign test data.
                    mCurrentYear        = 2013;
                    mCurrentWeek        = 5 + (int)(Math.random() * ((10 - 5) + 1));
                    mQueueAvailable     = true;
                    mCurrentActiveQueue = "football_heads_up_draft_free";
                }

                // Sync successful.
                syncCallback.onSync();
            }
        };

        // Make api call.
        mRestAPI.preferences_get(RestAPICallback.create(operation));
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface SyncCallback {

        public void onSync();
    }

    // endregion
}