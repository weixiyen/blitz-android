package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.json.JsonHelper;
import com.blitz.app.utilities.rest.RestAPICallbackCombined;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.gson.JsonObject;
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

    @SuppressWarnings("unused") private int mDraftStartBuffer;
    @SuppressWarnings("unused") private int mTimePerPick;
    @SuppressWarnings("unused") private int mRounds;
    @SuppressWarnings("unused") private int mUsersNeeded;
    @SuppressWarnings("unused") private int mWeek;
    @SuppressWarnings("unused") private int mYear;

    @SuppressWarnings("unused") private String mId;
    @SuppressWarnings("unused") private String mChatId;
    @SuppressWarnings("unused") private String model;
    @SuppressWarnings("unused") private String owner;
    @SuppressWarnings("unused") private String game_status;
    @SuppressWarnings("unused") private String mType;
    @SuppressWarnings("unused") private String mStatus;

    @SuppressWarnings("unused") private boolean mUserConfirmed;

    @SuppressWarnings("unused") private Date mCompleted;
    @SuppressWarnings("unused") private Date mCreated;
    @SuppressWarnings("unused") private Date mLastServerTime;
    @SuppressWarnings("unused") private Date mLastUpdated;
    @SuppressWarnings("unused") private Date mStarted;

    @SuppressWarnings("unused") private HashMap<String, Float> points;
    @SuppressWarnings("unused") private HashMap<String, Integer> rating_change;
    @SuppressWarnings("unused") private HashMap<String, ArrayList<String>> rosters;
    @SuppressWarnings("unused") private HashMap<String, ObjectModelUser> user_info;

    @SuppressWarnings("unused") private ArrayList<String> positions_required;
    @SuppressWarnings("unused") private ArrayList<String> users;

    // endregion

    // region Getters
    // =============================================================================================

    public String getId() {
        return mId;
    }

    public String getTeamName(int team) {
        return user_info.get(users.get(team)).getUsername();
    }

    public float getTeamPoints(int team) {
        return points.get(users.get(team));
    }

    public List<String> getTeamRoster(int team) {
        return rosters.get(users.get(team));
    }

    public int getTeamRatingChange(int team) {
        String key = users.get(team);
        final int change;
        if(rating_change.containsKey(key)) {
            change = rating_change.get(key);
        } else {
            change = 0;
        }

        return change;
    }

    public String getStatus() {
        return game_status;
    }

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

        RestAPICallbackCombined<JsonObject> operation =
                new RestAPICallbackCombined<JsonObject>(activity) {

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
        mRestAPI.draft_get(mId, operation);
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

        RestAPICallbackCombined<RestAPIResult<ObjectModelDraft>> op =
                new RestAPICallbackCombined<RestAPIResult<ObjectModelDraft>>(activity) {

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
                filter, orderBy, null, op);
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

        RestAPICallbackCombined<RestAPIResult<ObjectModelDraft>> operation =
                new RestAPICallbackCombined<RestAPIResult<ObjectModelDraft>>(activity) {

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
            draft.mWeek             = JsonHelper.parseInt(jsonObject.get("week"));
            draft.mYear             = JsonHelper.parseInt(jsonObject.get("year"));

            // Parse the strings.
            draft.mId         = JsonHelper.parseString(jsonObject.get("id"));
            draft.mChatId     = JsonHelper.parseString(jsonObject.get("chat_id"));
            draft.mType       = JsonHelper.parseString(jsonObject.get("model"));
            draft.owner      = JsonHelper.parseString(jsonObject.get("owner"));
            draft.game_status = JsonHelper.parseString(jsonObject.get("game_status"));
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

                draft.points =  JsonHelper.builder().fromJson(jsonObjectPoints,
                        new TypeToken<HashMap<String, Float>>() {
                        }.getType());
            }

            if (jsonObjectRatingChange != null && !jsonObjectRatingChange.isJsonNull()) {

                draft.rating_change = JsonHelper.builder().fromJson(jsonObjectRatingChange,
                        new TypeToken<HashMap<String, Integer>>() {
                        }.getType());
            }

            if (jsonObjectRosters != null && !jsonObjectRosters.isJsonNull()) {

                draft.rosters = JsonHelper.builder().fromJson(jsonObjectRosters,
                        new TypeToken<HashMap<String, ArrayList<String>>>() {
                        }.getType());
            }

            if (jsonObjectUserInfo != null && !jsonObjectUserInfo.isJsonNull()) {

                draft.user_info = JsonHelper.builder().fromJson(jsonObjectUserInfo,
                        new TypeToken<HashMap<String, ObjectModelUser>>() {
                        }.getType());
            }

            // Parse the array lists.
            draft.positions_required = JsonHelper.parseArrayList(jsonObject.getAsJsonArray("positions_required"));
            draft.users = JsonHelper.parseArrayList(jsonObject.getAsJsonArray("users"));
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