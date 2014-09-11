package com.blitz.app.screens.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.view_models.HeadToHeadDraft;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelGameLog;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenFragmentDraftDetail extends BaseFragment implements ViewModelGameLog.ViewModelGameLogCallbacks {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.main_recent_list)     ListView mRecentMatches;

    private ViewModelGameLog mViewModel; // lazy loaded

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup the recent matches UI.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

    }

    @Override
    public ViewModel onFetchViewModel() {
        if(mViewModel == null) {
            mViewModel = new ViewModelGameLog(getActivity(), this);
        }
        return mViewModel;
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    public void onDrafts(List<HeadToHeadDraft> matches) {
        final MatchInfoAdapter adapter = new MatchInfoAdapter(getActivity().getApplicationContext(),
                matches, getActivity());

        if(mRecentMatches != null) {
            mRecentMatches.setAdapter(adapter);
        }
    }

    // endregion
}