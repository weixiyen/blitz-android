package com.blitz.app.screens.matchup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestModelGame;
import com.blitz.app.rest_models.RestModelPlayer;
import com.blitz.app.rest_models.RestModelStats;
import com.blitz.app.screens.stats.StatsBreakdownScreen;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Player list adapter for binding player info to the draft detail UI's ListView.
 *
 * Created by spiff on 9/10/14.
 */
public class PlayerListAdapter extends ArrayAdapter<RestModelPlayer> {

    public static final String STAT_NAMES = "PlayerListAdapter.statNames";
    public static final String STAT_VALUES = "PlayerListAdapter.statValues";
    public static final String STAT_POINTS = "PlayerListAdapter.statPoints";

    private final List<RestModelPlayer> mPlayer1Picks;
    private final List<RestModelPlayer> mPlayer2Picks;
    private final List<RestModelGame>   mPlayer1Games;
    private final List<RestModelGame>   mPlayer2Games;
    private final Map<String, List<RestModelStats>> mPlayerStats;
    private final int mWeek;

    private final Activity mActivity;

    public PlayerListAdapter(Context context, List<RestModelPlayer> player1picks, List<RestModelPlayer> player2picks,
                             List<RestModelGame> player1games, List<RestModelGame> player2games,
                             Map<String, List<RestModelStats>> playerStats,
                             int week,
                             Activity activity) {


        super(context, R.layout.matchup_screen, player1picks);
        mActivity = activity;

        mPlayer1Picks = player1picks;
        mPlayer2Picks = player2picks;
        mPlayer1Games = player1games;
        mPlayer2Games = player2games;
        mPlayerStats = playerStats;
        mWeek = week;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(mActivity)
                    .inflate(R.layout.matchup_screen_draft_list_item, parent, false);
        }

        // TODO: Handle this guard better, it was crashing on player 2 picks out of bounds.
        // TODO: That should never happen as far as I can tell.
        if (mPlayer1Picks.size() > position && mPlayer2Picks.size() > position) {

            final RestModelPlayer p1 = mPlayer1Picks.get(position);
            final RestModelPlayer p2 = mPlayer2Picks.get(position);

            ((TextView) v.findViewById(R.id.player1_name)).setText(p1.getFullName());
            ((TextView) v.findViewById(R.id.player1_position_team)).setText(getPositionTeam(p1));
            ((TextView) v.findViewById(R.id.player2_name)).setText(p2.getFullName());
            ((TextView) v.findViewById(R.id.player2_position_team)).setText(getPositionTeam(p2));

            final String s1 = getScore(p1, mPlayerStats);
            final String s2 = getScore(p2, mPlayerStats);

            ((TextView) v.findViewById(R.id.player1_score)).setText(s1);
            ((TextView) v.findViewById(R.id.player2_score)).setText(s2);

            setStatsNavigation(v.findViewById(R.id.player1_details), p1, s1);
            setStatsNavigation(v.findViewById(R.id.player2_details), p2, s2);

            setStatsNavigation(v.findViewById(R.id.player1_score), p1, s1);
            setStatsNavigation(v.findViewById(R.id.player2_score), p2, s2);

            if (mPlayer1Games != null && mPlayer2Games != null) {

                RestModelGame g1 = mPlayer1Games.get(position);
                RestModelGame g2 = mPlayer2Games.get(position);
                ((TextView) v.findViewById(R.id.player1_game_result)).setText(getGameResult(g1, p1));
                ((TextView) v.findViewById(R.id.player2_game_result)).setText(getGameResult(g2, p2));
            }
        }

        return v;
    }

    /**
     * Attaches an onClickListener to the input view which will navigate to the weekly stats
     * screen for the input player.
     * @param v the view to which the listener will be attached
     * @param player the player whose stats will be shown
     * @param formattedScore the pre-calculated score total for the player
     */
    private void setStatsNavigation(View v, final RestModelPlayer player, final String formattedScore) {
        v.setOnClickListener(view -> {

            if (mPlayerStats.containsKey(player.getId())) {
                Intent intent = new Intent(mActivity, StatsBreakdownScreen.class);

                Collection<RestModelStats> stats = mPlayerStats.get(player.getId());
                String[] statNames = new String[stats.size()];
                float[] statValues = new float[stats.size()];
                float[] statPoints = new float[stats.size()];

                int i = -1;
                for (RestModelStats stat : stats) {
                    i += 1;
                    statNames[i] = stat.getTypeName();
                    statValues[i] = stat.getValue();
                    statPoints[i] = stat.getTypePoints();
                }
                intent.putExtra(StatsBreakdownScreen.FIRST_NAME, player.getFirstName());
                intent.putExtra(StatsBreakdownScreen.LAST_NAME, player.getLastName());
                intent.putExtra(StatsBreakdownScreen.TOTAL_POINTS, formattedScore);
                intent.putExtra(StatsBreakdownScreen.WEEK, mWeek);
                intent.putExtra(STAT_NAMES, statNames);
                intent.putExtra(STAT_VALUES, statValues);
                intent.putExtra(STAT_POINTS, statPoints);
                mActivity.startActivity(intent);
            }
        });
    }

    private static String getGameResult(RestModelGame game, RestModelPlayer player) {

        final int playerScore;
        final int opponentScore;
        final String prefix;
        final String suffix;
        if(game.getHomeTeam().equals(player.getTeam())) {
            playerScore = game.getScoreHome();
            opponentScore = game.getScoreAway();
            suffix = " vs " + game.getAwayTeam();
        } else {
            playerScore = game.getScoreAway();
            opponentScore = game.getScoreHome();
            suffix = " @ " + game.getHomeTeam();
        }

        if(playerScore < opponentScore) {
            prefix = "L";
        } else if(playerScore > opponentScore) {
            prefix = "W";
        } else {
            // according to Wikipedia, it is possible, though rare, to have a tie game
            prefix = "T";
        }

        return prefix + ", " + playerScore + "-" + opponentScore + suffix;
    }

    private static float getPlayerScore(String playerId, Map<String, List<RestModelStats>> stats) {

        float scoreTotal = 0f;

        if (stats.containsKey(playerId)) {

            for (RestModelStats stat: stats.get(playerId)) {
                scoreTotal += stat.getTypePoints();
            }
        }

        return scoreTotal;
    }

    private static String getScore(RestModelPlayer player, Map<String, List<RestModelStats>> stats) {
        float score = getPlayerScore(player.getId(), stats);
        return String.format("%.02f", score);
    }

    private static String getPositionTeam(RestModelPlayer player) {
        return player.getPosition() + " - " + player.getTeam();
    }
}
