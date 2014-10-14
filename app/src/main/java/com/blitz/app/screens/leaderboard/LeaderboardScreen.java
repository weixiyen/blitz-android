package com.blitz.app.screens.leaderboard;

import android.os.Bundle;
import android.widget.ListView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelLeaderboard;

import java.util.List;

import butterknife.InjectView;

/**
 * Screen that shows players with the highest Elo rating.
 *
 * Created by Nate on 9/29/14.
 */
public class LeaderboardScreen extends BaseActivity implements ViewModelLeaderboard.Callbacks {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.leaderboard_list) ListView mPlayerList;

    // View model object.
    private ViewModelLeaderboard mViewModel;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Run custom transitions if needed.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCustomTransitions(CustomTransition.T_SLIDE_VERTICAL);
    }

    /**
     * Fetch view model.
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelLeaderboard(this, this);
        }

        return mViewModel;
    }

    // endregion

    // region View Model Callbacks
    // =============================================================================================

    /**
     * Simple pass though to list view adapter
     * when we have leaderboard info.
     *
     * @param userIds Ids.
     * @param userNames Names.
     * @param userWins Wins.
     * @param userLosses Losses.
     * @param userRating Rating.
     * @param userAvatarUrls Avatars.
     */
    @Override
    public void onLeadersReceived(List<String>  userIds,
                                  List<String>  userNames,
                                  List<Integer> userWins,
                                  List<Integer> userLosses,
                                  List<Integer> userRating,
                                  List<String>  userAvatarUrls) {

        if (mPlayerList != null) {
            mPlayerList.setAdapter(new LeaderboardListAdapter
                    (this, userIds, userNames, userWins, userLosses, userRating, userAvatarUrls));
        }
    }

    // endregion
}