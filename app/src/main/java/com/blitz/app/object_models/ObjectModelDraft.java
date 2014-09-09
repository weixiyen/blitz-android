package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.json.JsonHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIObject;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
public final class ObjectModelDraft extends ObjectModel {

    // region Member Variables
    // =============================================================================================

    private long mServerTimeOffset;

    @SuppressWarnings("unused") private int mDraftStartBuffer;
    @SuppressWarnings("unused") private int mTimePerPick;
    @SuppressWarnings("unused") private int mRounds;
    @SuppressWarnings("unused") private int mUsersNeeded;
    @SuppressWarnings("unused") private int mWeek;
    @SuppressWarnings("unused") private int mYear;

    @SuppressWarnings("unused") private String mId;
    @SuppressWarnings("unused") private String mChatId;
    @SuppressWarnings("unused") private String mModel;
    @SuppressWarnings("unused") private String mOwner;
    @SuppressWarnings("unused") private String mGameStatus;
    @SuppressWarnings("unused") private String mType;
    @SuppressWarnings("unused") private String mStatus;

    @SuppressWarnings("unused") private boolean mUserConfirmed;

    @SuppressWarnings("unused") private Date mCompleted;
    @SuppressWarnings("unused") private Date mCreated;
    @SuppressWarnings("unused") private Date mLastServerTime;
    @SuppressWarnings("unused") private Date mLastUpdated;
    @SuppressWarnings("unused") private Date mStarted;

    @SuppressWarnings("unused") private HashMap<String, Float>             mPoints;
    @SuppressWarnings("unused") private HashMap<String, Integer>           mRatingChange;
    @SuppressWarnings("unused") private HashMap<String, ArrayList<String>> mRosters;
    @SuppressWarnings("unused") private HashMap<String, ObjectModelUser>   mUserInfo;

    @SuppressWarnings("unused") private ArrayList<String> mPositionsRequired;
    @SuppressWarnings("unused") private ArrayList<String> mUsers;

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Set the draft id.
     *
     * @param draftId Draft id.
     */
    public void setDraftId(String draftId) {

        mId = draftId;
    }

