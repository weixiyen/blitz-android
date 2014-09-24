package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.json.JsonHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.client.Response;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
public final class ObjectModelDraft extends ObjectModel {

    // region Member Variables
    // =============================================================================================

    private long mServerTimeOffset;

    @SuppressWarnings("unused") @SerializedName("draft_start_buffer") private int mDraftStartBuffer;
    @SuppressWarnings("unused") @SerializedName("time_per_pick")      private int mTimePerPick;
    @SuppressWarnings("unused") @SerializedName("rounds")             private int mRounds;
    @SuppressWarnings("unused") @SerializedName("users_needed")       private int mUsersNeeded;
    @SuppressWarnings("unused") @SerializedName("week")               private int mWeek;
    @SuppressWarnings("unused") @SerializedName("year")               private int mYear;

    @SuppressWarnings("unused") @SerializedName("id")          private String mId;
    @SuppressWarnings("unused") @SerializedName("chat_id")     private String mChatId;
    @SuppressWarnings("unused") @SerializedName("model")       private String mModel;
    @SuppressWarnings("unused") @SerializedName("owner")       private String mOwner;
    @SuppressWarnings("unused") @SerializedName("game_status") private String mGameStatus;
    @SuppressWarnings("unused") @SerializedName("type")        private String mType;
    @SuppressWarnings("unused") @SerializedName("status")      private String mStatus;

    @SuppressWarnings("unused") @SerializedName("user_confirmed") private boolean mUserConfirmed;

    @SuppressWarnings("unused") @SerializedName("completed")        private Date mCompleted;
    @SuppressWarnings("unused") @SerializedName("created")          private Date mCreated;
    @SuppressWarnings("unused") @SerializedName("last_server_time") private Date mLastServerTime;
    @SuppressWarnings("unused") @SerializedName("last_updated")     private Date mLastUpdated;
    @SuppressWarnings("unused") @SerializedName("started")          private Date mStarted;

    @SuppressWarnings("unused") @SerializedName("points")        private HashMap<String, Float> mPoints;
    @SuppressWarnings("unused") @SerializedName("rating_change") private HashMap<String, Integer> mRatingChange;
    @SuppressWarnings("unused") @SerializedName("rosters")       private HashMap<String, ArrayList<String>> mRosters;
    @SuppressWarnings("unused") @SerializedName("user_info")     private HashMap<String, ObjectModelUser> mUserInfo;

    @SuppressWarnings("unused") @SerializedName("positionsRequired") private ArrayList<String> mPositionsRequired;
    @SuppressWarnings("unused") @SerializedName("users")             private ArrayList<String> mUsers;

    // endregion

    // region Getters
    // =============================================================================================

    public String getId() {
        return mId;
    }

    public String getTeamName(int team) {
        return mUserInfo.get(mUsers.get(team)).getUsername();
    }

    public float getTeamPoints(int team) {
        return mPoints.get(mUsers.get(team));
    }

    public List<String> getTeamRoster(int team) {
        return mRosters.get(mUsers.get(team));
    }

    public int getTeamRatingChange(int team) {
        String key = mUsers.get(team);
        final int change;
        if(mRatingChange.containsKey(key)) {
            change = mRatingChange.get(key);
        } else {
            change = 0;
        }

        return change;
    }

    public int getYear() {
        return mYear;
    }

    public int getWeek() {
        return mWeek;
    }

    public String getStatus() {
        return mGameStatus;
    }

    // endregion

    // region REST Methods
    // =============================================================================================

