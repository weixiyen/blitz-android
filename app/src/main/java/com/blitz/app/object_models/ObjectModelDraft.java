package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit.client.Response;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public final class ObjectModelDraft extends ObjectModel {

    // region Member Variables
    // =============================================================================================

    private long mServerTimeOffset;

    @SuppressWarnings("unused") @SerializedName("draft_start_buffer")
    private int mDraftStartBuffer;
    @SuppressWarnings("unused") @SerializedName("time_per_pick")
    private int mTimePerPick;
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
    private HashMap<String, Float> mPoints;
    @SuppressWarnings("unused") @SerializedName("rating_change")
    private HashMap<String, Integer> mRatingChange;
    @SuppressWarnings("unused") @SerializedName("rosters")
    private HashMap<String, ArrayList<String>> mRosters;
    @SuppressWarnings("unused") @SerializedName("user_info")
    private HashMap<String, ObjectModelUser> mUserInfo;

    @SuppressWarnings("unused") @SerializedName("positionsRequired")
    private ArrayList<String> mPositionsRequired;
    @SuppressWarnings("unused") @SerializedName("users")
    private ArrayList<String> mUsers;
    @SuppressWarnings("unused") @SerializedName("picks")
    private ArrayList<Pick> mPicks;

    // endregion

    // region Getters
    // =============================================================================================

    public String getId() {
        return mId;
    }

    public String getStatus() {
        return mGameStatus;
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
     * Get last round complete time.
     *
     * @return Last round complete time.
     */
    @SuppressWarnings("unused")
    private Date getLastRoundCompleteTime() {

        return mLastRoundCompleteTime;
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
    @SuppressWarnings("unused")
    public static void fetchSyncedDraft(final Activity activity, String draftId,
                                        final DraftCallback callback) {

        if (draftId == null) {
            return;
        }

        RestAPICallback<ObjectModelDraft> operation =
                new RestAPICallback<ObjectModelDraft>(activity) {

            @Override
            public void success(ObjectModelDraft jsonObject) {

                // Set the server time offset.
                if (jsonObject != null) {
                    jsonObject.setServerTimeOffset
                            (getOperationTimeStart(), getOperationTimeEnd());
                }

                // Now left queue.
                if (callback != null) {
                    callback.onSuccess(jsonObject);
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

    // region Inner Class
    // =============================================================================================

    /**
     * Player pick object.
     */
    public class Pick {

        @SuppressWarnings("unused") @SerializedName("player_id")
        private String mPlayerId;
        @SuppressWarnings("unused") @SerializedName("user_id")
        private String mUserId;
        @SuppressWarnings("unused") @SerializedName("position")
        private String mPosition;

        @SuppressWarnings("unused") @SerializedName("round")
        private int mRound;

        @SuppressWarnings("unused") @SerializedName("timestamp")
        private Date mTimestamp;
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface DraftCallback {

        public void onSuccess(ObjectModelDraft draft);
    }

    public interface DraftsCallback {

        public void onSuccess(List<ObjectModelDraft> drafts);
    }

    // endregion
}