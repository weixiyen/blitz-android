package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nate on 9/21/14. Copyright 2014 Blitz Studios
 *
 * TODO: Revise
 */
public class RestModelGame extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SuppressWarnings("unused") @SerializedName("home_team")  private String mHomeTeam;
    @SuppressWarnings("unused") @SerializedName("away_team")  private String mAwayTeam;
    @SuppressWarnings("unused") @SerializedName("status")     private String mStatus;
    @SuppressWarnings("unused") @SerializedName("score_home") private int mScoreHome;
    @SuppressWarnings("unused") @SerializedName("score_away") private int mScoreAway;

    private static Map<Pair<Integer, Integer>, List<RestModelGame>> mCache =
            new ConcurrentHashMap<>();

    private enum GameStatus {
        STATUS_PREGAME,
        STATUS_IN_PROGRESS,
        STATUS_FINAL
    }

    // endregion

    // region REST Methods
    // ============================================================================================================

    /**
     * Fetch games for a given time.
     */
    @SuppressWarnings("unused")
    public static void fetchGames(Activity activity, int year, int week,
                                  @NonNull RestCallbacks<RestModelGame> callback) {

        final Pair<Integer, Integer> cacheKey = Pair.create(year, week);

        if (mCache.containsKey(cacheKey)) {
            callback.onSuccess(mCache.get(cacheKey));

        } else {

            mRestAPI.games_get(String.valueOf(year) + "_" + week, new RestAPICallback<>(activity, result -> {

                // Insert into cache.
                mCache.put(cacheKey, result.getResults());

                // Invoke success callback.
                callback.onSuccess(result.getResults());

            }, null));
        }
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    @SuppressWarnings("unused")
    public String getHomeTeamName() {
        return mHomeTeam;
    }

    @SuppressWarnings("unused")
    public String getAwayTeamName() {
        return mAwayTeam;
    }

    @SuppressWarnings("unused")
    public int getHomeTeamScore() {
        return mScoreHome;
    }

    @SuppressWarnings("unused")
    public int getAwayTeamScore() {
        return mScoreAway;
    }

    @SuppressWarnings("unused")
    public GameStatus getStatus() {
        if (mStatus.equals(GameStatus.STATUS_FINAL.name())) {
            return GameStatus.STATUS_FINAL;
        } else if (mStatus.equals(GameStatus.STATUS_IN_PROGRESS.name())) {
            return GameStatus.STATUS_IN_PROGRESS;
        } else {
            return GameStatus.STATUS_PREGAME;
        }
    }

    // endregion
}
