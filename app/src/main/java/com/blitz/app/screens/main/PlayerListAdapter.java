package com.blitz.app.screens.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.stats.PlayerWeekStatsScreen;
import com.blitz.app.simple_models.Game;
import com.blitz.app.simple_models.Player;
import com.blitz.app.simple_models.Stat;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;

/**
 * Player list adapter for binding player info to the draft detail UI's ListView.
 *
 * Created by spiff on 9/10/14.
 */
public class PlayerListAdapter extends ArrayAdapter {

    public static final String STAT_NAMES = "PlayerListAdapter.statNames";
    public static final String STAT_VALUES = "PlayerListAdapter.statValues";
    public static final String STAT_POINTS = "PlayerListAdapter.statPoints";

    private final List<Player> mPlayer1Picks;
    private final List<Player> mPlayer2Picks;
    private final List<Game>   mPlayer1Games;
    private final List<Game>   mPlayer2Games;
    private final Multimap<String, Stat> mPlayerStats;
    private final int mWeek;

    private final Activity mActivity;

    public PlayerListAdapter(Context context, List<Player> player1picks, List<Player> player2picks,
                             List<Game> player1games, List<Game> player2games,
                             Multimap<String, Stat> playerStats,
                             int week,
                             Activity activity) {


        super(context, R.layout.main_screen_fragment_draft_detail, player1picks);
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
                    .inflate(R.layout.main_screen_draft_list_item, null);
        }

        final Player p1 = mPlayer1Picks.get(position);
        Player p2 = mPlayer2Picks.get(position);

        ((TextView) v.findViewById(R.id.player1_name)).setText(p1.getFullName());
        ((TextView) v.findViewById(R.id.player1_position_team)).setText(getPositionTeam(p1));
        ((TextView) v.findViewById(R.id.player2_name)).setText(p2.getFullName());
        ((TextView) v.findViewById(R.id.player2_position_team)).setText(getPositionTeam(p2));

        final String s1 = getScore(p1, mPlayerStats);
        final String s2 = getScore(p2, mPlayerStats);

        ((TextView) v.findViewById(R.id.player1_score)).setText(s1);
        ((TextView) v.findViewById(R.id.player2_score)).setText(s2);

        setStatsNavigation((TextView) v.findViewById(R.id.player1_name), p1, s1);
        setStatsNavigation((TextView) v.findViewById(R.id.player2_name), p2, s2);

        if(mPlayer1Games != null && mPlayer2Games != null) {

            Game g1 = mPlayer1Games.get(position);
            Game g2 = mPlayer2Games.get(position);
            ((TextView) v.findViewById(R.id.player1_game_result)).setText(getGameResult(g1, p1));
            ((TextView) v.findViewById(R.id.player2_game_result)).setText(getGameResult(g2, p2));

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
    private void setStatsNavigation(View v, final Player player, final String formattedScore) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PlayerWeekStatsScreen.class);
                Collection<Stat> stats = mPlayerStats.get(player.getId());
                String[] statNames = new String[stats.size()];
                float[] statValues = new float[stats.size()];
                float[] statPoints = new float[stats.size()];

                int i = -1;
                for (Stat stat : stats) {
                    i += 1;
                    statNames[i] = stat.getStatName();
                    statValues[i] = stat.getValue();
                    statPoints[i] = stat.getPoints();
                }
                intent.putExtra(PlayerWeekStatsScreen.FIRST_NAME, player.getFirstName());
                intent.putExtra(PlayerWeekStatsScreen.LAST_NAME, player.getLastName());
                intent.putExtra(PlayerWeekStatsScreen.TOTAL_POINTS, formattedScore);
                intent.putExtra(PlayerWeekStatsScreen.WEEK, mWeek);
                intent.putExtra(STAT_NAMES, statNames);
                intent.putExtra(STAT_VALUES, statValues);
                intent.putExtra(STAT_POINTS, statPoints);
                mActivity.startActivity(intent);
            }
        });
    }

    private static String getGameResult(Game game, Player player) {

        final int playerScore;
        final int opponentScore;
        final String prefix;
        final String suffix;
        if(game.getHomeTeamName().equals(player.getTeamName())) {
            playerScore = game.getHomeTeamScore();
            opponentScore = game.getAwayTeamScore();
            suffix = " vs " + game.getAwayTeamName();
        } else {
            playerScore = game.getAwayTeamScore();
            opponentScore = game.getHomeTeamScore();
            suffix = " @ " + game.getHomeTeamName();
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

    private static float getPlayerScore(String playerId, Multimap<String, Stat> stats) {

        float scoreTotal = 0f;
        for(Stat stat: stats.get(playerId)) {
            scoreTotal += stat.getPoints();
        }
        return scoreTotal;
    }

    private static String getScore(Player player, Multimap<String, Stat> stats) {
        float score = getPlayerScore(player.getId(), stats);
        return String.format("%.02f", score);
    }

    private static String getPositionTeam(Player player) {
        return player.getPosition() + " - " + player.getTeamName();
    }
}
