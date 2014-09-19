package com.blitz.app.screens.main;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.simple_models.Player;

import java.util.List;

/**
 * Player list adapter for binding player info to the draft detail UI's ListView.
 *
 * Created by spiff on 9/10/14.
 */
public class PlayerListAdapter extends ArrayAdapter {

    private final List<Pair<Player, Player>> mPlayers;
    private final Activity mActivity;

    public PlayerListAdapter(Context context, List<Pair<Player, Player>> players, Activity activity) {

        super(context, R.layout.main_screen_fragment_draft_detail, players);
        mActivity = activity;

        mPlayers = players;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(mActivity)
                    .inflate(R.layout.main_screen_draft_list_item, null);
        }

        Pair<Player, Player> players = mPlayers.get(position);

        ((TextView) v.findViewById(R.id.player1_name)).setText(players.first.getFullName());
        ((TextView) v.findViewById(R.id.player1_position_team)).setText(getPositionTeam(players.first));
        ((TextView) v.findViewById(R.id.player1_score)).setText(getScore(players.first));

        ((TextView) v.findViewById(R.id.player2_name)).setText(players.second.getFullName());
        ((TextView) v.findViewById(R.id.player2_position_team)).setText(getPositionTeam(players.second));
        ((TextView) v.findViewById(R.id.player2_score)).setText(getScore(players.second));


        return v;
    }

    private static String getScore(Player player) {
        return String.format("%.02f", player.getScore());
    }

    private static String getPositionTeam(Player player) {
        return player.getPosition() + " - " + player.getTeamName();
    }
}
