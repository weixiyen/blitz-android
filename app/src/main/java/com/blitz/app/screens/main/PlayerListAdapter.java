package com.blitz.app.screens.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.stats.PlayerWeekStatsScreen;
import com.blitz.app.simple_models.Game;
import com.blitz.app.simple_models.Player;

import java.util.List;

/**
 * Player list adapter for binding player info to the draft detail UI's ListView.
 *
 * Created by spiff on 9/10/14.
 */
public class PlayerListAdapter extends ArrayAdapter {

    private final List<Player> mPlayer1Picks;
    private final List<Player> mPlayer2Picks;
    private final List<Float>  mPlayer1Scores;
    private final List<Float>  mPlayer2Scores;
    private final List<Game>   mPlayer1Games;
    private final List<Game>   mPlayer2Games;

    private final Activity mActivity;

    public PlayerListAdapter(Context context, List<Player> player1picks, List<Player> player2picks,
                             List<Game> player1games, List<Game> player2games,
                             List<Float> player1scores, List<Float> player2scores,
                             Activity activity) {


        super(context, R.layout.main_screen_fragment_draft_detail, player1picks);
        mActivity = activity;

        mPlayer1Picks = player1picks;
        mPlayer2Picks = player2picks;
        mPlayer1Games = player1games;
        mPlayer2Games = player2games;
        mPlayer1Scores = player1scores;
        mPlayer2Scores = player2scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(mActivity)
                    .inflate(R.layout.main_screen_draft_list_item, null);
        }

        Player p1 = mPlayer1Picks.get(position);
        Player p2 = mPlayer2Picks.get(position);

        ((TextView) v.findViewById(R.id.player1_name)).setText(p1.getFullName());
        ((TextView) v.findViewById(R.id.player1_position_team)).setText(getPositionTeam(p1));
        ((TextView) v.findViewById(R.id.player2_name)).setText(p2.getFullName());
        ((TextView) v.findViewById(R.id.player2_position_team)).setText(getPositionTeam(p2));


        if(mPlayer1Scores != null && mPlayer2Scores != null) {

            Float  s1 = mPlayer1Scores.get(position);
            Float  s2 = mPlayer2Scores.get(position);
            ((TextView) v.findViewById(R.id.player1_score)).setText(getScore(s1));
            ((TextView) v.findViewById(R.id.player2_score)).setText(getScore(s2));
        }

        if(mPlayer1Games != null && mPlayer2Games != null) {

            Game g1 = mPlayer1Games.get(position);
            Game g2 = mPlayer2Games.get(position);
            ((TextView) v.findViewById(R.id.player1_game_result)).setText(getGameResult(g1, p1));
            ((TextView) v.findViewById(R.id.player2_game_result)).setText(getGameResult(g2, p2));

        }

        ((TextView) v.findViewById(R.id.player1_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PlayerWeekStatsScreen.class);
                mActivity.startActivity(intent);
            }
        });


        return v;
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

    private static String getScore(Float score) {
        return String.format("%.02f", score); // TODO we should be consuming some other kind of object with this set
    }

    private static String getPositionTeam(Player player) {
        return player.getPosition() + " - " + player.getTeamName();
    }
}
