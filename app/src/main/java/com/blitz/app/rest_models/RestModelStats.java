package com.blitz.app.rest_models;

import android.support.annotation.NonNull;

import com.blitz.app.simple_models.Stat;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nate on 9/18/14. Copyright 2014 Blitz Studios
 */
public class RestModelStats extends RestModel {

    // region REST Methods
    // ============================================================================================================

    /**
     * Fetch stats for a given list of players.
     */
    public static void fetchStatsForPlayers(List<String> playerIds, int year, int week,
                                            @NonNull RestAPICallback<RestAPIResult<Stat>> callback) {

        List<String> keys = new ArrayList<>(playerIds.size());

        for(String id: playerIds) {
            keys.add(id + "_" + year + "_" + week);
        }
        mRestAPI.stats_get(keys, "player_year_week_index",
                null,
                100,
                callback);
    }

    // endregion
}
