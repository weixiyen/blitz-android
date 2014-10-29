package com.blitz.app.screens.leagues;

import android.os.Bundle;
import android.widget.ListView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelLeagues;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 10/24/14. Copyright 2014 Blitz Studios
 */
public class LeaguesScreen extends BaseFragment implements ViewModelLeagues.Callbacks {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.leagues_screen_list) ListView leaguesScreenList;

    // Associated view model.
    private ViewModelLeagues mViewModel;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

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
        leaguesScreenList.setAdapter(new LeaguesScreenAdapterCreate());
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
}