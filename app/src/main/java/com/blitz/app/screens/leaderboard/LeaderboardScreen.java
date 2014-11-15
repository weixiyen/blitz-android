package com.blitz.app.screens.leaderboard;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.dropdown.BlitzDropdown;
import com.blitz.app.utilities.logging.LogHelper;
import com.blitz.app.utilities.scrubber.BlitzScrubber;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelLeaderboard;

import java.util.Arrays;
import java.util.List;

import butterknife.InjectView;

/**
 * Screen that shows players with the highest Elo rating.
 *
 * Created by Nate on 9/29/14.
 */
public class LeaderboardScreen extends BaseActivity implements ViewModelLeaderboard.Callbacks {

    // region Member Variables
    // ============================================================================================================

    @InjectView(R.id.blitz_dropdown_container)  View mBlitzDropdownContainer;
    @InjectView(R.id.blitz_dropdown_header) TextView mBlitzDropdownHeader;
    @InjectView(R.id.blitz_dropdown_list)   ListView mBlitzDropdownList;
    @InjectView(R.id.blitz_dropdown_list_wrap)  View mBlitzDropdownListWrap;

    @InjectView(R.id.leaderboard_list) ListView mLeaderboardElementList;
    @InjectView(R.id.leaderboard_spinner) ProgressBar mLeaderboardSpinner;

    @InjectView(R.id.blitz_scrubber) BlitzScrubber mBlitzScrubber;
    @InjectView(R.id.blitz_scrubber_selected) TextView mBlitzScrubberSelected;

    // View model object.
    private ViewModelLeaderboard mViewModel;

    // Dropdown widget.
    private BlitzDropdown mBlitzDropdown;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Run custom transitions if needed.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Modal style presentation.
        setCustomTransitions(CustomTransition.T_SLIDE_VERTICAL);

        List<String> headers = Arrays.asList("Top 150 Players", "Top 150 Leagues");

        // Provide the view pager.
        mBlitzScrubber.setSize(2);
        mBlitzScrubber.setScrubberTextView(mBlitzScrubberSelected, headers);
        mBlitzScrubber.setCallbacks(this::setSelected);

        // Configure the dropdown.
        mBlitzDropdown = new BlitzDropdown(this, headers);
        mBlitzDropdown.setListView(mBlitzDropdownListWrap, mBlitzDropdownList);
        mBlitzDropdown.setHeaderView(mBlitzDropdownHeader);
        mBlitzDropdown.setContainerView(mBlitzDropdownContainer);
        mBlitzDropdown.setCallbacks(this::setSelected);

        setSelected(ViewModelLeaderboard.TOP_150_PLAYERS);
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

    // region Public Methods
    // ============================================================================================================

    /**
     * Item is selected (users or leagues).
     */
    private void setSelected(int position) {

        LogHelper.log("Set selected: " + position);

        // Set the scrubber.
        mBlitzScrubber.setScrubberItemSelected(position);

        // Set the dropdown.
        mBlitzDropdown.setSelected(position);

        // Set the view model.
        mViewModel.setSelected(position);

        // Kill existing data.
        mLeaderboardElementList.setAdapter(null);

        // Show the loading spinner.
        AnimHelperFade.setVisibility(mLeaderboardSpinner, View.VISIBLE);
    }

    // endregion

    // region View Model Callbacks
    // ============================================================================================================

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

        AnimHelperFade.setVisibility(mLeaderboardSpinner, View.GONE, () -> {

            if (mLeaderboardElementList != null) {
                mLeaderboardElementList.setAdapter(new LeaderboardListAdapter
                        (LeaderboardScreen.this, userIds, userNames, userWins,
                                userLosses, userRating, userAvatarUrls));
            }
        });
    }

    @Override
    public void onLeaguesReceived(List<String> leagueIds, List<String> leagueNames, List<Integer> leagueRating) {

        AnimHelperFade.setVisibility(mLeaderboardSpinner, View.GONE, () -> {

            if (mLeaderboardElementList != null) {
                mLeaderboardElementList.setAdapter(new LeaderboardListAdapter
                        (LeaderboardScreen.this, leagueIds, leagueNames, null, null, leagueRating, null));
            }
        });
    }

    // endregion
}