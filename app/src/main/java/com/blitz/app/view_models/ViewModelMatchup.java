package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestResult;
import com.blitz.app.rest_models.RestResults;
import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelGame;
import com.blitz.app.rest_models.RestModelItem;
import com.blitz.app.rest_models.RestModelPlayer;
import com.blitz.app.rest_models.RestModelStats;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;

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
    // ============================================================================================================

    // Associated users.
    private RestModelUser mPlayer1;
    private RestModelUser mPlayer2;

    // Associated user avatars.
    private String mPlayer1AvatarUrl;
    private String mPlayer2AvatarUrl;

    List<RestModelPlayer>   mRoster1;
    List<RestModelPlayer>   mRoster2;

    List<RestModelGame> mGames;

    private RestModelDraft mDraft;
    private Map<String, List<RestModelStats>> mPlayerStatsMap;

    private String mDraftId;

    // endregion

    // region Constructors
    // ============================================================================================================

    /**
     * Disallow the default constructor.
     */
    @SuppressWarnings("unused")
    private ViewModelMatchup(BaseActivity activity, Callbacks callbacks) {
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
    public ViewModelMatchup(BaseActivity activity, final Callbacks callbacks, String draftId) {
        super(activity, callbacks);

        mDraftId = draftId;
    }

    // endregion

    @Override
    public void initialize() {

        RestModelDraft.fetchSyncedDraft(mActivity, mDraftId,
                new RestResult<RestModelDraft>() {

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

    // region Setup Methods
    // ============================================================================================================


    private void setupUserInfo() {

        RestModelUser.getUsers(null, mDraft.getUsers(), new RestResults<RestModelUser>() {

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
                        items -> {
                            for (RestModelItem item : items) {

                                if (mPlayer1.getAvatarId().equals(item.getId())) {
                                    mPlayer1AvatarUrl = item.getDefaultImgPath();
                                } else {
                                    mPlayer2AvatarUrl = item.getDefaultImgPath();
                                }

                                onSyncComplete();
                            }
                        });
            }
        });
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    private void setupModel() {

        final List<String> playerIds = new ArrayList<>();

        for (RestModelDraft.Pick pick : mDraft.getPicks()) {
            playerIds.add(pick.getPlayerId());
        }

        RestModelPlayer.fetchPlayers(null, playerIds,
                players -> {

                    Map<String, RestModelPlayer> playerMap = new HashMap<>();

                    for (RestModelPlayer player : players) {

                        playerMap.put(player.getId(), player);
                    }

                    populateRosters(playerMap);
                    onSyncComplete();
                });


        final int week = mDraft.getWeek();
        final int year = mDraft.getYear();

        RestModelGame.fetchGames(mActivity, year, week, new RestResults<RestModelGame>() {

            @Override
            public void onSuccess(List<RestModelGame> object) {

                mGames = object;
                onSyncComplete();
            }
        });

        RestModelStats.fetchStatsForPlayers(mActivity, playerIds, year, week, new RestResults<RestModelStats>() {
            @Override
            public void onSuccess(List<RestModelStats> object) {

                mPlayerStatsMap = buildPlayerStatsMap(object);
                onSyncComplete();
            }
        });
    }

    private synchronized void onSyncComplete() {

        final Callbacks callbacks = getCallbacks(Callbacks.class);

        if (mPlayer1 != null && mPlayer2 != null &&
            mRoster1 != null && mRoster2 != null &&
            mGames != null &&
            mDraft != null &&
            mPlayer1AvatarUrl != null && mPlayer2AvatarUrl != null &&
            mPlayerStatsMap != null) {

            List<RestModelGame> p1Games = getPlayerGames(mRoster1, mGames);
            List<RestModelGame> p2Games = getPlayerGames(mRoster2, mGames);
            float p1Score = getScore(mRoster1, mPlayerStatsMap);
            float p2Score = getScore(mRoster2, mPlayerStatsMap);

            callbacks.onMatchup(mPlayer1.getUsername(), p1Score,
                                mPlayer2.getUsername(), p2Score);

            callbacks.onStuff(mRoster1, mRoster2, p1Games, p2Games,
                    mPlayerStatsMap, mDraft.getWeek());

            callbacks.onAvatars(mPlayer1AvatarUrl, mPlayer2AvatarUrl);
        }
    }

    private static float getScore(List<RestModelPlayer> roster, Map<String, List<RestModelStats>> playerStats) {

        float total = 0;

        for (RestModelPlayer player: roster) {
            if (playerStats.containsKey(player.getId())) {

                for (RestModelStats stat: playerStats.get(player.getId())) {
                    total += stat.getTypePoints();
                }
            }
        }

        return total;
    }

    private void populateRosters(Map<String, RestModelPlayer> playersDict) {

        final List<RestModelPlayer> myPicks = new ArrayList<>();
        final List<RestModelPlayer> opponentPicks = new ArrayList<>();

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

    private List<RestModelGame> getPlayerGames(List<RestModelPlayer> roster, List<RestModelGame> games) {

        List<RestModelGame> gamesForPlayers = new ArrayList<>(roster.size());

        for(RestModelPlayer player : roster) {
            RestModelGame playerGame = new RestModelGame();
            for(RestModelGame game: games) {
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

    private Map<String, List<RestModelStats>> buildPlayerStatsMap(List<RestModelStats> stats) {

        Map<String, List<RestModelStats>> map = new HashMap<>();

        for (RestModelStats stat: stats) {

            if (stat.isSupported()) {
                List<RestModelStats> playerStats;

                if (map.containsKey(stat.getPlayerId())) {
                    playerStats = map.get(stat.getPlayerId());
                } else {
                    playerStats = new ArrayList<>();
                }

                playerStats.add(stat);

                map.put(stat.getPlayerId(), playerStats);
            }
        }

        return map;
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    /**
     * Callbacks.
     */
    public interface Callbacks extends ViewModel.Callbacks {

        void onStuff(List<RestModelPlayer> p1roster, List<RestModelPlayer> p2Roster, List<RestModelGame> p1Games,
                     List<RestModelGame> p2Games,
                     Map<String, List<RestModelStats>> playerStats, int week);

        void onMatchup(String player1Name, float player1score, String player2Name, float player2Score);

        void onAvatars(String player1AvatarUrl, String player2AvatarUrl);
    }

    // endregion
}
