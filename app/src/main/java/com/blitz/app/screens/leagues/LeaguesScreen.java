package com.blitz.app.screens.leagues;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.logging.LogHelper;
import com.blitz.app.utilities.scrubber.BlitzScrubber;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelLeagues;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 10/24/14. Copyright 2014 Blitz Studios
 */
public class LeaguesScreen extends BaseFragment implements ViewModelLeagues.Callbacks,
        LeaguesScreenAdapterCreate.Callbacks, BlitzScrubber.Callbacks {

    // region Member Variables
    // ============================================================================================================

    @InjectView(R.id.leagues_header) TextView mLeaguesHeader;
    @InjectView(R.id.leagues_screen_list) ListView mLeaguesScreenList;

    // Associated view model.
    private ViewModelLeagues mViewModel;

    // Tracks selected league.
    private int mSelectedLeague;

    // User leagues, directly maps to
    // scrubber indices.
    private List<String> mUserLeagueIds;
    private List<String> mUserLeagueNames;

    // Initialize the adapters this screen will use.
    private LeaguesScreenAdapterCreate mAdapterCreate = new LeaguesScreenAdapterCreate(this);
    private LeaguesScreenAdapterView mAdapterView = new LeaguesScreenAdapterView();

    // League selector.
    @InjectView(R.id.leagues_scrubber) BlitzScrubber mLeaguesScrubber;
    @InjectView(R.id.leagues_scrubber_item) TextView mLeaguesScrubberItem;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * A simpler version of on create view.  Can be used
     * as a convenience method which abstracts away
     * some of the details of a fragment and makes them
     * more like activities.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        // Provide the view pager.
        mLeaguesScrubber.setViewPager(((MainScreen) getActivity()).getViewPager());

        // Callbacks.
        mLeaguesScrubber.setCallbacks(this);
    }

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelLeagues(getBaseActivity(), this);
        }

        return mViewModel;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Set the selected league and update associated UI.
     *
     * @param selectedLeague Selected league.
     */
    private void setSelectedLeague(int selectedLeague) {

        // Update selected league index.
        mSelectedLeague = selectedLeague;

        // Set the header.
        if (mLeaguesHeader != null) {
            mLeaguesHeader.setText(mUserLeagueNames.get(mSelectedLeague));
        }

        // Ensure the correct item is selected.
        mLeaguesScrubber.setScrubberItemSelected(mSelectedLeague);

        String selectedLeagueId = mUserLeagueIds.get(mSelectedLeague);

        // Trigger a view model update for associated id.
        mViewModel.setSelectedLeagueId(mUserLeagueIds.get(mSelectedLeague));

        if (selectedLeagueId == null) {

            // Set the initial adapter for create/join.
            mLeaguesScreenList.setAdapter(mAdapterCreate);
        } else {

            // Set the initial adapter for user league.
            mLeaguesScreenList.setAdapter(mAdapterView);
        }
    }

    // endregion

    // region View Model Callbacks
    // ============================================================================================================

    /**
     * Received all available user leagues.
     *
     * @param leagueIds League ids. The last is null (represents
     *                  the create/join screen).
     * @param leagueNames League names.
     */
    @Override
    public void onUserLeagues(List<String> leagueIds, List<String> leagueNames) {

        // Store the results.
        mUserLeagueNames = leagueNames;
        mUserLeagueIds = leagueIds;

        // Set the scrubber text view and names.
        mLeaguesScrubber.setScrubberTextView(mLeaguesScrubberItem, mUserLeagueNames);

        // Set the size of the scrubber.
        mLeaguesScrubber.setSize(mUserLeagueIds.size());

        // Initialize selected league.
        setSelectedLeague(mSelectedLeague);
    }

    /**
     * On recruiting leagues data received.
     *
     * @param leagueIds League ids.
     * @param leagueNames League names.
     * @param leagueRatings League ratings.
     * @param leagueMemberCounts League member counts.
     */
    @Override
    public void onRecruitingLeagues(List<String> leagueIds,
                                    List<String> leagueNames,
                                    List<Integer> leagueRatings,
                                    List<Integer> leagueMemberCounts) {

        if (mLeaguesScreenList != null) {

            // Set recruiting leagues.
            mAdapterCreate.setAssociatedListView(mLeaguesScreenList);
            mAdapterCreate.setRecruitingLeagues
                    (leagueIds, leagueNames, leagueRatings, leagueMemberCounts);
        }
    }

    // endregion

    // region Adapter Callbacks
    // ============================================================================================================

    @Override
    public void onJoinLeagueClicked(String leagueId) {

        LogHelper.log("Join league: " + leagueId);
    }

    @Override
    public void onJoinLeagueManualClicked() {

        LogHelper.log("Join league, manual input.");
    }

    @Override
    public void onCreateLeagueClicked() {

        LogHelper.log("Create league.");
    }

    // endregion

    // region Scrubber Callbacks
    // ============================================================================================================

    /**
     * Update selected league UI when
     * the scrubber position changes.
     *
     * @param position Current position.
     */
    @Override
    public void onScrubberItemSelected(int position) {

        // Update selected league.
        setSelectedLeague(position);
    }

    // endregion
}