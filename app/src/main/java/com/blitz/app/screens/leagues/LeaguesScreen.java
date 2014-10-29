package com.blitz.app.screens.leagues;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelLeagues;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 10/24/14. Copyright 2014 Blitz Studios
 */
public class LeaguesScreen extends BaseFragment implements ViewModelLeagues.Callbacks {

    // region Member Variables
    // ============================================================================================================

    @InjectView(R.id.leagues_header) TextView mLeaguesHeader;
    @InjectView(R.id.leagues_screen_list) ListView mLeaguesScreenList;

    // Associated view model.
    private ViewModelLeagues mViewModel;

    // Initialize the adapters this screen will use.
    private LeaguesScreenAdapterCreate mAdapterCreate = new LeaguesScreenAdapterCreate();
    private LeaguesScreenAdapterView mAdapterView = new LeaguesScreenAdapterView();

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

        // TODO: Use real data.
        mLeaguesScreenList.setAdapter(mAdapterCreate);
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
}