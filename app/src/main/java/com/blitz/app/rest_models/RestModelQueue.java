package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/20/14. Copyright 2014 Blitz Studios
 */
public class RestModelQueue extends RestModel {

    // region Member Variables
    // ============================================================================================================

    // Current active draft key,
    // static to preserve state.
    private static String mDraftKey;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Add user to draft queue.  Requires a draft
     * key to be set.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public void queueUp(final Activity activity, @NonNull Runnable callback) {

        // First sync preferences to get the active draft key.
        AuthHelper.instance().getPreferences(activity, true,
                new RestModelCallback<RestModelPreferences>() {

                    @Override
                    public void onSuccess(RestModelPreferences object) {

                        // Set draft key to the active queue.
                        mDraftKey = object.getCurrentActiveQueue();

                        // Construct POST body.
                        JsonObject body = new JsonObject();
                        body.addProperty("draft_key", mDraftKey);

                        // Make api call.
                        mRestAPI.queue_post(body, new RestAPICallback<>(activity, result -> callback.run(), null));
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
    @SuppressWarnings("unused")
    public void leaveQueue(Activity activity, @NonNull Runnable callback) {

        if (mDraftKey == null) {
            return;
        }

        // Make api call.
        mRestAPI.queue_delete(mDraftKey, new RestAPICallback<>(activity, result -> callback.run(), null));
    }

    /**
     * Confirm user match in the draft queue. Requires
     * a draft key to be set.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public void confirmQueue(Activity activity, @NonNull Runnable callback) {

        if (mDraftKey == null) {
            return;
        }

        // Construct PUT body.
        JsonObject body = new JsonObject();
                   body.addProperty("draft_key", mDraftKey);

        // Make api call.
        mRestAPI.queue_put(body, new RestAPICallback<>(activity, result -> callback.run(), null));
    }

    // endregion
}