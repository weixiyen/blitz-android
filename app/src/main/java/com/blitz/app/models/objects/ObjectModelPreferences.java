package com.blitz.app.models.objects;

import android.app.Activity;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectPreference;
import com.blitz.app.utilities.app.AppConfig;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
@SuppressWarnings("unused")
public class ObjectModelPreferences extends ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Current year.
    private int mCurrentYear;

    // Current draft week.
    private int mCurrentWeek;

    // Is a queue available.
    private boolean mQueueAvailable;

    // What queue are we in.
    private String mCurrentActiveQueue;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

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
            public void success() {

                // Fetch json result.
                JsonObjectPreference jsonObject = getJsonObject(JsonObjectPreference.class);

                if (AppConfig.IS_PRODUCTION) {

                    // Assign from result.
                    mCurrentYear        = jsonObject.current_year;
                    mCurrentWeek        = jsonObject.current_week;
                    mQueueAvailable     = jsonObject.queue_available;
                    mCurrentActiveQueue = jsonObject.current_active_queue;

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
        RestAPIClient.getAPI().preferences
                (new RestAPICallback<JsonObjectPreference>(mRestApiObject, operation));
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    public interface SyncCallback {

        public void onSync();
    }
}