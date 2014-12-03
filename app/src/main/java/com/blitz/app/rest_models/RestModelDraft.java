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

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public final class RestModelDraft extends RestModel {

    // region Member Variables
    // ============================================================================================================

    // More accurate client times.
    private long serverTimeOffset;

    // Map of players, used for live drafts only.
    private HashMap<String, RestModelPlayer> playerDataMap;

    @SuppressWarnings("unused") @SerializedName("draft_start_buffer") @Getter
    private int draftStartBuffer;
    @SuppressWarnings("unused") @SerializedName("time_per_pick") @Getter
    private int timePerPick;
    @SuppressWarnings("unused") @SerializedName("time_per_postview") @Getter
    private int timePerPostview;
    @SuppressWarnings("unused") @SerializedName("time_per_preview") @Getter
    private int timePerPreview;
    @SuppressWarnings("unused") @SerializedName("rounds") @Getter
    private int rounds;
    @SuppressWarnings("unused") @SerializedName("users_needed")
    private int usersNeeded;
    @SuppressWarnings("unused") @SerializedName("week") @Getter
    private int week;
    @SuppressWarnings("unused") @SerializedName("year") @Getter
    private int year;

    @SuppressWarnings("unused") @SerializedName("id") @Getter
    private String id;
    @SuppressWarnings("unused") @SerializedName("chat_id")
    private String chatId;
    @SuppressWarnings("unused") @SerializedName("model")
    private String model;
    @SuppressWarnings("unused") @SerializedName("owner")
    private String owner;
    @SuppressWarnings("unused") @SerializedName("game_status") @Getter
    private String gameStatus;
    @SuppressWarnings("unused") @SerializedName("type")
    private String type;
    @SuppressWarnings("unused") @SerializedName("status")
    private String status;

    @SuppressWarnings("unused") @SerializedName("user_confirmed")
    private boolean userConfirmed;

    @SuppressWarnings("unused") @SerializedName("completed")
    private Date completed;
    @SuppressWarnings("unused") @SerializedName("created")
    private Date created;
    @SuppressWarnings("unused") @SerializedName("last_server_time")
    private Date lastServerTime;
    @SuppressWarnings("unused") @SerializedName("last_updated")
    private Date lastUpdated;
    @SuppressWarnings("unused") @SerializedName("started")
    private Date started;
    @SuppressWarnings("unused") @SerializedName("last_round_complete_time") @Getter @Setter
    private Date lastRoundCompleteTime;

    @SuppressWarnings("unused") @SerializedName("points")
    private Map<String, Float> points;
    @SuppressWarnings("unused") @SerializedName("rating_change")
    private Map<String, Integer> ratingChange;
    @SuppressWarnings("unused") @SerializedName("rosters") @Getter
    private Map<String, List<String>> rosters;
    @SuppressWarnings("unused") @SerializedName("user_info") @Getter
    private Map<String, RestModelUser> userInfo;

    @SuppressWarnings("unused") @SerializedName("positions_required") @Getter
    private List<String> positionsRequired;
    @SuppressWarnings("unused") @SerializedName("users") @Getter
    private List<String> users;
    @SuppressWarnings("unused") @SerializedName("picks") @Getter
    private List<Pick> picks;
    @SuppressWarnings("unused") @SerializedName("choices") @Getter
    private List<List<String>> choices;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Fetch the team name.
     *
     * @param team Team id.
     *
     * @return Team name string.
     */
    @SuppressWarnings("unused")
    public String getTeamName(int team) {

        if (users != null &&
                userInfo != null &&
                userInfo.get(users.get(team)) != null) {

            return userInfo.get(users.get(team)).getUsername();
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

        return points.get(users.get(team));
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
        boolean picksAvailable = choices != null &&
                (picks == null || choices.size() > (picks.size() / 2));

        if (picksAvailable) {

            // If a single pick has already been made.
            if (picks != null && picks.size() % 2 == 1) {

                Pick latestPick = picks.get(picks.size() - 1);

                // If this user made the latest pick, can't pick again!
                if (AuthHelper.instance().getUserId().equals(latestPick.userId)) {

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

        return status != null &&
               status.equals("completed");
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

        String key = users.get(team);

        final int change;
        if(ratingChange.containsKey(key)) {
            change = ratingChange.get(key);
        } else {
            change = 0;
        }

        return change;
    }

    /**
     * Seconds since draft started.
     *
     * @return Seconds since draft started.
     */
    @SuppressWarnings("unused")
    public int getSecondsSinceStarted() {

        return getSecondsSince(started);
    }

    /**
     * Seconds since last round completed.
     *
     * @return Seconds since last round completed.
     */
    @SuppressWarnings("unused")
    public int getSecondsSinceLastRoundCompleteTime() {

        return getSecondsSince(lastRoundCompleteTime);
    }

    /**
     * Fetch the current round in the draft.
     *
     * @return Draft round.
     */
    @SuppressWarnings("unused")
    public int getCurrentRound() {

        // The round is derived based on the picks so far.
        return (picks == null ? 1 : picks.size() / 2) + 1;
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

        return rosters.get(users.get(team));
    }

    /**
     * Fetch a list of current player choices.
     *
     * @return List of choices.
     */
    @SuppressWarnings("unused")
    public List<String> getCurrentPlayerChoices() {

        if (choices != null && choices.size() > 0) {

            return choices.get(choices.size() - 1);
        }

        return null;
    }

    /**
     * Fetch map of player data.
     *
     * @return Player data.
     */
    @SuppressWarnings("unused")
    public Map<String, RestModelPlayer> getPlayerDataMap() {

        if (playerDataMap == null) {
            playerDataMap = new HashMap<>();
        }

        return playerDataMap;
    }

    /**
     * Set the last time received from the server.
     *
     * @param lastServerTime Last server time.
     */
    @SuppressWarnings("unused")
    public void setLastServerTime(Date lastServerTime) {

        // Update the last server time.
        this.lastServerTime = lastServerTime;

        // Set the offset based on the current client time.
        serverTimeOffset = DateUtils
                .getTimeSinceDateInGMTAsMilliseconds(this.lastServerTime);
    }

    /**
     * Add a new choice to this draft.
     *
     * @param choice Choice object.
     */
    @SuppressWarnings("unused")
    public void addChoice(RestModelPlayer choice) {

        if (playerDataMap == null) {
            playerDataMap = new HashMap<>();
        }

        // Add to player data map.
        playerDataMap.put(choice.getId(), choice);
    }

    /**
     * Add a new list of choices.
     *
     * @param choices List of choices.
     */
    @SuppressWarnings("unused")
    public void addChoices(List<String> choices) {

        if (this.choices == null) {
            this.choices = new ArrayList<>();
        }

        this.choices.add(choices);
    }

    /**
     * Add a pick to the picks array.
     *
     * @param pick Pick object.
     */
    @SuppressWarnings("unused")
    public void addPick(Pick pick) {

        if (picks == null) {
            picks = new ArrayList<>();
        }

        picks.add(pick);
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
                                  @NonNull RestResult<RestModelDraft> callback) {

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
        restAPI.draft_patch(draftId, jsonReplace, new RestAPICallback<>(activity,
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
                                        @NonNull RestResult<RestModelDraft> callback) {

        if (draftId == null) {
            return;
        }

        // Set the start time.
        Date operationStartTime = DateUtils.getDateInGMT();

        // Make api call.
        restAPI.draft_get(draftId, new RestAPICallback<>(activity, result -> {

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
            @NonNull RestResults<RestModelDraft> callback) {

        RestAPICallback<RestAPIResult<RestModelDraft>> operation = new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null);

        // Logout on failure.
        operation.setLogoutOnFailure(true);

        // Filter by currently drafting.
        String filter = "{\"status\": \"drafting\", \"model\": \"heads_up_draft\"}";

        // Sort by most recent.
        String orderBy = "{\"created\": \"ASC\"}";

        restAPI.drafts_get(getKeys(userId), null, "users", filter, orderBy, null, operation);
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
                                          @NonNull RestResults<RestModelDraft> callback) {

        // Create filter for current time frame.
        String filter = "{\"week\": " + week + ", \"year\": " +
                year + ", \"model\": \"heads_up_draft\"}";

        // Order by completed and created descending.
        String orderBy = "{\"completed\": \"DESC\", \"created\": \"DESC\"}";

        restAPI.drafts_get(getKeys(userId), getPluck(), "users",
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
        Date serverTime = lastServerTime != null ? lastServerTime : started;

        if (serverTime != null) {

            // Half half of the request time in milliseconds.
            long halfOfRequestTime = (clientTimeAfterRequest.getTime() -
                    clientTimeBeforeRequest.getTime()) / 2;

            // Now get the exact time at midpoint of request.
            long timeAtMidpointOfRequest = clientTimeBeforeRequest.getTime() +
                    halfOfRequestTime;

            serverTimeOffset = Math.abs(serverTime.getTime() - timeAtMidpointOfRequest);

        } else {

            serverTimeOffset = 0;
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
                (DateUtils.getTimeSinceDateInGMTAsMilliseconds(date) - serverTimeOffset) / 1000.0f);
    }

    // endregion

    // region Inner Class
    // ============================================================================================================

    /**
     * Player pick object.
     */
    public static class Pick {

        @SuppressWarnings("unused") @SerializedName("player_id") @Getter
        private String playerId;
        @SuppressWarnings("unused") @SerializedName("user_id") @Getter
        private String userId;
        @SuppressWarnings("unused") @SerializedName("position")
        private String position;
        @SuppressWarnings("unused") @SerializedName("timestamp")
        private String timestamp;

        @SuppressWarnings("unused") @SerializedName("round")
        private int round;

        /**
         * Create a pick object manually with
         * a bare minimum of objects.
         *
         * @param playerId Player id.
         * @param userId User id.
         */
        @SuppressWarnings("unused")
        public Pick(String playerId, String userId) {

            this.playerId = playerId;
            this.userId = userId;
        }
    }

    // endregion
}