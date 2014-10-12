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

/**
 * List adapter for players scores on the Leaderboard.
 *
 * Created by Nate on 9/30/14.
 */
public class LeaderboardListAdapter extends ArrayAdapter<String> {

    // region Member Variables
    // =============================================================================================

    private List<String>  mUserNames;
    private List<Integer> mUserWins;
    private List<Integer> mUserLosses;
    private List<Integer> mUserRating;
    private List<String>  mUserAvatarUrls;

    // endregion

    // region Constructor
    // =============================================================================================

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
    // =============================================================================================

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.leaderboard_list_item, parent, false);
        }

        setText(view, R.id.leaderboard_index,  String.format("%03d", position + 1));
        setText(view, R.id.leaderboard_user_name, mUserNames.get(position));
        setText(view, R.id.leaderboard_wins, "W" + String.format("%03d", mUserWins.get(position)));
        setText(view, R.id.leaderboard_losses, "L" + String.format("%03d", mUserLosses.get(position)));
        setText(view, R.id.leaderboard_rating, Integer.toString(mUserRating.get(position)));

        ((BlitzImageView)view.findViewById(R.id.leaderboard_helmet))
                .setImageUrl(mUserAvatarUrls.get(position));

        return view;
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    private void setText(View v, int resourceId, String text) {
        ((TextView) v.findViewById(resourceId)).setText(text);
    }

    // endregion
}