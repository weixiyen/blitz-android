package com.blitz.app.screens.recent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.matchup.MatchupScreen;
import com.blitz.app.view_models.ViewModelRecent;

import java.util.List;

/**
 * An ArrayAdapter for match infos.
 *
 * Created by Nate on 9/7/14.
 */
public class RecentScreenMatchAdapter extends ArrayAdapter<ViewModelRecent.HeadToHeadDraft> {

    private List<ViewModelRecent.HeadToHeadDraft> mItems;
    private final Activity mActivity;

    public RecentScreenMatchAdapter(List<ViewModelRecent.HeadToHeadDraft> items, Activity activity) {

        super(activity, R.layout.recent_screen_match_item, items);
        mItems = items;
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(mActivity)
                    .inflate(R.layout.recent_screen_match_item, null);
        }

        final ViewModelRecent.HeadToHeadDraft draft = mItems.get(position);

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

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, MatchupScreen.class);

                    intent.putExtra(MatchupScreen.PARAM_DRAFT_ID, draft.getId());
                    mActivity.startActivity(intent);
                }
            });
        }

        return v;
    }
}
