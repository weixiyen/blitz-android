package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;

import com.blitz.app.object_models.ObjectModelGame;
import com.blitz.app.object_models.ObjectModelStats;
import com.blitz.app.screens.main.MatchInfoAdapter;
import com.blitz.app.simple_models.Game;
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
        final String[] player1ids = extras.getStringArray(MatchInfoAdapter.PLAYER_1_ROSTER);
        final String[] player2ids = extras.getStringArray(MatchInfoAdapter.PLAYER_2_ROSTER);
        final int week = extras.getInt(MatchInfoAdapter.WEEK);
        final int year = extras.getInt(MatchInfoAdapter.YEAR);

        final ViewModelDraftDetailCallbacks callbacks =
                getCallbacks(ViewModelDraftDetailCallbacks.class);

        ObjectModelStats.fetchRoster(Arrays.asList(player1ids), new Callback<RestAPIResult<Player>>() {
            @Override
            public void success(final RestAPIResult<Player> player1Result, Response response) {

                ObjectModelStats.fetchRoster(Arrays.asList(player2ids), new Callback<RestAPIResult<Player>>() {
                    @Override
                    public void success(RestAPIResult<Player> player2Result, Response response) {
                        final List<Player> p1roster = player1Result.getResults();
                        final List<Player> p2roster = player2Result.getResults();

                        ObjectModelGame.fetchGames(year, week, new Callback<List<Game>>() {
                            @Override
                            public void success(List<Game> games, Response response) {
                                // build list of game results per pick
                                List<Game> p1Games = getPlayerGames(p1roster, games);
                                List<Game> p2Games = getPlayerGames(p2roster, games);

                                callbacks.onStuff(p1roster, p2roster, p1Games, p2Games);

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

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private List<Game> getPlayerGames(List<Player> roster, List<Game> games) {

        List<Game> gamesForPlayers = new ArrayList<Game>(roster.size());

        for(Player player: roster) {
            Game playerGame = new Game();
            for(Game game: games) {
                if(player.getTeamName().equals(game.getAwayTeamName()) ||
                        player.getTeamName().equals(game.getHomeTeamName())) {
                    playerGame = game;
                    break;
                }
            }
            gamesForPlayers.add(playerGame);
        }

        return gamesForPlayers;
    }

    public interface ViewModelDraftDetailCallbacks extends ViewModelCallbacks {

        void onStuff(List<Player> p1roster, List<Player> p2Roster, List<Game> p1games, List<Game> p2games);
        void onMatchup(String player1Name, float player1score, String player2Name, float player2Score);
    }
}
