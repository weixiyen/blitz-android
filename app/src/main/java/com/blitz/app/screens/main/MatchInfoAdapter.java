package com.blitz.app.screens.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.view_models.HeadToHeadDraft;
import com.blitz.app.view_models.ViewModelDraftDetail;

import java.util.List;

/**
 * An ArrayAdapter for match infos.
 *
 * Created by Nate on 9/7/14.
 */
public class MatchInfoAdapter extends ArrayAdapter {

    public static final String PLAYER_1_ROSTER = "MatchInfoAdapter.player1Roster";
    public static final String PLAYER_2_ROSTER = "MatchInfoAdapter.player2Roster";

    private List<HeadToHeadDraft> mItems;
    private final Activity mActivity;

    public MatchInfoAdapter(Context context, List<HeadToHeadDraft> items, Activity activity) {
        super(context, R.layout.main_screen_fragment_recent_list_item, items);
        mItems = items;
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(mActivity)
                    .inflate(R.layout.main_screen_fragment_recent_list_item, null);
        }

        final HeadToHeadDraft match = mItems.get(position);

        if(match != null) {

            int textColor = Color.rgb(251, 251, 251);

            TextView player1Score = (TextView) v.findViewById(R.id.main_list_p1_score);
            player1Score.setText(String.format("%.02f", match.getPlayer1Score()));
            player1Score.setTextColor(textColor);

            TextView player2Score = (TextView) v.findViewById(R.id.main_list_p2_score);
            player2Score.setText(String.format("%.02f", match.getPlayer2Score()));
            player2Score.setTextColor(textColor);

            int leaderColor = Color.rgb(0, 255, 255);

            if(match.getPlayer1Score() > match.getPlayer2Score()) {
                player1Score.setTextColor(leaderColor);
            } else if(match.getPlayer2Score() > match.getPlayer1Score()) {
                player2Score.setTextColor(leaderColor);
            }

            TextView status = (TextView) v.findViewById(R.id.main_list_status);
            status.setText(match.getStatus() + "\n" +
                    match.getPlayer1Name() + " vs " + match.getPlayer2Name());
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MainScreenFragmentDraftDetail.class);
                intent.putExtra(PLAYER_1_ROSTER, new String[]{"070855e5-50ff-4470-84a4-47995c3be532"});
                intent.putExtra(PLAYER_2_ROSTER, new String[]{"a665d15d-2337-43b8-beb9-e7c5a2bdafdb"});
                mActivity.startActivity(intent);
            }
        });

        return v;
    }
}
