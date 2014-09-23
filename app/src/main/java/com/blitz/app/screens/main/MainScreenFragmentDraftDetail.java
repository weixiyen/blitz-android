package com.blitz.app.screens.main;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.simple_models.Game;
import com.blitz.app.simple_models.Player;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDraftDetail;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenFragmentDraftDetail extends BaseActivity implements ViewModelDraftDetail.ViewModelDraftDetailCallbacks {

    // region Member Variables
    // =============================================================================================

    private ViewModelDraftDetail mViewModel;

    @InjectView(R.id.main_draft_detail_player_list)     ListView mPlayerList;

    // endregion

    // region Private Methods
    // =============================================================================================

    private String formatScore(float score) {
        return String.format("%.02f", score);
    }

    private void setName(int playerDetails, String name) {
        ((TextView)findViewById(playerDetails).findViewById(R.id.player_name)).setText(name);
    }

    private void setScore(int playerDetails, float score) {
        ((TextView)findViewById(playerDetails).findViewById(R.id.player_score)).setText(formatScore(score));
    }

    private void flipPlayer2Avatar() {
        findViewById(R.id.player_2_details).findViewById(R.id.player_avatar).setScaleX(-1f);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup the recent matches UI.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        flipPlayer2Avatar();
    }

    @Override
    public ViewModel onFetchViewModel() {

        if(mViewModel == null) {
            mViewModel = new ViewModelDraftDetail(this, this);
        }

        return mViewModel;
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    @Override
    public void onStuff(List<Player> p1roster, List<Player> p2Roster,
                        List<Game> p1games, List<Game> p2games,
                        List<Float> p1Scores, List<Float> p2Scores) {
        mPlayerList.setAdapter(new PlayerListAdapter(getApplicationContext(), p1roster, p2Roster, p1games, p2games,
                p1Scores, p2Scores, this));
    }

    @Override
    public void onMatchup(String player1Name, float player1score, String player2Name, float player2Score) {

        setName(R.id.player_1_details, player1Name);
        setName(R.id.player_2_details, player2Name);

        setScore(R.id.player_1_details, player1score);
        setScore(R.id.player_2_details, player2Score);
    }

    // endregion
}