    /**
     * Fetch a draft given a draft id.
     *
     * @param activity Associated activity.
     * @param callback Callback on completion.
     */
    public void sync(final Activity activity, final Runnable callback) {

        if (mId == null) {
            return;
        }

        final long clientTimeBeforeRequest = new Date().getTime();

        // Operation callbacks.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                // Set the offset.
                setServerTimeOffset(clientTimeBeforeRequest);

                // Now left queue.
                if (callback != null) {
                    callback.run();
                }
            }
        };

        // Make api call.
        mRestAPI.draft_get(mId, RestAPICallback.create(operation));
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

                // Fetch array of results.
                JsonArray jsonArray = restAPIObject.getJsonObject().getAsJsonArray("results");

                if (callback != null) {
                    callback.onSuccess(parseDrafts(jsonArray));
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

        mRestAPI.drafts_get(getKeys(userId), null, "users",
                filter, orderBy, null, RestAPICallback.create(operation));
    }

    /**
     * Fetch list of the users drafts filtered by
     * various elements for use in the game log.
     *
     * @param activity Activity fo loading/error dialogs.
     * @param userId User id.
     * @param week Target week.
     * @param year Target year.
     * @param limit Total results.
     * @param callback Success callback, provides the list of drafts.
     */
    @SuppressWarnings("unused")
    public static void fetchDraftsForUser(Activity activity, String userId,
                                          Integer week,
                                          Integer year,
                                          Integer limit,
                                          final DraftsCallback callback) {

        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                // Fetch array of results.
                JsonArray jsonArray = restAPIObject.getJsonObject().getAsJsonArray("results");

                if (callback != null) {
                    callback.onSuccess(parseDrafts(jsonArray));
                }
            }
        };

        // Create filter for current time frame.
        String filter = "{\"week\": " + week + ", \"year\": " +
                year + ", \"model\": \"heads_up_draft\"}";

        // Order by completed and created descending.
        String orderBy = "{\"completed\": \"DESC\", \"created\": \"DESC\"}";

        mRestAPI.drafts_get(getKeys(userId), getPluck(), "users",
                filter, orderBy, limit, RestAPICallback.create(operation));
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Parse raw json result array into a list
     * of draft object models.
     *
     * @param jsonArray Json object array.
     *
     * @return List of draft models.
     */
    private static ArrayList<ObjectModelDraft> parseDrafts(JsonArray jsonArray) {

        // Create a new list of drafts.
        ArrayList<ObjectModelDraft> drafts = new ArrayList<ObjectModelDraft>();

        if (jsonArray != null) {

            // Iterate over each draft json.
            for (int i = 0; i < jsonArray.size(); i++) {

                drafts.add(parseDraft(jsonArray.get(i).getAsJsonObject()));
            }
        }

        return drafts;
    }

    /**
     * Parse a json object into a populated
     * draft object model.
     *
     * @param jsonObject Json object.
     *
     * @return Draft model.
     */
    private static ObjectModelDraft parseDraft(JsonObject jsonObject) {

        // Create a new draft.
        ObjectModelDraft draft = new ObjectModelDraft();

        // Parse the integers.
        draft.mDraftStartBuffer = JsonHelper.parseInt(jsonObject.get("draft_start_buffer"));
        draft.mTimePerPick      = JsonHelper.parseInt(jsonObject.get("time_per_pick"));
        draft.mRounds           = JsonHelper.parseInt(jsonObject.get("rounds"));
        draft.mUsersNeeded      = JsonHelper.parseInt(jsonObject.get("users_needed"));
        draft.mWeek             = JsonHelper.parseInt(jsonObject.get("week"));
        draft.mYear             = JsonHelper.parseInt(jsonObject.get("year"));

        // Parse the strings.
        draft.mId         = JsonHelper.parseString(jsonObject.get("id"));
        draft.mChatId     = JsonHelper.parseString(jsonObject.get("chat_id"));
        draft.mType       = JsonHelper.parseString(jsonObject.get("model"));
        draft.mOwner      = JsonHelper.parseString(jsonObject.get("owner"));
        draft.mGameStatus = JsonHelper.parseString(jsonObject.get("game_status"));
        draft.mType       = JsonHelper.parseString(jsonObject.get("type"));
        draft.mStatus     = JsonHelper.parseString(jsonObject.get("status"));

        // Parse the booleans.
        draft.mUserConfirmed = JsonHelper.parseBool(jsonObject.get("user_confirmed"));

        // Parse the dates.
        draft.mCompleted      = JsonHelper.parseDate(jsonObject.get("completed"));
        draft.mCreated        = JsonHelper.parseDate(jsonObject.get("created"));
        draft.mLastServerTime = JsonHelper.parseDate(jsonObject.get("last_server_time"));
        draft.mLastUpdated    = JsonHelper.parseDate(jsonObject.get("last_updated"));
        draft.mStarted        = JsonHelper.parseDate(jsonObject.get("started"));

        // Parse the hash maps.
        draft.mPoints       = JsonHelper.parseHashMap(jsonObject.getAsJsonObject("points"));
        draft.mRatingChange = JsonHelper.parseHashMap(jsonObject.getAsJsonObject("rating_change"));
        draft.mRosters      = JsonHelper.parseHashMap(jsonObject.getAsJsonObject("rosters"));
        draft.mUserInfo     = JsonHelper.parseHashMap(jsonObject.getAsJsonObject("user_info"));

        // Parse the array lists.
        draft.mPositionsRequired = JsonHelper.parseArrayList(jsonObject.getAsJsonArray("positions_required"));
        draft.mUsers             = JsonHelper.parseArrayList(jsonObject.getAsJsonArray("users"));

        return draft;
    }

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

    /**
     * Get pluck values used to fetch
     * larger draft lists.
     *
     * @return List of pluck elements.
     */
    private static ArrayList<String> getPluck() {

        ArrayList<String> pluck = new ArrayList<String>();

        pluck.add("id");
        pluck.add("type");
        pluck.add("users");
        pluck.add("rosters");
        pluck.add("points");
        pluck.add("created");
        pluck.add("completed");
        pluck.add("game_status");
        pluck.add("user_info");
        pluck.add("rating_change");

        return pluck;
    }

    /**
     * Are we drafting.
     */
    private boolean isDrafting() {
        return mStatus != null && mStatus.equals("drafting");
    }

    /**
     * Is the draft complete.
     */
    private boolean isDraftComplete() {
        return mStatus != null && mStatus.equals("completed");
    }

    /**
     * Some sort of magic that makes for a better
     * real time experience in terms of latency.
     *
     * See Weixi for details.
     *
     * @param clientTimeBeforeRequest Client time before the request.
     */
    private void setServerTimeOffset(long clientTimeBeforeRequest) {

        if (isDrafting() && mLastServerTime != null) {

            // Fetch current time on the client.
            long clientTimeAfterRequest = new Date().getTime();

            // Half half of the request time in milliseconds.
            long halfOfRequestTime = (clientTimeAfterRequest - clientTimeBeforeRequest) / 2;

            // Now get the exact time at midpoint of request.
            long timeAtMidpointOfRequest = clientTimeBeforeRequest + halfOfRequestTime;

            mServerTimeOffset = Math.abs(mLastServerTime.getTime() - timeAtMidpointOfRequest);

        } else {

            mServerTimeOffset = 0;
        }
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface DraftsCallback {

        public void onSuccess(List<ObjectModelDraft> drafts);
    }

    // endregion
}