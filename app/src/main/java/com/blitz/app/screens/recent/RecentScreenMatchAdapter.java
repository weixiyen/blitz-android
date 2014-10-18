package com.blitz.app.screens.recent;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.matchup.MatchupScreen;
import com.blitz.app.utilities.image.BlitzImageView;
import com.blitz.app.view_models.ViewModelRecent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An ArrayAdapter for match infos.
 *
 * Created by Nate on 9/7/14.
 */
public class RecentScreenMatchAdapter extends ArrayAdapter<ViewModelRecent.HeadToHeadDraft> {

    private List<ViewModelRecent.HeadToHeadDraft> mItems;

    public RecentScreenMatchAdapter(List<ViewModelRecent.HeadToHeadDraft> items, Activity activity) {
        super(activity, R.layout.recent_screen_match_item, items);

        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.recent_screen_match_item, parent, false);

            // Create and set the view holder.
            // Create and set the view holder.
            convertView.setTag(new RecentScreenMatchItemViewHolder(convertView));
        }

        // Fetch view holder.
        RecentScreenMatchItemViewHolder viewHolder =
                (RecentScreenMatchItemViewHolder)convertView.getTag();

        final ViewModelRecent.HeadToHeadDraft draft = mItems.get(position);

        if (draft != null) {

            // Set match scores.
            viewHolder.mRecentP1Score.setText(String.format("%.02f", draft.getPlayer1Score()));
            viewHolder.mRecentP2Score.setText(String.format("%.02f", draft.getPlayer2Score()));

            // Set match names.
            viewHolder.mRecentP1Name.setText(draft.getPlayer1Name());
            viewHolder.mRecentP2Name.setText(draft.getPlayer2Name());

            // Set match status.
            viewHolder.mRecentStatus.setText(draft.getStatus());

            /*
            int leaderColor = Color.rgb(0, 255, 255);

            if (draft.getPlayer1Score() > draft.getPlayer2Score()) {
                player1Score.setTextColor(leaderColor);
            } else if(draft.getPlayer2Score() > draft.getPlayer1Score()) {
                player2Score.setTextColor(leaderColor);
            }
            */

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MatchupScreen.class);

                    intent.putExtra(MatchupScreen.PARAM_DRAFT_ID, draft.getId());
                    getContext().startActivity(intent);
                }
            });
        }

        return convertView;
    }

    // region View Holder
    // =============================================================================================

    /**
     * Quick lookup into a views subviews.
     */
    static class RecentScreenMatchItemViewHolder {

        @InjectView(R.id.recent_match_p1_avatar) BlitzImageView mRecentP1Avatar;
        @InjectView(R.id.recent_match_p1_name)         TextView mRecentP1Name;
        @InjectView(R.id.recent_match_p1_score)        TextView mRecentP1Score;
        @InjectView(R.id.recent_match_p2_avatar) BlitzImageView mRecentP2Avatar;
        @InjectView(R.id.recent_match_p2_name)         TextView mRecentP2Name;
        @InjectView(R.id.recent_match_p2_score)        TextView mRecentP2Score;
        @InjectView(R.id.recent_match_status)          TextView mRecentStatus;

        public RecentScreenMatchItemViewHolder(View matchListItem) {

            // Map the member variables.
            ButterKnife.inject(this, matchListItem);
        }
    }

    // endregion
}
