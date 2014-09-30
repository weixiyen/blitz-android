package com.blitz.app.screens.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.object_models.ObjectModelUser;

import java.util.List;

/**
 * List adapter for players scores on the Leaderboard.
 *
 * Created by Nate on 9/30/14.
 */
public class LeaderboardListAdapter extends ArrayAdapter<ObjectModelUser> {

    final List<ObjectModelUser> mLeaders;

    /**
     *
     * @param leaders the top N users ordered by rating in descending order.
     */
    public LeaderboardListAdapter(Context context, List<ObjectModelUser> leaders) {

        super(context, R.layout.leaderboard_screen, leaders);
        mLeaders = leaders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext())
                    .inflate(R.layout.leaderboard_list_item, null);
        }

        ObjectModelUser leader = mLeaders.get(position);

        setText(v, R.id.list_index, Integer.toString(position + 1));
        setText(v, R.id.user_name, leader.getUsername());
        setText(v, R.id.wins, "W" + leader.getWins());
        setText(v, R.id.losses, "L" + leader.getLosses());
        setText(v, R.id.rating, Integer.toString(leader.getRating()));

        return v;
    }

    private void setText(View v, int resourceId, String text) {
        ((TextView) v.findViewById(resourceId)).setText(text);
    }
}
