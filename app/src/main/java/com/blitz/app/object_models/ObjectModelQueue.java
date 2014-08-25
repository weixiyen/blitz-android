package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.blitz.app.utilities.rest.RestAPIObject;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/20/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelQueue {

    // region Member Variables
    // =============================================================================================

    // Current active draft key,
    // static to preserve state.
    private static String mDraftKey;

    // Preferences model used to sync.
    private ObjectModelPreferences mModelPreferences;

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Add user to draft queue.  Requires a draft
     * key to be set.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void queueUp(final Activity activity, final Runnable callback) {

        if (mModelPreferences == null) {
            mModelPreferences = new ObjectModelPreferences();
        }

        // First sync preferences to get the active draft key.
        mModelPreferences.Sync(activity, new ObjectModelPreferences.SyncCallback() {

            @Override
            public void onSync() {

                // Set draft key to the active queue.
                mDraftKey =  mModelPreferences.currentActiveQueue();

                // Define operation, call on queue up when complete.
                RestAPIOperation operation = new RestAPIOperation(activity) {

                    @Override
                    public void success(RestAPIObject restAPIObject) {

                        // Now in queue.
                        if (callback != null) {
                            callback.run();
                        }
                    }
                };

                // Construct POST body.
                JsonObject body = new JsonObject();
                           body.addProperty("draft_key", mDraftKey);

                // Make api call.
                RestAPIClient.getAPI().queue_post(body, RestAPICallback.create(operation));
            }
        });
    }

    /**
     * Remove user from the draft queue.  Requires a
     * draft key to be set.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void leaveQueue(Activity activity, final Runnable callback) {

        if (mDraftKey == null) {
            return;
        }

        // Operation callbacks.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                // Now left queue.
                if (callback != null) {
                    callback.run();
                }
            }
        };

        // Make api call.
        RestAPIClient.getAPI().queue_delete
                (mDraftKey, RestAPICallback.create(operation));
    }

    /**
     * Confirm user match in the draft queue. Requires
     * a draft key to be set.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void confirmQueue(Activity activity, final Runnable callback) {

        if (mDraftKey == null) {
            return;
        }

        // Operation callbacks.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                // Now confirmed queue.
                if (callback != null) {
                    callback.run();
                }
            }
        };

        // Construct PUT body.
        JsonObject body = new JsonObject();
                   body.addProperty("draft_key", mDraftKey);

        // Make api call.
        RestAPIClient.getAPI().queue_put
                (body, RestAPICallback.create(operation));
    }

    // endregion
}