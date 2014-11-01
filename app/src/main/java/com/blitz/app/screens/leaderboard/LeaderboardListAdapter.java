package com.blitz.app.screens.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.image.BlitzImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * List adapter for players scores on the Leaderboard.
 *
 * Created by Nate on 9/30/14.
 */
public class LeaderboardListAdapter extends ArrayAdapter<String> {

    // region Member Variables
    // ============================================================================================================

    private List<String>  mUserNames;
    private List<Integer> mUserWins;
    private List<Integer> mUserLosses;
    private List<Integer> mUserRating;
    private List<String>  mUserAvatarUrls;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Create adapter.
     *
     * @param context Context.
     * @param userIds User ids.
     * @param userNames User names.
     * @param userWins User wins.
     * @param userLosses User losses.
     * @param userRating User ratings.
     * @param userAvatarUrls User avatars.
     */
    public LeaderboardListAdapter(Context context,
                                  List<String>  userIds,
                                  List<String>  userNames,
                                  List<Integer> userWins,
                                  List<Integer> userLosses,
                                  List<Integer> userRating,
                                  List<String>  userAvatarUrls) {
        super(context, R.layout.leaderboard_screen, userIds);

        // Set data source.
        mUserNames      = userNames;
        mUserWins       = userWins;
        mUserLosses     = userLosses;
        mUserRating      = userRating;
        mUserAvatarUrls = userAvatarUrls;
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Fetch and populate a leaderboard list item
     * at a specified position.
     *
     * @param position Position.
     * @param convertView Re-usable view.
     * @param parent Parent view.
     *
     * @return Inflated and populated view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.leaderboard_screen_list_item, parent, false);

            // Create and set associated view holder.
            convertView.setTag(new LeaderboardListItemViewHolder(convertView));
        }

        // Fetch view holder.
        LeaderboardListItemViewHolder viewHolder =
                (LeaderboardListItemViewHolder)convertView.getTag();

        // Set the text.
        viewHolder.mLeaderboardIndex.setText(Integer.toString(position + 1));
        viewHolder.mLeaderboardUserName.setText(mUserNames.get(position));
        viewHolder.mLeaderboardWins.setText("W" + String.format("%03d", mUserWins.get(position)));
        viewHolder.mLeaderboardLosses.setText("L" + String.format("%03d", mUserLosses.get(position)));
        viewHolder.mLeaderboardRating.setText(Integer.toString(mUserRating.get(position)));

        // Set the image.
        viewHolder.mLeaderboardHelmet.setImageBitmap(null);
        viewHolder.mLeaderboardHelmet.setImageUrl(mUserAvatarUrls.get(position));

        return convertView;
    }

    // endregion

    // region View Holder
    // ============================================================================================================

    /**
     * Quick lookup into a views subviews.
     */
    static class LeaderboardListItemViewHolder {

        @InjectView(R.id.leaderboard_index)        TextView mLeaderboardIndex;
        @InjectView(R.id.leaderboard_user_name)    TextView mLeaderboardUserName;
        @InjectView(R.id.leaderboard_wins)         TextView mLeaderboardWins;
        @InjectView(R.id.leaderboard_losses)       TextView mLeaderboardLosses;
        @InjectView(R.id.leaderboard_rating)       TextView mLeaderboardRating;
        @InjectView(R.id.leaderboard_helmet) BlitzImageView mLeaderboardHelmet;

        public LeaderboardListItemViewHolder(View leaderboardListItem) {

            // Map the member variables.
            ButterKnife.inject(this, leaderboardListItem);
        }
    }

    // endregion
}