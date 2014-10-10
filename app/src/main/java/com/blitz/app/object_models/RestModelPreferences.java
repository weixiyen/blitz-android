package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.google.gson.JsonObject;

/**
 * Created by Miguel Gaeta on 6/26/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("unused")
public class RestModelPreferences extends RestModel {

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
     * @param activity Context for loading/error dialogs.
     *
     * @param syncCallback Completion callback.
     */
    public void Sync(Activity activity, final SyncCallback syncCallback) {

        // Define operation, call onSync when complete.
        RestAPICallback<JsonObject> operation =
                new RestAPICallback<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

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
        mRestAPI.preferences_get(operation);
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface SyncCallback {

        public void onSync();
    }

    // endregion
}