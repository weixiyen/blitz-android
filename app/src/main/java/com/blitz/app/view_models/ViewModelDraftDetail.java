package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;

import com.blitz.app.object_models.ObjectModelStats;
import com.blitz.app.screens.main.MatchInfoAdapter;
import com.blitz.app.simple_models.Player;
import com.blitz.app.utilities.rest.RestAPIResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * View model for draft detail page.
 *
 * Created by Nate on 9/10/14.
 */
public class ViewModelDraftDetail extends ViewModel {

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelDraftDetail(Activity activity, ViewModelDraftDetailCallbacks callbacks) {
        super(activity, callbacks);
    }

    @Override
    public void initialize() {

        final Bundle extras = mActivity.getIntent().getExtras();
        final String[] player1ids = (String[]) extras.get(MatchInfoAdapter.PLAYER_1_ROSTER);
        final String[] player2ids = (String[]) extras.get(MatchInfoAdapter.PLAYER_2_ROSTER);

        ObjectModelStats.fetchRoster(Arrays.asList(player1ids), new Callback<RestAPIResult<Player>>() {
            @Override
            public void success(final RestAPIResult<Player> player1Result, Response response) {

                ObjectModelStats.fetchRoster(Arrays.asList(player2ids), new Callback<RestAPIResult<Player>>() {
                    @Override
                    public void success(RestAPIResult<Player> player2Result, Response response) {
                        final List<Player> p1roster = player1Result.getResults();
                        final List<Player> p2roster = player2Result.getResults();

                        ViewModelDraftDetailCallbacks callbacks =
                                getCallbacks(ViewModelDraftDetailCallbacks.class);

                        final List<Pair<Player, Player>> players = new ArrayList<Pair<Player, Player>>();
                        for(int i=0; i < p1roster.size(); i++) {
                            players.add(Pair.create(p1roster.get(i), p2roster.get(i)));
                        }

                        callbacks.onPlayers(players);
                        callbacks.onMatchup(extras.getString(MatchInfoAdapter.PLAYER_1_NAME),
                                extras.getFloat(MatchInfoAdapter.PLAYER_1_SCORE),
                                extras.getString(MatchInfoAdapter.PLAYER_2_NAME),
                                extras.getFloat(MatchInfoAdapter.PLAYER_2_SCORE));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public interface ViewModelDraftDetailCallbacks extends ViewModelCallbacks {

        void onPlayers(List<Pair<Player, Player>> players);
        void onMatchup(String player1Name, float player1score, String player2Name, float player2Score);
    }
}