    /**
     * Fetch a draft given a draft id.
     *
     * @param activity Associated activity.
     * @param callback Callback on completion.
     */
    public void sync(final Activity activity, String draftId, final Runnable callback) {

        if (draftId == null) {
            return;
        }

        RestAPICallback<JsonObject> operation =
                new RestAPICallback<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

                // Set the offset.
                setServerTimeOffset(getOperationTimeStart(), getOperationTimeEnd());

                // Parse result into this draft.
                parseDraft(ObjectModelDraft.this, jsonObject);

                // Now left queue.
                if (callback != null) {
                    callback.run();
                }
            }
        };

        // Make api call.
        mRestAPI.draft_get(draftId, operation);
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
    public static void fetchActiveDraftsForUser(final Activity activity, String userId,
                                                final DraftsCallback callback) {

        RestAPICallback<RestAPIResult<ObjectModelDraft>> operation =
                new RestAPICallback<RestAPIResult<ObjectModelDraft>>(activity) {

            @Override
            public void success(RestAPIResult<ObjectModelDraft> jsonObject) {

                callback.onSuccess(jsonObject.getResults());
            }

            @Override
            public void failure(Response response, boolean networkError) {

                // Show unauthorized message.
                if (getErrorDialog() != null) {
                    getErrorDialog().showUnauthorized();
                }
            }
        };

        // Filter by currently drafting.
        String filter = "{\"status\": \"drafting\", \"model\": \"heads_up_draft\"}";

        // Sort by most recent.
        String orderBy = "{\"created\": \"ASC\"}";

        mRestAPI.drafts_get(getKeys(userId), null, "users",
                filter, orderBy, null, operation);
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

        RestAPICallback<RestAPIResult<ObjectModelDraft>> operation =
                new RestAPICallback<RestAPIResult<ObjectModelDraft>>(activity) {

            @Override
            public void success(RestAPIResult<ObjectModelDraft> jsonObject) {

                callback.onSuccess(jsonObject.getResults());
            }
        };

        // Create filter for current time frame.
        String filter = "{\"week\": " + week + ", \"year\": " +
                year + ", \"model\": \"heads_up_draft\"}";

        // Order by completed and created descending.
        String orderBy = "{\"completed\": \"DESC\", \"created\": \"DESC\"}";

        mRestAPI.drafts_get(getKeys(userId), getPluck(), "users",
                filter, orderBy, limit, operation);
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Parse a json object into a populated
     * draft object model.
     *
     * @param draft Draft object.
     * @param jsonObject Json object.
     */
    private static void parseDraft(ObjectModelDraft draft, JsonObject jsonObject) {

        if (draft != null) {

            // Parse the integers.
            draft.mDraftStartBuffer = JsonHelper.parseInt(jsonObject.get("draft_start_buffer"));
            draft.mTimePerPick      = JsonHelper.parseInt(jsonObject.get("time_per_pick"));
            draft.mRounds           = JsonHelper.parseInt(jsonObject.get("rounds"));
            draft.mUsersNeeded      = JsonHelper.parseInt(jsonObject.get("users_needed"));
            draft.mWeek = JsonHelper.parseInt(jsonObject.get("week"));
            draft.mYear = JsonHelper.parseInt(jsonObject.get("year"));

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

            // Parse into objects.
            JsonObject jsonObjectPoints       = jsonObject.getAsJsonObject("points");
            JsonObject jsonObjectRatingChange = jsonObject.getAsJsonObject("rating_change");
            JsonObject jsonObjectRosters      = jsonObject.getAsJsonObject("rosters");
            JsonObject jsonObjectUserInfo     = jsonObject.getAsJsonObject("user_info");

            if (jsonObjectPoints != null && !jsonObjectPoints.isJsonNull()) {

                draft.mPoints =  JsonHelper.builder().fromJson(jsonObjectPoints,
                        new TypeToken<HashMap<String, Float>>() {
                        }.getType());
            }

            if (jsonObjectRatingChange != null && !jsonObjectRatingChange.isJsonNull()) {

                draft.mRatingChange = JsonHelper.builder().fromJson(jsonObjectRatingChange,
                        new TypeToken<HashMap<String, Integer>>() {
                        }.getType());
            }

            if (jsonObjectRosters != null && !jsonObjectRosters.isJsonNull()) {

                draft.mRosters = JsonHelper.builder().fromJson(jsonObjectRosters,
                        new TypeToken<HashMap<String, ArrayList<String>>>() {
                        }.getType());
            }

            if (jsonObjectUserInfo != null && !jsonObjectUserInfo.isJsonNull()) {

                draft.mUserInfo = JsonHelper.builder().fromJson(jsonObjectUserInfo,
                        new TypeToken<HashMap<String, ObjectModelUser>>() {
                        }.getType());
            }

            // Parse the array lists.
            draft.mPositionsRequired = JsonHelper.parseArrayList(jsonObject.getAsJsonArray("positions_required"));
            draft.mUsers = JsonHelper.parseArrayList(jsonObject.getAsJsonArray("users"));
        }
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
        pluck.add("week");
        pluck.add("year");

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
     */
    private void setServerTimeOffset(Date clientTimeBeforeRequest, Date clientTimeAfterRequest) {

        if (isDrafting() && mLastServerTime != null) {

            // Half half of the request time in milliseconds.
            long halfOfRequestTime = (clientTimeAfterRequest.getTime() -
                    clientTimeBeforeRequest.getTime()) / 2;

            // Now get the exact time at midpoint of request.
            long timeAtMidpointOfRequest = clientTimeBeforeRequest.getTime() +
                    halfOfRequestTime;

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