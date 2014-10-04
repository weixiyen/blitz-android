package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;

import com.blitz.app.object_models.ObjectModelDraft;
import com.blitz.app.object_models.ObjectModelGame;
import com.blitz.app.object_models.ObjectModelPlayer;
import com.blitz.app.object_models.ObjectModelStats;
import com.blitz.app.screens.main.MatchInfoAdapter;
import com.blitz.app.simple_models.Game;
import com.blitz.app.simple_models.Stat;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View model for draft detail page.
 *
 * Created by Nate on 9/10/14.
 */
public class ViewModelDraftDetail extends ViewModel {

    List<ObjectModelPlayer>   mRoster1;
    List<ObjectModelPlayer>   mRoster2;
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

        ObjectModelDraft.fetchSyncedDraft(mActivity, draftId, new ObjectModelDraft.DraftCallback() {
            @Override
            public void onSuccess(final ObjectModelDraft draft) {

                mDraft = draft;

                final List<String> playerIds = new ArrayList<String>(draft.getPicks().size());

                for (ObjectModelDraft.Pick pick : draft.getPicks()) {
                    playerIds.add(pick.getPlayerId());
                }

                ObjectModelPlayer.fetchPlayers(mActivity, playerIds,
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

                ObjectModelGame.fetchGames(year, week, new RestAPICallback<List<Game>>(mActivity) {
                    @Override
                    public void success(List<Game> games) {

                        mGames = games;
                        onSyncComplete(callbacks);
                    }
                });

                ObjectModelStats.fetchStatsForPlayers(playerIds, year, week,
                        new RestAPICallback<RestAPIResult<Stat>>(mActivity) {

                    @Override
                    public void success(RestAPIResult<Stat> stats) {

                        mPlayerStatsMap = buildPlayerStatsMap(stats.getResults());
                        onSyncComplete(callbacks);
                    }
                });
            }
        });
    }

    private synchronized void onSyncComplete(ViewModelDraftDetailCallbacks callbacks) {

        if (!mInitialized &&
                mRoster1 != null && mRoster2 != null &&
                mGames != null &&
                mDraft != null &&
                mPlayerStatsMap != null) {

            List<Game> player1Games = getPlayerGames(mRoster1, mGames);
            List<Game> player2Games = getPlayerGames(mRoster2, mGames);

            callbacks.onStuff(mRoster1, mRoster2, player1Games, player2Games,
                    mPlayerStatsMap, mDraft.getWeek());

            mInitialized = true;
        }
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
