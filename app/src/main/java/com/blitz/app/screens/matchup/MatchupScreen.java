package com.blitz.app.screens.matchup;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestModelPlayer;
import com.blitz.app.screens.main.PlayerListAdapter;
import com.blitz.app.simple_models.Game;
import com.blitz.app.simple_models.Stat;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.image.BlitzImageView;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDraftDetail;
import com.google.common.collect.Multimap;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MatchupScreen extends BaseActivity implements ViewModelDraftDetail.ViewModelDraftDetailCallbacks {

    public static final String NAVIGATE_TO_PLAY_SCREEN = "MatchupScreen.navigateToPlayScreen";

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

    /**
     * Updates the UI for the provided view group to show that the associated player has the
     * higher score.
     */
    private void indicateLeader(int playerDetails) {
        ((TextView)findViewById(playerDetails).findViewById(R.id.player_score))
                .setTextColor(getResources().getColor(R.color.text_color_leader_blue));
    }

    private void flipPlayer2Avatar() {
        findViewById(R.id.player2_details).findViewById(R.id.player_avatar).setScaleX(-1f);
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
    public void onStuff(List<RestModelPlayer> p1roster, List<RestModelPlayer> p2Roster,
                        List<Game> p1games, List<Game> p2games,
                        Multimap<String, Stat> playerStats, int week) {
        mPlayerList.setAdapter(new PlayerListAdapter(this, p1roster, p2Roster, p1games, p2games,
                playerStats, week, this));
    }

    @Override
    public void onMatchup(String player1Name, float player1score,
                          String player2Name, float player2Score) {

        setName(R.id.player1_details, player1Name);
        setName(R.id.player2_details, player2Name);

        setScore(R.id.player1_details, player1score);
        setScore(R.id.player2_details, player2Score);

        if(player1score > player2Score) {
            indicateLeader(R.id.player1_details);
        } else if(player2Score > player1score) {
            indicateLeader(R.id.player2_details);
        }

        findViewById(R.id.loading_message).setVisibility(View.INVISIBLE);
        findViewById(R.id.matchup_data).setVisibility(View.VISIBLE);
    }

    @Override
    public void onAvatars(String player1AvatarUrl, String player2AvatarUrl) {
        ((BlitzImageView)findViewById(R.id.player1_details).findViewById(R.id.player_avatar))
                .setImageUrl(player1AvatarUrl);

        ((BlitzImageView)findViewById(R.id.player2_details).findViewById(R.id.player_avatar))
                .setImageUrl(player2AvatarUrl);
    }

    /**
     * Conditionally navigate to the main screen.
     */
    @Override
    public void onBackPressed() {

        if (getIntent().getBooleanExtra(NAVIGATE_TO_PLAY_SCREEN, false)) {

            AuthHelper.instance().tryEnterMainApp(this);
        } else {

            super.onBackPressed();
        }
    }


    // endregion
}