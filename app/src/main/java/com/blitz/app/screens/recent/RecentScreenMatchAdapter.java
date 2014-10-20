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
public class RecentScreenMatchAdapter extends ArrayAdapter<ViewModelRecent.SummaryDraft> {

    // region Member Variables.
    // =============================================================================================

    private List<ViewModelRecent.SummaryDraft> mItems;

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Initialize the adapter.
     *
     * @param items Model.
     * @param activity Activity.
     */
    public RecentScreenMatchAdapter(List<ViewModelRecent.SummaryDraft> items, Activity activity) {
        super(activity, R.layout.recent_screen_match_item, items);

        mItems = items;
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Fetch view.
     *
     * @param position Position.
     * @param convertView Reusable view.
     * @param parent Parent group.
     *
     * @return Configured view.
     */
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

        // Configure and return the view.
        return configureView(convertView, viewHolder, position);
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Configure view.
     *
     * @param view Re-usable view.
     * @param viewHolder View holder.
     * @param position Position.
     *
     * @return Configured view.
     */
    private View configureView(View view, RecentScreenMatchItemViewHolder viewHolder,
                               int position) {

        // Fetch associated draft.
        final ViewModelRecent.SummaryDraft draft = mItems.get(position);

        // Verify model.
        if (draft == null) {

            return view;
        }

        int colorLight  = view.getResources().getColor(R.color.text_color_light);
        int colorOrange = view.getResources().getColor(R.color.text_color_orange);
        int colorGreen  = view.getResources().getColor(R.color.text_color_green);

        // Set match scores.
        viewHolder.mRecentP1Score.setText(String.format("%.02f", draft.getP1Score()));
        viewHolder.mRecentP2Score.setText(String.format("%.02f", draft.getP2Score()));

        viewHolder.mRecentP1Score.setTextColor(colorLight);
        viewHolder.mRecentP2Score.setTextColor(colorLight);

        // Set match names.
        viewHolder.mRecentP1Name.setText(draft.getP1Name());
        viewHolder.mRecentP2Name.setText(draft.getP2Name());

        // Set match avatars.
        viewHolder.mRecentP1Avatar.setImageUrl(draft.getP1AvatarUrl());
        viewHolder.mRecentP2Avatar.setImageUrl(draft.getP2AvatarUrl());

        // Set match status.
        viewHolder.mRecentStatus.setText(draft.getStatus());
        viewHolder.mRecentStatus.setTextColor(colorLight);

        // Fetch the leading score.
        TextView leadingScore = draft.getP1Score() >= draft.getP2Score() ?
                viewHolder.mRecentP1Score : viewHolder.mRecentP2Score;

        int targetColor = colorLight;

        if (draft.getStatus().equals("final")) {

            targetColor = colorGreen;
        }

        if (draft.getStatus().equals("in progress")) {

            targetColor = colorOrange;
        }

        // Set status and leading score color.
        viewHolder.mRecentStatus.setTextColor(targetColor);
        leadingScore.setTextColor(targetColor);

        // Set an on click listener.
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), MatchupScreen.class);

                intent.putExtra(MatchupScreen.PARAM_DRAFT_ID, draft.getId());
                getContext().startActivity(intent);
            }
        });

        return view;
    }

    // endregion

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
