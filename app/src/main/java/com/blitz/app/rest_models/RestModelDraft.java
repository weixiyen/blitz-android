package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.date.DateUtils;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public final class RestModelDraft extends RestModel {

    // region Member Variables
    // ============================================================================================================

    // More accurate client times.
    private long mServerTimeOffset;

    // Map of players, used for live drafts only.
    private HashMap<String, RestModelPlayer> mPlayerDataMap;

    @SuppressWarnings("unused") @SerializedName("draft_start_buffer")
    private int mDraftStartBuffer;
    @SuppressWarnings("unused") @SerializedName("time_per_pick")
    private int mTimePerPick;
    @SuppressWarnings("unused") @SerializedName("time_per_postview")
    private int mTimePerPostview;
    @SuppressWarnings("unused") @SerializedName("time_per_preview")
    private int mTimePerPreview;
    @SuppressWarnings("unused") @SerializedName("rounds")
    private int mRounds;
    @SuppressWarnings("unused") @SerializedName("users_needed")
    private int mUsersNeeded;
    @SuppressWarnings("unused") @SerializedName("week")
    private int mWeek;
    @SuppressWarnings("unused") @SerializedName("year")
    private int mYear;

    @SuppressWarnings("unused") @SerializedName("id")
    private String mId;
    @SuppressWarnings("unused") @SerializedName("chat_id")
    private String mChatId;
    @SuppressWarnings("unused") @SerializedName("model")
    private String mModel;
    @SuppressWarnings("unused") @SerializedName("owner")
    private String mOwner;
    @SuppressWarnings("unused") @SerializedName("game_status")
    private String mGameStatus;
    @SuppressWarnings("unused") @SerializedName("type")
    private String mType;
    @SuppressWarnings("unused") @SerializedName("status")
    private String mStatus;

    @SuppressWarnings("unused") @SerializedName("user_confirmed")
    private boolean mUserConfirmed;

    @SuppressWarnings("unused") @SerializedName("completed")
    private Date mCompleted;
    @SuppressWarnings("unused") @SerializedName("created")
    private Date mCreated;
    @SuppressWarnings("unused") @SerializedName("last_server_time")
    private Date mLastServerTime;
    @SuppressWarnings("unused") @SerializedName("last_updated")
    private Date mLastUpdated;
    @SuppressWarnings("unused") @SerializedName("started")
    private Date mStarted;
    @SuppressWarnings("unused") @SerializedName("last_round_complete_time")
    private Date mLastRoundCompleteTime;

    @SuppressWarnings("unused") @SerializedName("points")
    private Map<String, Float> mPoints;
    @SuppressWarnings("unused") @SerializedName("rating_change")
    private Map<String, Integer> mRatingChange;
    @SuppressWarnings("unused") @SerializedName("rosters")
    private Map<String, List<String>> mRosters;
    @SuppressWarnings("unused") @SerializedName("user_info")
    private Map<String, RestModelUser> mUserInfo;

    @SuppressWarnings("unused") @SerializedName("positions_required")
    private List<String> mPositionsRequired;
    @SuppressWarnings("unused") @SerializedName("users")
    private List<String> mUsers;
    @SuppressWarnings("unused") @SerializedName("picks")
    private List<Pick> mPicks;
    @SuppressWarnings("unused") @SerializedName("choices")
    private List<List<String>> mChoices;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Fetch the draft id.
     *
     * @return Draft id.
     */
    @SuppressWarnings("unused")
    public String getId() {

        return mId;
    }

    /**
     * Fetch draft status.
     *
     * @return Draft status.
     */
    @SuppressWarnings("unused")
    public String getStatus() {

        return mGameStatus;
    }

    /**
     * Fetch the team name.
     *
     * @param team Team id.
     *
     * @return Team name string.
     */
    @SuppressWarnings("unused")
    public String getTeamName(int team) {

        if (mUsers != null &&
                mUserInfo != null &&
                mUserInfo.get(mUsers.get(team)) != null) {

            return mUserInfo.get(mUsers.get(team)).getUsername();
        }

        return null;
    }

    /**
     * Get points of a specified player team.
     *
     * @param team Team id.
     *
     * @return Points.
     */
    @SuppressWarnings("unused")
    public float getTeamPoints(int team) {

        return mPoints.get(mUsers.get(team));
    }

    /**
     * Find out if the user can draft
     * a a player.
     *
     * @return Can user draft a player.
     */
    @SuppressWarnings("unused")
    public boolean getCanUserDraft() {

        // Are any player picks available.
        boolean picksAvailable = mChoices != null &&
                (mPicks == null || mChoices.size() > (mPicks.size() / 2));

        if (picksAvailable) {

            // If a single pick has already been made.
            if (mPicks != null && mPicks.size() % 2 == 1) {

                Pick latestPick = mPicks.get(mPicks.size() - 1);

                // If this user made the latest pick, can't pick again!
                if (AuthHelper.instance().getUserId().equals(latestPick.mUserId)) {

                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Is the draft complete.
     */
    @SuppressWarnings("unused")
    public boolean getIsDraftComplete() {

        return mStatus != null &&
               mStatus.equals("completed");
    }

    /**
     * Fetch total rounds in the draft.
     *
     * @return Total rounds.
     */
    @SuppressWarnings("unused")
    public int getRounds() {

        return mRounds;
    }

    /**
     * Fetch draft year.
     *
     * @return Draft year.
     */
    @SuppressWarnings("unused")
    public int getYear() {

        return mYear;
    }

    /**
     * Fetch draft week.
     *
     * @return Draft week.
     */
    @SuppressWarnings("unused")
    public int getWeek() {

        return mWeek;
    }

    /**
     * Get the rating change for a given team.
     *
     * @param team Team id.
     *
     * @return Rating change.
     */
    @SuppressWarnings("unused")
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

    /**
     * Time per preview in seconds.
     *
     * @return Time in seconds.
     */
    @SuppressWarnings("unused")
    public int getTimePerPreview() {

        return mTimePerPreview;
    }

    /**
     * Time per post view in seconds.
     *
     * @return Time in seconds.
     */
    @SuppressWarnings("unused")
    public int getTimePerPostview() {

        return mTimePerPostview;
    }

    /**
     * Fetch amount of time for a given pick.
     *
     * @return Time per pick.
     */
    @SuppressWarnings("unused")
    public int getTimePerPick() {

        return mTimePerPick;
    }

    /**
     * Seconds since draft started.
     *
     * @return Seconds since draft started.
     */
    @SuppressWarnings("unused")
    public int getSecondsSinceStarted() {

        return getSecondsSince(mStarted);
    }

    /**
     * Seconds since last round completed.
     *
     * @return Seconds since last round completed.
     */
    @SuppressWarnings("unused")
    public int getSecondsSinceLastRoundCompleteTime() {

        return getSecondsSince(mLastRoundCompleteTime);
    }

    /**
     * Buffer time before draft starts.
     *
     * @return Buffer time in seconds.
     */
    @SuppressWarnings("unused")
    public int getDraftStartBuffer() {

        return mDraftStartBuffer;
    }

    /**
     * Fetch the current round in the draft.
     *
     * @return Draft round.
     */
    @SuppressWarnings("unused")
    public int getCurrentRound() {

        // The round is derived based on the picks so far.
        return (mPicks == null ? 1 : mPicks.size() / 2) + 1;
    }

    /**
     * Fetch list of users associated with this draft.
     *
     * @return List of user ids.
     */
    @SuppressWarnings("unused")
    public List<String> getUsers() {

        return mUsers;
    }

    /**
     * Get a specified team roster.
     *
     * @param team Team id.
     *
     * @return Team roster.
     */
    @SuppressWarnings("unused")
    public List<String> getTeamRoster(int team) {
        return mRosters.get(mUsers.get(team));
    }

    /**
     * Get picks made so far.
     *
     * @return List of picks.
     */
    @SuppressWarnings("unused")
    public List<Pick> getPicks() {

        return mPicks;
    }

    /**
     * Fetch a list of current player choices.
     *
     * @return List of choices.
     */
    @SuppressWarnings("unused")
    public List<String> getCurrentPlayerChoices() {

        if (mChoices != null && mChoices.size() > 0) {

            return mChoices.get(mChoices.size() - 1);
        }

        return null;
    }

    /**
     * Get list of all round choices.
     *
     * @return List of choices for each round.
     */
    @SuppressWarnings("unused")
    public List<List<String>> getChoices() {

        return mChoices;
    }

    /**
     * Fetch positions required for this draft.
     *
     * @return Positions required for this draft.
     */
    @SuppressWarnings("unused")
    public List<String> getPositionsRequired() {

        return mPositionsRequired;
    }

    /**
     * Fetch map of player data.
     *
     * @return Player data.
     */
    @SuppressWarnings("unused")
    public Map<String, RestModelPlayer> getPlayerDataMap() {

        if (mPlayerDataMap == null) {
            mPlayerDataMap = new HashMap<>();
        }

        return mPlayerDataMap;
    }

    /**
     * Get player rosters.
     *
     * @return List of player rosters.
     */
    @SuppressWarnings("unused")
    public Map<String, List<String>> getRosters() {

        return mRosters;
    }

    /**
     * Fetch user info dictionary.
     *
     * @return User info dictionary.
     */
    @SuppressWarnings("unused")
    public Map<String, RestModelUser> getUserInfo() {

        return mUserInfo;
    }

    /**
     * Get last round complete time.
     *
     * @return Last round complete time.
     */
    @SuppressWarnings("unused")
    public Date getLastRoundCompleteTime() {

        return mLastRoundCompleteTime;
    }

    /**
     * Set the last round complete time.
     *
     * @param lastRoundCompleteTime Last round complete time.
     */
    @SuppressWarnings("unused")
    public void setLastRoundCompleteTime(Date lastRoundCompleteTime) {

        mLastRoundCompleteTime = lastRoundCompleteTime;
    }

    /**
     * Set the last time received from the server.
     *
     * @param lastServerTime Last server time.
     */
    @SuppressWarnings("unused")
    public void setLastServerTime(Date lastServerTime) {

        // Update the last server time.
        mLastServerTime = lastServerTime;

        // Set the offset based on the current client time.
        mServerTimeOffset = DateUtils
                .getTimeSinceDateInGMTAsMilliseconds(mLastServerTime);
    }

    /**
     * Add a new choice to this draft.
     *
     * @param choice Choice object.
     */
    @SuppressWarnings("unused")
    public void addChoice(RestModelPlayer choice) {

        if (mPlayerDataMap == null) {
            mPlayerDataMap = new HashMap<>();
        }

        // Add to player data map.
        mPlayerDataMap.put(choice.getId(), choice);
    }

    /**
     * Add a new list of choices.
     *
     * @param choices List of choices.
     */
    @SuppressWarnings("unused")
    public void addChoices(List<String> choices) {

        if (mChoices == null) {
            mChoices = new ArrayList<>();
        }

        mChoices.add(choices);
    }

    /**
     * Add a pick to the picks array.
     *
     * @param pick Pick object.
     */
    @SuppressWarnings("unused")
    public void addPick(Pick pick) {

        if (mPicks == null) {
            mPicks = new ArrayList<>();
        }

        mPicks.add(pick);
    }

    // endregion

    // region REST Methods
    // ============================================================================================================

    /**
     * Pick a player.
     *
     * @param activity Activity fo loading/error dialogs.
     * @param draftId Draft id.
     * @param playerId Player id.
     * @param callback Callback on completion.
     */
    @SuppressWarnings("unused")
    public static void pickPlayer(Activity activity, String draftId, String playerId,
                                  @NonNull RestCallback<RestModelDraft> callback) {

        if (draftId == null || playerId == null) {

            return;
        }

        // Create object holding values to replace.
        JsonObject jsonReplace   = new JsonObject();
        JsonObject jsonPicks     = new JsonObject();
        JsonObject jsonPlayerIds = new JsonObject();

        jsonPlayerIds.addProperty("player_id", playerId);
        jsonPicks.add("picks", jsonPlayerIds);
        jsonReplace.add("append", jsonPicks);

        // Make api call.
        mRestAPI.draft_patch(draftId, jsonReplace, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()),
                    (response, networkError) -> callback.onFailure()));
    }

    /**
     * Fetch a draft given a draft id.
     *
     * @param activity Associated activity.
     * @param callback Callback on completion.
     */
    @SuppressWarnings("unused")
    public static void fetchSyncedDraft(Activity activity, String draftId,
                                        @NonNull RestCallback<RestModelDraft> callback) {

        if (draftId == null) {
            return;
        }

        // Set the start time.
        Date operationStartTime = DateUtils.getDateInGMT();

        // Make api call.
        mRestAPI.draft_get(draftId, new RestAPICallback<>(activity, result -> {

            // Set the server time offset.
            if (result != null) {
                result.setServerTimeOffset(operationStartTime, DateUtils.getDateInGMT());
            }

            // Now left queue.
            callback.onSuccess(result);

        }, null));
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
            @NonNull RestCallbacks<RestModelDraft> callback) {

        RestAPICallback<RestAPIResult<RestModelDraft>> operation = new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null);

        // Logout on failure.
        operation.setLogoutOnFailure(true);

        // Filter by currently drafting.
        String filter = "{\"status\": \"drafting\", \"model\": \"heads_up_draft\"}";

        // Sort by most recent.
        String orderBy = "{\"created\": \"ASC\"}";

        mRestAPI.drafts_get(getKeys(userId), null, "users", filter, orderBy, null, operation);
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
                                          @NonNull RestCallbacks<RestModelDraft> callback) {

        // Create filter for current time frame.
        String filter = "{\"week\": " + week + ", \"year\": " +
                year + ", \"model\": \"heads_up_draft\"}";

        // Order by completed and created descending.
        String orderBy = "{\"completed\": \"DESC\", \"created\": \"DESC\"}";

        mRestAPI.drafts_get(getKeys(userId), getPluck(), "users",
                filter, orderBy, limit, new RestAPICallback<>(activity,
                        result -> callback.onSuccess(result.getResults()), null));
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Fetch keys for draft.
     *
     * @param userId User id.
     *
     * @return List of keys.
     */
    private static ArrayList<String> getKeys(String userId) {

        ArrayList<String> keys = new ArrayList<>();

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

        ArrayList<String> pluck = new ArrayList<>();

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
     * Some sort of magic that makes for a better
     * real time experience in terms of latency.
     */
    private void setServerTimeOffset(Date clientTimeBeforeRequest, Date clientTimeAfterRequest) {

        // Use either the started or last received server time.
        Date serverTime = mLastServerTime != null ? mLastServerTime : mStarted;

        if (serverTime != null) {

            // Half half of the request time in milliseconds.
            long halfOfRequestTime = (clientTimeAfterRequest.getTime() -
                    clientTimeBeforeRequest.getTime()) / 2;

            // Now get the exact time at midpoint of request.
            long timeAtMidpointOfRequest = clientTimeBeforeRequest.getTime() +
                    halfOfRequestTime;

            mServerTimeOffset = Math.abs(serverTime.getTime() - timeAtMidpointOfRequest);

        } else {

            mServerTimeOffset = 0;
        }
    }

    /**
     * Get seconds since a given date, but
     * include the server time offset.
     *
     * @param date Date in GMT.
     *
     * @return Seconds since that time.
     */
    private int getSecondsSince(Date date) {

        return (int) Math.floor(Math.abs
                (DateUtils.getTimeSinceDateInGMTAsMilliseconds(date) - mServerTimeOffset) / 1000.0f);
    }

    // endregion

    // region Inner Class
    // ============================================================================================================

    /**
     * Player pick object.
     */
    public static class Pick {

        @SuppressWarnings("unused") @SerializedName("player_id")
        private String mPlayerId;
        @SuppressWarnings("unused") @SerializedName("user_id")
        private String mUserId;
        @SuppressWarnings("unused") @SerializedName("position")
        private String mPosition;
        @SuppressWarnings("unused") @SerializedName("timestamp")
        private String mTimestamp;

        @SuppressWarnings("unused") @SerializedName("round")
        private int mRound;

        /**
         * Used for JSON parsing into object.
         */
        @SuppressWarnings("unused")
        public Pick() {

        }

        /**
         * Create a pick object manually with
         * a bare minimum of objects.
         *
         * @param playerId Player id.
         * @param userId User id.
         */
        @SuppressWarnings("unused")
        public Pick(String playerId, String userId) {

            mPlayerId = playerId;
              mUserId =   userId;
        }

        /**
         * Get player Id.
         *
         * @return Player id.
         */
        @SuppressWarnings("unused")
        public String getPlayerId() {

            return mPlayerId;
        }

        /**
         * Get user id associated with pick.
         *
         * @return User id.
         */
        @SuppressWarnings("unused")
        public String getUserId() {

            return mUserId;
        }
    }

    // endregion
}