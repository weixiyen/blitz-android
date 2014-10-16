package com.blitz.app.screens.leaderboard;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperFade;
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

    @InjectView(R.id.leaderboard_list) ListView mLeaderboardPlayerList;
    @InjectView(R.id.leaderboard_spinner) ProgressBar mLeaderboardSpinner;

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

        // Hide the player list at first.
        mLeaderboardPlayerList.setVisibility(View.GONE);

        // Modal style presentation.
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
    public void onLeadersReceived(final List<String>  userIds,
                                  final List<String>  userNames,
                                  final List<Integer> userWins,
                                  final List<Integer> userLosses,
                                  final List<Integer> userRating,
                                  final List<String>  userAvatarUrls) {

        AnimHelperFade.setVisibility(mLeaderboardSpinner, View.GONE, new Runnable() {

            @Override
            public void run() {

                if (mLeaderboardPlayerList != null) {
                    mLeaderboardPlayerList.setAdapter(new LeaderboardListAdapter
                            (LeaderboardScreen.this, userIds, userNames, userWins,
                                    userLosses, userRating, userAvatarUrls));

                    // Fade in the player list.
                    AnimHelperFade.setVisibility(mLeaderboardPlayerList, View.VISIBLE);
                }

                AnimHelperFade.setVisibility(mLeaderboardPlayerList, View.VISIBLE);
            }
        });
    }

    // endregion
}