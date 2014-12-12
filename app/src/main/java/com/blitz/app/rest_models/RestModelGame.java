package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

/**
 * Created by Nate on 9/21/14. Copyright 2014 Blitz Studios
 *
 * TODO: Revise
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelGame extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("home_team")  @Getter private String homeTeam;
    @SerializedName("away_team")  @Getter private String awayTeam;
    @SerializedName("status")     @Getter private String status;
    @SerializedName("score_home") @Getter private int scoreHome;
    @SerializedName("score_away") @Getter private int scoreAway;

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
    public static void fetchGames(Activity activity, int year, int week,
                                  @NonNull RestResults<RestModelGame> callback) {

        final Pair<Integer, Integer> cacheKey = Pair.create(year, week);

        if (mCache.containsKey(cacheKey)) {
            callback.onSuccess(mCache.get(cacheKey));

        } else {

            restAPI.games_get(String.valueOf(year) + "_" + week, new RestAPICallback<>(activity, result -> {

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

    /**
     * Fetch game status string.
     */
    public GameStatus getStatusEnum() {

        if (status.equals(GameStatus.STATUS_FINAL.name())) {
            return GameStatus.STATUS_FINAL;
        } else if (status.equals(GameStatus.STATUS_IN_PROGRESS.name())) {
            return GameStatus.STATUS_IN_PROGRESS;
        } else {
            return GameStatus.STATUS_PREGAME;
        }
    }

    // endregion
}