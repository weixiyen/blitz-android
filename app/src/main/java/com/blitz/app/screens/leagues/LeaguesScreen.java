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

        // Set the initial adapter.
        mLeaguesScreenList.setAdapter(mAdapterCreate);

        // Provide the view pager.
        mLeaguesScrubber.setViewPager(((MainScreen) getActivity()).getViewPager());

        // Weeks in season.
        mLeaguesScrubber.setSize(1);
        mLeaguesScrubber.setScrubberItemSelected(0);

        // Week display text view.
        mLeaguesScrubber.setScrubberTextView(mLeaguesScrubberItem);

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

    // region View Model Callbacks
    // ============================================================================================================

    /**
     * Set the league name, or set the default create/join
     * text if no league name is provided.
     *
     * @param leagueName League name.
     */
    @Override
    public void onLeagueName(String leagueName) {

        if (mLeaguesHeader != null) {
            mLeaguesHeader.setText(leagueName);
        }
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
            mLeaguesScreenList.setAdapter(mAdapterCreate);

            // Set recruiting leagues.
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

    @Override
    public void onScrubberItemSelected(int position) {

        LogHelper.log("Scrub me: " + position);
    }

    // endregion
}