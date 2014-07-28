package com.blitz.app.models.objects;

import android.app.Activity;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectQueue;

/**
 * Created by mrkcsc on 7/20/14.
 */
public class ObjectModelQueue extends ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Current active draft key,
    // static to preserve state.
    private static String mDraftKey;

    // Preferences model used to sync.
    private ObjectModelPreferences mModelPreferences;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

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
                    public void success() {

                        // Now in queue.
                        if (callback != null) {
                            callback.run();
                        }
                    }
                };

                // Construct POST body.
                JsonObjectQueue.Body body = new JsonObjectQueue.Body(mDraftKey);

                // Make api call.
                RestAPIClient.getAPI().queue
                        (body, new RestAPICallback<JsonObjectQueue>(mRestApiObject, operation));
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
            public void success() {

                // Now left queue.
                if (callback != null) {
                    callback.run();
                }
            }
        };

        // Make api call.
        RestAPIClient.getAPI().queue
                (mDraftKey, new RestAPICallback<JsonObjectQueue>(mRestApiObject, operation));
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
            public void success() {

                // Now confirmed queue.
                if (callback != null) {
                    callback.run();
                }
            }
        };

        // Construct PUT body.
        JsonObjectQueue.BodyPUT body = new JsonObjectQueue.BodyPUT(mDraftKey);

        // Make api call.
        RestAPIClient.getAPI().queue
                (body, new RestAPICallback<JsonObjectQueue>(mRestApiObject, operation));
    }
}