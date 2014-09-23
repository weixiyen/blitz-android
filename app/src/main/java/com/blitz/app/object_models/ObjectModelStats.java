package com.blitz.app.object_models;

import android.util.Pair;

import com.blitz.app.simple_models.Player;
import com.blitz.app.simple_models.Stat;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.blitz.app.view_models.ViewModelDraftDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;

/**
 * Created by Nate on 9/18/14.
 */
public class ObjectModelStats extends ObjectModel {
    private String player_id;

    public String getUserId() {
        return player_id;
    }

    public static void fetchRoster(List<String> playerIds, final Callback<RestAPIResult<Player>> playerCallback) {

        mRestAPI.players_get(playerIds, playerCallback);
    }

    public static void fetchStatsForPlayers(List<String> playerIds, int week, int year, final Callback<RestAPIResult<Stat>> callback) {
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
