package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelGame;
import com.blitz.app.rest_models.RestModelItem;
import com.blitz.app.rest_models.RestModelPlayer;
import com.blitz.app.rest_models.RestModelStats;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.simple_models.Game;
import com.blitz.app.simple_models.Stat;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View model for draft detail page.
 *
 * Created by Nate on 9/10/14.
 */
public class ViewModelMatchup extends ViewModel {

    // region Member Variables
    // =============================================================================================

    RestModelUser mPlayer1;
    RestModelUser mPlayer2;
    List<RestModelPlayer>   mRoster1;
    List<RestModelPlayer>   mRoster2;
    String                    mAvatarUrl1;
    String                    mAvatarUrl2;
    List<Game>                mGames;
    private RestModelDraft mDraft;
    private Multimap<String, Stat> mPlayerStatsMap;
    private boolean           mInitialized = false;

    // endregion

    /**
     * Disallow the default constructor.
     */
    @SuppressWarnings("unused")
    private ViewModelMatchup(Activity activity, ViewModelDraftDetailCallbacks callbacks) {
        super(activity, callbacks);
    }

    /**
     * Use default constructor, but also provide a draft id.
     *
     * @param activity Activity.
     * @param callbacks Callbacks.
     * @param draftId Associated draft id.
     */
    @SuppressWarnings("unused")
    public ViewModelMatchup(Activity activity,
                            final ViewModelDraftDetailCallbacks callbacks, String draftId) {
        super(activity, callbacks);

        RestModelDraft.fetchSyncedDraft(mActivity, draftId, new RestModelCallback<RestModelDraft>() {
            @Override
            public void onSuccess(final RestModelDraft draft) {

                mDraft = draft;

                final List<String> playerIds = new ArrayList<String>(draft.getPicks().size());

                for (RestModelDraft.Pick pick : draft.getPicks()) {
                    playerIds.add(pick.getPlayerId());
                }

                RestModelPlayer.fetchPlayers(null, playerIds,
                        new RestModelPlayer.CallbackPlayers() {

                            @Override
                            public void onSuccess(List<RestModelPlayer> players) {

                                Map<String, RestModelPlayer> playerMap =
                                        new HashMap<String, RestModelPlayer>();

                                for (RestModelPlayer player : players) {

                                    playerMap.put(player.getId(), player);
                                }

                                populateRosters(playerMap);
                                onSyncComplete(callbacks);
                            }
                        });

                // TODO: if SIMULATE

                final int week = draft.getWeek();
                final int year = draft.getYear();

                RestModelGame.fetchGames(year, week, new RestAPICallback<List<Game>>(null) {
                    @Override
                    public void success(List<Game> games) {

                        mGames = games;
                        onSyncComplete(callbacks);
                    }
                });

                RestModelStats.fetchStatsForPlayers(playerIds, year, week,
                        new RestAPICallback<RestAPIResult<Stat>>(null) {

                            @Override
                            public void success(RestAPIResult<Stat> stats) {

                                mPlayerStatsMap = buildPlayerStatsMap(stats.getResults());
                                onSyncComplete(callbacks);
                            }
                        });

                RestModelUser.getUsers(null, mDraft.getUsers(), new RestModelUser.CallbackUsers() {
                    @Override
                    public void onSuccess(List<RestModelUser> users) {

                        for (RestModelUser user : users) {
                            if (user.getId().equals(AuthHelper.instance().getUserId())) {
                                mPlayer1 = user;
                            } else {
                                mPlayer2 = user;
                            }
                        }

                        RestModelItem.fetchAvatars(null,
                                Arrays.asList(mPlayer1.getAvatarId(), mPlayer2.getAvatarId()),
                                new RestModelItem.CallbackItems() {
                                    @Override
                                    public void onSuccess(List<RestModelItem> items) {
                                        for (RestModelItem item : items) {
                                            if (mPlayer1.getAvatarId().equals(item.getId())) {
                                                mAvatarUrl1 = item.getDefaultImgPath();
                                            } else {
                                                mAvatarUrl2 = item.getDefaultImgPath();
                                            }
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    public void initialize() {

    }

    private synchronized void onSyncComplete(ViewModelDraftDetailCallbacks callbacks) {

        if (!mInitialized && // only do this once
                mPlayer1 != null && mPlayer2 != null &&
                mRoster1 != null && mRoster2 != null &&
                mGames != null &&
                mDraft != null &&
                mAvatarUrl1 != null && mAvatarUrl2 != null &&
                mPlayerStatsMap != null) {

            List<Game> p1Games = getPlayerGames(mRoster1, mGames);
            List<Game> p2Games = getPlayerGames(mRoster2, mGames);
            float p1Score = getScore(mRoster1, mPlayerStatsMap);
            float p2Score = getScore(mRoster2, mPlayerStatsMap);

            callbacks.onMatchup(mPlayer1.getUsername(), p1Score,
                                mPlayer2.getUsername(), p2Score);
            callbacks.onStuff(mRoster1, mRoster2, p1Games, p2Games,
                    mPlayerStatsMap, mDraft.getWeek());

            callbacks.onAvatars(mAvatarUrl1, mAvatarUrl2);

            mInitialized = true;
        }
    }

    private static float getScore(List<RestModelPlayer> roster, Multimap<String, Stat> playerStats) {

        float total = 0;

        for (RestModelPlayer player: roster) {
            for(Stat stat: playerStats.get(player.getId())) {
                total += stat.getPoints();
            }
        }

        return total;
    }

    private void populateRosters(Map<String, RestModelPlayer> playersDict) {

        final List<RestModelPlayer> myPicks = new ArrayList<RestModelPlayer>();
        final List<RestModelPlayer> opponentPicks = new ArrayList<RestModelPlayer>();

        for(Map.Entry<String, List<String>> entry : mDraft.getRosters().entrySet()) {

            for(String playerId : entry.getValue()) {

                if (!playersDict.containsKey(playerId)) {
                    continue;
                }

                String userId = entry.getKey();

                if (userId.equals(AuthHelper.instance().getUserId())) {

                    myPicks.add(playersDict.get(playerId));
                } else {

                    opponentPicks.add(playersDict.get(playerId));
                }
            }
        }


        mRoster1 = myPicks;
        mRoster2 = opponentPicks;
    }

    private List<Game> getPlayerGames(List<RestModelPlayer> roster, List<Game> games) {

        List<Game> gamesForPlayers = new ArrayList<Game>(roster.size());

        for(RestModelPlayer player : roster) {
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

    private Multimap<String, Stat> buildPlayerStatsMap(List<Stat> stats) {

        Multimap<String, Stat> map = ArrayListMultimap.create();
        for(Stat stat: stats) {
            if(stat.isSupported()) {
                map.put(stat.getPlayerId(), stat);
            }
        }

        return map;
    }

    public interface ViewModelDraftDetailCallbacks extends ViewModelCallbacks {

        void onStuff(List<RestModelPlayer> p1roster, List<RestModelPlayer> p2Roster, List<Game> p1Games,
                     List<Game> p2Games,
                     Multimap<String, Stat> playerStats, int week);

        void onMatchup(String player1Name, float player1score, String player2Name, float player2Score);

        void onAvatars(String player1AvatarUrl, String player2AvatarUrl);
    }
}
