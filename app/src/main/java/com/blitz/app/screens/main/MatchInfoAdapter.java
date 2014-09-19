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
import com.blitz.app.simple_models.HeadToHeadDraft;

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

        final HeadToHeadDraft draft = mItems.get(position);

        if(draft != null) {

            int textColor = Color.rgb(251, 251, 251);

            TextView player1Score = (TextView) v.findViewById(R.id.main_list_p1_score);
            player1Score.setText(String.format("%.02f", draft.getPlayer1Score()));
            player1Score.setTextColor(textColor);

            TextView player2Score = (TextView) v.findViewById(R.id.main_list_p2_score);
            player2Score.setText(String.format("%.02f", draft.getPlayer2Score()));
            player2Score.setTextColor(textColor);

            int leaderColor = Color.rgb(0, 255, 255);

            if(draft.getPlayer1Score() > draft.getPlayer2Score()) {
                player1Score.setTextColor(leaderColor);
            } else if(draft.getPlayer2Score() > draft.getPlayer1Score()) {
                player2Score.setTextColor(leaderColor);
            }

            TextView status = (TextView) v.findViewById(R.id.main_list_status);
            status.setText(draft.getStatus() + "\n" +
                    draft.getPlayer1Name() + " vs " + draft.getPlayer2Name());
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MainScreenFragmentDraftDetail.class);
                intent.putExtra(PLAYER_1_ROSTER, draft.getPlayer1Picks().toArray(new String[0]));
                intent.putExtra(PLAYER_2_ROSTER, draft.getPlayer2Picks().toArray(new String[0]));
                mActivity.startActivity(intent);
            }
        });

        return v;
    }
}
