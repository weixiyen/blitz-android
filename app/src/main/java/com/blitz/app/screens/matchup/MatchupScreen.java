package com.blitz.app.screens.matchup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestModelGame;
import com.blitz.app.rest_models.RestModelPlayer;
import com.blitz.app.rest_models.RestModelStats;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.image.BlitzImageView;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelMatchup;

import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MatchupScreen extends BaseActivity implements ViewModelMatchup.Callbacks {

    // region Member Variables
    // ============================================================================================================

    // Did we enter this screen directly from finishing a draft.
    public static final String PARAM_IS_FROM_DRAFT = "PARAM_IS_FROM_DRAFT";

    // The passed in draft id.
    public static final String PARAM_DRAFT_ID = "PARAM_DRAFT_ID";

    // The draft that this matchup is
    // associated with, cannot be null.
    private String mDraftId;

    // Are we transitioning from draft.
    private boolean mIsFromDraft;

    private ViewModelMatchup mViewModel;

    @InjectView(R.id.main_draft_detail_player_list)     ListView mPlayerList;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Setup the recent matches UI.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If no draft id exists.
        if (mDraftId == null) {

            // A draft id is required for this screen.
            AuthHelper.instance().tryEnterMainApp(this);
        }

        flipPlayer2Avatar();
    }


    /**
     * Conditionally navigate to the main screen.
     */
    @Override
    public void onBackPressed() {

        if (mIsFromDraft) {

            // Transition directly into main app.
            AuthHelper.instance().tryEnterMainApp(this);
        } else {

            // Normal back behavior.
            super.onBackPressed();
        }
    }

    /**
     * Fetch view model with draft id, which
     * requires immediate parameter parsing.
     *
     * @return Initialized view model.
     */
    @Override
    public ViewModel onFetchViewModel() {

        // Parse params.
        parseIntentParameters();

        if (mViewModel == null) {
            mViewModel = new ViewModelMatchup(this, this, mDraftId);
        }

        return mViewModel;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Parse parameters into this screen.
     */
    private void parseIntentParameters() {

        Intent intent = getIntent();

        // Ensure intent is no null.
        if (intent != null) {

            // Fetch associated draft id (required).
            mDraftId = intent.getStringExtra(MatchupScreen.PARAM_DRAFT_ID);

            // Fetch is we are from draft screen.
            mIsFromDraft = intent.getBooleanExtra(MatchupScreen.PARAM_IS_FROM_DRAFT, false);
        }
    }

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
                .setTextColor(getResources().getColor(R.color.active_blue));
    }

    private void flipPlayer2Avatar() {
        findViewById(R.id.player2_details).findViewById(R.id.player_avatar).setScaleX(-1f);
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    @Override
    public void onStuff(List<RestModelPlayer> p1roster, List<RestModelPlayer> p2Roster,
                        List<RestModelGame> p1games, List<RestModelGame> p2games,
                        Map<String, List<RestModelStats>> playerStats, int week) {
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

    // endregion
}