package com.blitz.app.rest_models;

import android.app.Activity;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/20/14. Copyright 2014 Blitz Studios
 */
public class RestModelQueue extends RestModel {

    // region Member Variables
    // =============================================================================================

    // Current active draft key,
    // static to preserve state.
    private static String mDraftKey;

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

        // First sync preferences to get the active draft key.
        AuthHelper.instance().getPreferences(activity, true,
                new RestModelCallback<RestModelPreferences>() {

                    @Override
                    public void onSuccess(RestModelPreferences object) {

                        // Set draft key to the active queue.
                        mDraftKey = object.getCurrentActiveQueue();

                        // Define operation, call on queue up when complete.
                        RestAPICallback<JsonObject> operation = new RestAPICallback<JsonObject>(activity) {

                            @Override
                            public void success(JsonObject jsonObject) {

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
                        mRestAPI.queue_post(body, operation);
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
        RestAPICallback<JsonObject> operation =
                new RestAPICallback<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

                // Now left queue.
                if (callback != null) {
                    callback.run();
                }
            }
        };

        // Make api call.
        mRestAPI.queue_delete(mDraftKey, operation);
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
        RestAPICallback<JsonObject> operation =
                new RestAPICallback<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

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
        mRestAPI.queue_put(body, operation);
    }

    // endregion
}