package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;

import com.blitz.app.object_models.ObjectModelDraft;
import com.blitz.app.object_models.ObjectModelGame;
import com.blitz.app.object_models.ObjectModelItem;
import com.blitz.app.object_models.ObjectModelPlayer;
import com.blitz.app.object_models.ObjectModelStats;
import com.blitz.app.screens.main.MatchInfoAdapter;
import com.blitz.app.simple_models.Game;
import com.blitz.app.simple_models.Stat;
import com.blitz.app.utilities.logging.LogHelper;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
        final String draftId = extras.getString(MatchInfoAdapter.DRAFT_ID);


        final ViewModelDraftDetailCallbacks callbacks =
                getCallbacks(ViewModelDraftDetailCallbacks.class);

        ObjectModelDraft.fetchSyncedDraft(mActivity, draftId, new ObjectModelDraft.DraftCallback() {
            @Override
            public void onSuccess(final ObjectModelDraft draft) {

                final List<String> player1ids = draft.getTeamRoster(0);
                final List<String> player2ids = draft.getTeamRoster(1);
                final int week = draft.getWeek();
                final int year = draft.getYear();

                ObjectModelPlayer.fetchPlayers(mActivity, player1ids,
                        new ObjectModelPlayer.CallbackPlayers() {

                            @Override
                            public void onSuccess(final List<ObjectModelPlayer> p1roster) {

                                ObjectModelPlayer.fetchPlayers(mActivity, player2ids,
                                        new ObjectModelPlayer.CallbackPlayers() {

                                            @Override
                                            public void onSuccess(final List<ObjectModelPlayer> p2roster) {

                                                final List<String> allPlayerIds = new ArrayList<String>();
                                                allPlayerIds.addAll(player1ids);
                                                allPlayerIds.addAll(player2ids);

                                                ObjectModelGame.fetchGames(year, week, new Callback<List<Game>>() {
                                                    @Override
                                                    public void success(final List<Game> games, Response response) {

                                                        ObjectModelStats.fetchStatsForPlayers(allPlayerIds, year, week,
                                                                new Callback<RestAPIResult<Stat>>() {

                                                                    @Override
                                                                    public void success(RestAPIResult<Stat> statRestAPIResult, Response response) {
                                                                        // build list of game results per pick
                                                                        List<Game> p1Games = getPlayerGames(p1roster, games);
                                                                        List<Game> p2Games = getPlayerGames(p2roster, games);

                                                                        Multimap<String, Stat> playerStats = buildPlayerStatsMap(statRestAPIResult.getResults());

                                                                        callbacks.onStuff(p1roster, p2roster, p1Games, p2Games,
                                                                                playerStats, week);

                                                                        ObjectModelItem.fetchAvatars(mActivity, Arrays.asList(
                                                                                draft.getUserInfo(0).getAvatarId(),
                                                                                draft.getUserInfo(1).getAvatarId()),
                                                                                new ObjectModelItem.CallbackItems() {

                                                                            @Override
                                                                            public void onSuccess(List<ObjectModelItem> items) {

                                                                                callbacks.onAvatars(items.get(0).getDefaultImgPath(), items.get(1).getDefaultImgPath());
                                                                            }
                                                                        });

                                                                        callbacks.onMatchup(extras.getString(MatchInfoAdapter.PLAYER_1_NAME),
                                                                                extras.getFloat(MatchInfoAdapter.PLAYER_1_SCORE),
                                                                                extras.getString(MatchInfoAdapter.PLAYER_2_NAME),
                                                                                extras.getFloat(MatchInfoAdapter.PLAYER_2_SCORE)
                                                                        );
                                                                    }

                                                                    @Override
                                                                    public void failure(RetrofitError error) {
                                                                        LogHelper.log(Arrays.toString(error.getStackTrace()));
                                                                    }
                                                                });
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        error.printStackTrace();
                                                    }
                                                });
                                            }
                                        });
                            }
                        });
            }
        });


    }

    private List<Game> getPlayerGames(List<ObjectModelPlayer> roster, List<Game> games) {

        List<Game> gamesForPlayers = new ArrayList<Game>(roster.size());

        for(ObjectModelPlayer player : roster) {
            Game playerGame = new Game();
            for(Game game: games) {
                if(player.getTeam().equals(game.getAwayTeamName()) ||
                        player.getTeam().equals(game.getHomeTeamName())) {
                    playerGame = game;
                    break;
                }
            }
            gamesForPlayers.add(playerGame);
        }

        return gamesForPlayers;
    }

    private static List<Float> getRosterScores(String[] roster, Multimap<String, Stat> stats) {

        List<Float> scores = new ArrayList<Float>(roster.length);
        for(String playerId: roster) {
            Collection<Stat> playerStats = stats.get(playerId);
            float sum = 0;
            for(Stat stat: playerStats) {
                sum += stat.getPoints();
            }
            scores.add(sum);
        }
        return scores;
    }

    private Multimap<String, Stat> buildPlayerStatsMap(List<Stat> stats) {

        Multimap<String, Stat> map = ArrayListMultimap.create();
        for(Stat stat: stats) {
            map.put(stat.getPlayerId(), stat);
        }

        return map;
    }

    public interface ViewModelDraftDetailCallbacks extends ViewModelCallbacks {

        void onStuff(List<ObjectModelPlayer> p1roster, List<ObjectModelPlayer> p2Roster, List<Game> p1Games,
                     List<Game> p2Games,
                     Multimap<String, Stat> playerStats, int week);

        void onMatchup(String player1Name, float player1score, String player2Name, float player2Score);

        void onAvatars(String player1AvatarUrl, String player2AvatarUrl);
    }
}
