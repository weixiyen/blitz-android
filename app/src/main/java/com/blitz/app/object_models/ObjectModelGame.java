package com.blitz.app.object_models;

import android.util.Pair;

import com.blitz.app.simple_models.Game;
import com.blitz.app.utilities.rest.RestAPI;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.blitz.app.view_models.ViewModelDraftDetail.ViewModelDraftDetailCallbacks;
import com.google.android.gms.maps.MapsInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Nate on 9/21/14.
 */
public class ObjectModelGame extends ObjectModel {

    private static Map<Pair<Integer, Integer>, List<Game>> mCache =
            new ConcurrentHashMap<Pair<Integer, Integer>, List<Game>>();

    public static void fetchGames(int year, int week,
                                final ViewModelDraftDetailCallbacks callback) {

        final Pair<Integer, Integer> cacheKey = Pair.create(year, week);
        if(mCache.containsKey(cacheKey)) {
            callback.onGames(mCache.get(cacheKey));

        } else {

            Callback<RestAPIResult<Game>> apiCallback = new Callback<RestAPIResult<Game>>() {

                @Override
                public void success(RestAPIResult restAPIResult, Response response) {

                    mCache.put(cacheKey, restAPIResult.getResults());
                    callback.onGames(restAPIResult.getResults());
                }

                @Override
                public void failure(RetrofitError error) {

                    error.printStackTrace(); // TODO: this should be handled by the RestAPICallback
                }
            };

            mRestAPI.games_get(year, week, apiCallback);
        }
    }
}
