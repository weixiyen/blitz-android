package com.blitz.app.object_models;

import android.util.Pair;

import com.blitz.app.simple_models.Player;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.blitz.app.view_models.ViewModelDraftDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by spiff on 9/18/14.
 */
public class ObjectModelStats extends ObjectModel {
    private String player_id;

    public String getUserId() {
        return player_id;
    }

    public static void fetchStats(final ViewModelDraftDetail.ViewModelDraftDetailCallbacks callbacks) {

        mRestAPI.test_draft_get(new Callback<RestAPIResult<ObjectModelStats>>() {

            @Override
            public void success(RestAPIResult<ObjectModelStats> objectModelStats, Response response) {

                List<Pair<Player, Player>> players = new ArrayList<Pair<Player, Player>>();
                for(ObjectModelStats stat : objectModelStats.getResults()) {
                    Player p = new Player(stat.getUserId(), "test", "test2", 0.0f);
                    players.add(Pair.create(p, p));
                }
                callbacks.onPlayers(players);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
