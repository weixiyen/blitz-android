package com.blitz.app.object_models;

import android.util.Pair;

import com.blitz.app.simple_models.Player;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.blitz.app.view_models.ViewModelDraftDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;

/**
 * Created by spiff on 9/18/14.
 */
public class ObjectModelStats extends ObjectModel {
    private String player_id;

    public String getUserId() {
        return player_id;
    }

    public static void fetchRoster(List<String> playerIds, final Callback<RestAPIResult<Player>> playerCallback) {

        mRestAPI.players_get(playerIds, playerCallback);
    }

    public static void fetchStats(String[] player1Roster, String[] player2Roster, final ViewModelDraftDetail.ViewModelDraftDetailCallbacks callbacks) {

        RestAPICallback<RestAPIResult<ObjectModelStats>> operation =
                new RestAPICallback<RestAPIResult<ObjectModelStats>>(null) {

            @Override
            public void success(RestAPIResult<ObjectModelStats> operation) {

                List<Pair<Player, Player>> players = new ArrayList<Pair<Player, Player>>();

                for (ObjectModelStats stat : operation.getResults()) {

                    Player p = new Player(stat.getUserId(), "test", "test2", 0.0f);
                    players.add(Pair.create(p, p));
                }
                callbacks.onPlayers(players);
            }
        };

        mRestAPI.test_stats_get(operation);
    }
}
