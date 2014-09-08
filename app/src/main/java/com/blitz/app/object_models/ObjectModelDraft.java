package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.blitz.app.utilities.rest.RestAPIObject;
import com.blitz.app.utilities.rest.RestAPIOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelDraft {

    // region Member Variables
    // =============================================================================================

    // The id of the draft, must be
    // provided to sync a draft.
    private String mDraftId;

    // endregion

    // region Public Methods
    // =============================================================================================

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
            public void success(RestAPIObject restAPIObject) {

                // Now left queue.
                if (callback != null) {
                    callback.run();
                }
            }
        };

        // Make api call.
        RestAPIClient.getAPI().draft_get
                (mDraftId, RestAPICallback.create(operation));
    }

    /**
     * Fetch list of the users current active drafts.  It is a serious
     * error if this method fails so if it does we must log out the user.
     *
     * @param activity Activity fo loading/error dialogs.
     * @param userId User id.
     * @param callback Success callback, provides the list of drafts.
     */
    @SuppressWarnings("unused")
    public static void fetchActiveDraftsForUser(Activity activity, String userId,
                                                final DraftsCallback callback) {

        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                // TODO: Parse and create drafts.

                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            /**
             * Force log out the user on failure.
             */
            @Override
            public void failure(boolean logout) {
                super.failure(true);
            }
        };

        // Filter by currently drafting.
        String filter = "{\"status\": \"drafting\", \"model\": \"heads_up_draft\"}";

        // Sort by most recent.
        String orderBy = "{\"created\": \"ASC\"}";

        RestAPIClient.getAPI().drafts_get(getKeys(userId), null, "users",
                filter, orderBy, null, RestAPICallback.create(operation));
    }

    @SuppressWarnings("unused")
    public static void fetchDraftsForUser(Activity activity, String userId,
                                          Integer week,
                                          Integer year,
                                          Integer limit,
                                          final DraftsCallback callback) {

        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                // TODO: Parse and create drafts.

                if (callback != null) {
                    callback.onSuccess(null);
                }
            }
        };

        // Create filter for current time frame.
        String filter = "{\"week\": " + week + ", \"year\": " +
                year + ", \"model\": \"heads_up_draft\"}";

        // Order by completed and created descending.
        String orderBy = "{\"completed\": \"DESC\", \"created\": \"DESC\"}";

        RestAPIClient.getAPI().drafts_get(getKeys(userId), null, "users",
                filter, orderBy, limit, RestAPICallback.create(operation));
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Fetch keys for draft.
     *
     * @param userId User id.
     *
     * @return List of keys.
     */
    private static ArrayList<String> getKeys(String userId) {

        ArrayList<String> keys = new ArrayList<String>();

        keys.add(userId);

        return keys;
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface DraftsCallback {

        public void onSuccess(List<ObjectModelDraft> drafts);
    }

    // endregion
}