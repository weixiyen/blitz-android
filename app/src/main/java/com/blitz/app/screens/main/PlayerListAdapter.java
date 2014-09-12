package com.blitz.app.screens.main;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.blitz.app.R;
import com.blitz.app.simple_models.Player;
import com.blitz.app.view_models.HeadToHeadDraft;

import java.util.ArrayList;
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

        return v;
    }
}
