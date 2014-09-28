package com.blitz.app.object_models;

import com.blitz.app.simple_models.Stat;
import com.blitz.app.utilities.rest.RestAPIResult;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;

/**
 * Created by Nate on 9/18/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelStats extends ObjectModel {
    private String player_id;

    public String getUserId() {
        return player_id;
    }

    public static void fetchRoster(List<String> playerIds, final Callback<RestAPIResult<ObjectModelPlayer>> playerCallback) {

        mRestAPI.nfl_players_get(playerIds, "id", playerCallback);
    }

    public static void fetchStatsForPlayers(List<String> playerIds, int year, int week, final Callback<RestAPIResult<Stat>> callback) {
        List<String> keys = new ArrayList<String>(playerIds.size());

        for(String id: playerIds) {
            keys.add(id + "_" + year + "_" + week);
        }
        mRestAPI.stats_get(keys, "player_year_week_index",
                null,
                100,
                callback);
    }
}
