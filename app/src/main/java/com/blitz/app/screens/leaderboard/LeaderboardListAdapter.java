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

    private List<String>  mElementNames;
    private List<String>  mElementAvatarUrls;
    private List<Integer> mElementWins;
    private List<Integer> mElementLosses;
    private List<Integer> mElementRating;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Create adapter.
     *
     * @param context Context.
     * @param elementIds User ids.
     * @param elementNames User names.
     * @param elementWins User wins.
     * @param elementLosses User losses.
     * @param elementRating User ratings.
     * @param elementAvatarUrls User avatars.
     */
    public LeaderboardListAdapter(Context context,
                                  List<String>  elementIds,
                                  List<String>  elementNames,
                                  List<Integer> elementWins,
                                  List<Integer> elementLosses,
                                  List<Integer> elementRating,
                                  List<String>  elementAvatarUrls) {
        super(context, R.layout.leaderboard_screen, elementIds);

        // Set data source.
        mElementNames = elementNames;
        mElementWins = elementWins;
        mElementLosses = elementLosses;
        mElementRating = elementRating;
        mElementAvatarUrls = elementAvatarUrls;
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
        viewHolder.mLeaderboardUserName.setText(mElementNames.get(position));
        viewHolder.mLeaderboardWins.setText(mElementWins != null ?
                "W" + String.format("%03d", mElementWins.get(position)) : null);
        viewHolder.mLeaderboardLosses.setText(mElementLosses != null ?
                "L" + String.format("%03d", mElementLosses.get(position)) : null);
        viewHolder.mLeaderboardRating.setText(Integer.toString(mElementRating.get(position)));

        // Set the image.
        viewHolder.mLeaderboardHelmet.setImageBitmap(null);
        viewHolder.mLeaderboardHelmet.setImageUrl
                (mElementAvatarUrls != null ? mElementAvatarUrls.get(position) : null);

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