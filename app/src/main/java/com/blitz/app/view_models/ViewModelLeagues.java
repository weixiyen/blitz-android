package com.blitz.app.view_models;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestModelCallbacks;
import com.blitz.app.rest_models.RestModelGroup;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;

import java.util.List;

/**
 * Created by mrkcsc on 10/28/14. Copyright 2014 Blitz Studios
 */
public class ViewModelLeagues extends ViewModel {

    // region Member Variables
    // ============================================================================================================

    // Do not show more than six leagues.
    private static final int MAX_LEAGUES_TO_SHOW = 6;
    private static final int MAX_RECRUITING_LEAGUES_TO_SHOW = 100;

    private List<RestModelGroup> mUserLeagues;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelLeagues(BaseActivity activity, Callbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Initialize the view model.
     */
    @Override
    public void initialize() {

        // Fetch league information.
        fetchUserLeaguesData();
        fetchCreateJoinLeagueData();
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Fetch leagues user is associated with,
     * to a cap.
     */
    private void fetchUserLeaguesData() {

        RestModelGroup.getGroupsForUserId(mActivity, AuthHelper.instance().getUserId(),
                new RestModelCallbacks<RestModelGroup>() {

            @Override
            public void onSuccess(List<RestModelGroup> object) {

                // Filter down leagues if necessary.
                mUserLeagues = object.subList(0, object.size() > MAX_LEAGUES_TO_SHOW ?
                                MAX_LEAGUES_TO_SHOW : object.size());
            }
        });
    }

    /**
     * TODO: Implement.
     */
    @SuppressWarnings("unused")
    private void fetchUserLeagueData() {

    }

    /**
     * Fetch the info related to panel that shows
     * for the create/join league screen.
     */
    private void fetchCreateJoinLeagueData() {

        if (getCallbacks(Callbacks.class) != null) {
            getCallbacks(Callbacks.class).onLeagueName(mActivity.getResources()
                    .getString(R.string.create_or_join_a_league));
        }

        // Fetch groups that are recruiting, no blocked UI.
        RestModelGroup.getGroupsRecruitingWithLimit(null, MAX_RECRUITING_LEAGUES_TO_SHOW,
                new RestModelCallbacks<RestModelGroup>() {

            @Override
            public void onSuccess(List<RestModelGroup> object) {

                // TODO: Implement the rest.
            }
        });
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onLeagueName(String leagueName);
    }

    // endregion
}