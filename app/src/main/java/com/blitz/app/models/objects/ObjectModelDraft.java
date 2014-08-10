package com.blitz.app.models.objects;

import android.app.Activity;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/27/14.
 */
public class ObjectModelDraft extends ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // The id of the draft, must be
    // provided to sync a draft.
    private String mDraftId;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Set the draft id.
     *
     * @param draftId Draft id.
     */
    public void setDraftId(String draftId) {
        mDraftId = draftId;
    }

    /**
     * Fetch a draft given a draft id.
     *
     * @param activity Associated activity.
     * @param callback Callback on completion.
     */
    public void sync(final Activity activity, final Runnable callback) {
        if (mDraftId == null) {
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
        RestAPIClient.getAPI().draft
                (mDraftId, new RestAPICallback<JsonObject>(mRestApiObject, operation));
    }
}