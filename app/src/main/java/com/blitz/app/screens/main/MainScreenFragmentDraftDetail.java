package com.blitz.app.screens.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.simple_models.Player;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.view_models.HeadToHeadDraft;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDraftDetail;
import com.blitz.app.view_models.ViewModelGameLog;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenFragmentDraftDetail extends BaseFragment implements ViewModelDraftDetail.ViewModelDraftDetailCallbacks {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.main_draft_detail_player_list)     ListView mPlayerList;

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

    // endregion

    // region Public Methods
    // =============================================================================================

    public void onPlayers(List<Pair<Player, Player>> players) {
        final PlayerListAdapter adapter = new PlayerListAdapter(getActivity().getApplicationContext(),
                players, getActivity());

        if(mPlayerList != null) {
            mPlayerList.setAdapter(adapter);
        }
    }

    // endregion
}