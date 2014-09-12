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
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.view_models.HeadToHeadDraft;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDraftDetail;
import com.blitz.app.view_models.ViewModelGameLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenFragmentDraftDetail extends BaseActivity implements ViewModelDraftDetail.ViewModelDraftDetailCallbacks {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO remove this test code
        Player p1 = new Player("Johnny Football", "SF", "QB", 2.234f);
        List<Pair<Player, Player>> testPlayers = new ArrayList<Pair<Player, Player>>();
        testPlayers.add(Pair.create(p1, p1));
        testPlayers.add(Pair.create(p1, p1));
        testPlayers.add(Pair.create(p1, p1));
        onPlayers(testPlayers);

    }

    // endregion

    // region Public Methods
    // =============================================================================================

    public void onPlayers(List<Pair<Player, Player>> players) {
        final PlayerListAdapter adapter = new PlayerListAdapter(getApplicationContext(),
                players, this);

        if(mPlayerList != null) {
            mPlayerList.setAdapter(adapter);
        }
    }

    // endregion
}