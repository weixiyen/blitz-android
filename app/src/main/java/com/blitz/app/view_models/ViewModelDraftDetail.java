package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;

import com.blitz.app.object_models.ObjectModelDraft;
import com.blitz.app.object_models.ObjectModelGame;
import com.blitz.app.object_models.ObjectModelItem;
import com.blitz.app.object_models.ObjectModelPlayer;
import com.blitz.app.object_models.ObjectModelStats;
import com.blitz.app.object_models.ObjectModelUser;
import com.blitz.app.object_models.RestModelCallback;
import com.blitz.app.screens.main.MatchInfoAdapter;
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
public class ViewModelDraftDetail extends ViewModel {

    ObjectModelUser           mPlayer1;
    ObjectModelUser           mPlayer2;
    List<ObjectModelPlayer>   mRoster1;
    List<ObjectModelPlayer>   mRoster2;
    String                    mAvatarUrl1;
    String                    mAvatarUrl2;
    List<Game>                mGames;
    private ObjectModelDraft  mDraft;
    private Multimap<String, Stat> mPlayerStatsMap;
    private boolean           mInitialized = false;

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

        if(mInitialized) return;

        final Bundle extras = mActivity.getIntent().getExtras();
        final String draftId = extras.getString(MatchInfoAdapter.DRAFT_ID);

        final ViewModelDraftDetailCallbacks callbacks = getCallbacks(ViewModelDraftDetailCallbacks.class);

        ObjectModelDraft.fetchSyncedDraft(mActivity, draftId, new RestModelCallback<ObjectModelDraft>() {
            @Override
            public void onSuccess(final ObjectModelDraft draft) {

                mDraft = draft;

                final List<String> playerIds = new ArrayList<String>(draft.getPicks().size());

                for (ObjectModelDraft.Pick pick : draft.getPicks()) {
                    playerIds.add(pick.getPlayerId());
                }

                ObjectModelPlayer.fetchPlayers(null, playerIds,
                        new ObjectModelPlayer.CallbackPlayers() {

                            @Override
                            public void onSuccess(List<ObjectModelPlayer> players) {

                                Map<String, ObjectModelPlayer> playerMap =
                                        new HashMap<String, ObjectModelPlayer>();

                                for (ObjectModelPlayer player: players) {

                                    playerMap.put(player.getId(), player);
                                }

                                populateRosters(playerMap);
                                onSyncComplete(callbacks);
                            }
                        });

                // TODO: if SIMULATE

                final int week = draft.getWeek();
                final int year = draft.getYear();

                ObjectModelGame.fetchGames(year, week, new RestAPICallback<List<Game>>(null) {
                    @Override
                    public void success(List<Game> games) {

                        mGames = games;
                        onSyncComplete(callbacks);
                    }
                });

                ObjectModelStats.fetchStatsForPlayers(playerIds, year, week,
                        new RestAPICallback<RestAPIResult<Stat>>(null) {

                    @Override
                    public void success(RestAPIResult<Stat> stats) {

                        mPlayerStatsMap = buildPlayerStatsMap(stats.getResults());
                        onSyncComplete(callbacks);
                    }
                });

                ObjectModelUser.getUsers(null, mDraft.getUsers(), new ObjectModelUser.CallbackUsers() {
                    @Override
                    public void onSuccess(List<ObjectModelUser> users) {

                        for(ObjectModelUser user: users) {
                            if(user.getId().equals(AuthHelper.instance().getUserId())) {
                                mPlayer1 = user;
                            } else {
                                mPlayer2 = user;
                            }
                        }

                        ObjectModelItem.fetchAvatars(null,
                                Arrays.asList(mPlayer1.getAvatarId(), mPlayer2.getAvatarId()),
                                new ObjectModelItem.CallbackItems() {
                                    @Override
                                    public void onSuccess(List<ObjectModelItem> items) {
                                        for (ObjectModelItem item: items) {
                                            if(mPlayer1.getAvatarId().equals(item.getId())) {
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

    private static float getScore(List<ObjectModelPlayer> roster, Multimap<String, Stat> playerStats) {

        float total = 0;

        for (ObjectModelPlayer player: roster) {
            for(Stat stat: playerStats.get(player.getId())) {
                total += stat.getPoints();
            }
        }

        return total;
    }

    private void populateRosters(Map<String, ObjectModelPlayer> playersDict) {

        final List<ObjectModelPlayer> myPicks = new ArrayList<ObjectModelPlayer>();
        final List<ObjectModelPlayer> opponentPicks = new ArrayList<ObjectModelPlayer>();

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

        void onStuff(List<ObjectModelPlayer> p1roster, List<ObjectModelPlayer> p2Roster, List<Game> p1Games,
                     List<Game> p2Games,
                     Multimap<String, Stat> playerStats, int week);

        void onMatchup(String player1Name, float player1score, String player2Name, float player2Score);

        void onAvatars(String player1AvatarUrl, String player2AvatarUrl);
    }
}
