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
}
