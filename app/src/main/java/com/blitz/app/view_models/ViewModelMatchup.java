package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelCallbacks;
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

    // Associated users.
    private RestModelUser mPlayer1;
    private RestModelUser mPlayer2;

    // Associated user avatars.
    private String mPlayer1AvatarUrl;
    private String mPlayer2AvatarUrl;

    List<RestModelPlayer>   mRoster1;
    List<RestModelPlayer>   mRoster2;

    List<Game>                mGames;
    private RestModelDraft mDraft;
    private Multimap<String, Stat> mPlayerStatsMap;

    // endregion

    // region Constructors
    // =============================================================================================

    /**
     * Disallow the default constructor.
     */
    @SuppressWarnings("unused")
    private ViewModelMatchup(Activity activity, ViewModelMatchupCallbacks callbacks) {
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
                            final ViewModelMatchupCallbacks callbacks, String draftId) {
        super(activity, callbacks);

        RestModelDraft.fetchSyncedDraft(mActivity, draftId,
                new RestModelCallback<RestModelDraft>() {

            @Override
            public void onSuccess(final RestModelDraft draft) {

                // Set the draft.
                mDraft = draft;

                // TODO: Split up.
                setupModel();
                setupUserInfo();
            }
        });
    }

    // endregion

    @Override
    public void initialize() {

    }

    // region Setup Methods
    // =============================================================================================


    private void setupUserInfo() {

        RestModelUser.getUsers(null, mDraft.getUsers(), new RestModelCallbacks<RestModelUser>() {

            @Override
            public void onSuccess(List<RestModelUser> users) {

                for (RestModelUser user : users) {
                    if (user.getId().equals(AuthHelper.instance().getUserId())) {
                        mPlayer1 = user;
                    } else {
                        mPlayer2 = user;
                    }
                }

                // Fetch associated avatar ids.
                List<String> playerAvatarIds = Arrays.asList(
                        mPlayer1.getAvatarId(),
                        mPlayer2.getAvatarId());

                // Fetch associated items.
                RestModelItem.fetchItems(null, playerAvatarIds,
                        new RestModelItem.CallbackItems() {

                            @Override
                            public void onSuccess(List<RestModelItem> items) {
                                for (RestModelItem item : items) {

                                    if (mPlayer1.getAvatarId().equals(item.getId())) {
                                        mPlayer1AvatarUrl = item.getDefaultImgPath();
                                    } else {
                                        mPlayer2AvatarUrl = item.getDefaultImgPath();
                                    }

                                    onSyncComplete();
                                }
                            }
                        });
            }
        });
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    private void setupModel() {

        final List<String> playerIds = new ArrayList<String>();

        for (RestModelDraft.Pick pick : mDraft.getPicks()) {
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
                        onSyncComplete();
                    }
                });


        final int week = mDraft.getWeek();
        final int year = mDraft.getYear();

        RestModelGame.fetchGames(year, week, new RestAPICallback<List<Game>>(null) {
            @Override
            public void success(List<Game> games) {

                mGames = games;
                onSyncComplete();
            }
        });

        RestModelStats.fetchStatsForPlayers(playerIds, year, week,
                new RestAPICallback<RestAPIResult<Stat>>(null) {

                    @Override
                    public void success(RestAPIResult<Stat> stats) {

                        mPlayerStatsMap = buildPlayerStatsMap(stats.getResults());
                        onSyncComplete();
                    }
                });
    }

    private synchronized void onSyncComplete() {

        final ViewModelMatchupCallbacks callbacks =
                getCallbacks(ViewModelMatchupCallbacks.class);

        if (mPlayer1 != null && mPlayer2 != null &&
            mRoster1 != null && mRoster2 != null &&
            mGames != null &&
            mDraft != null &&
            mPlayer1AvatarUrl != null && mPlayer2AvatarUrl != null &&
            mPlayerStatsMap != null) {

            List<Game> p1Games = getPlayerGames(mRoster1, mGames);
            List<Game> p2Games = getPlayerGames(mRoster2, mGames);
            float p1Score = getScore(mRoster1, mPlayerStatsMap);
            float p2Score = getScore(mRoster2, mPlayerStatsMap);

            callbacks.onMatchup(mPlayer1.getUsername(), p1Score,
                                mPlayer2.getUsername(), p2Score);

            callbacks.onStuff(mRoster1, mRoster2, p1Games, p2Games,
                    mPlayerStatsMap, mDraft.getWeek());

            callbacks.onAvatars(mPlayer1AvatarUrl, mPlayer2AvatarUrl);
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

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    /**
     * Callbacks.
     */
    public interface ViewModelMatchupCallbacks extends ViewModelCallbacks {

        void onStuff(List<RestModelPlayer> p1roster, List<RestModelPlayer> p2Roster, List<Game> p1Games,
                     List<Game> p2Games,
                     Multimap<String, Stat> playerStats, int week);

        void onMatchup(String player1Name, float player1score, String player2Name, float player2Score);

        void onAvatars(String player1AvatarUrl, String player2AvatarUrl);
    }

    // endregion
}
