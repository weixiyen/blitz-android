package com.blitz.app.screens.draft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.main.MatchInfoAdapter;
import com.blitz.app.screens.main.MatchupScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.animations.AnimHelperSpringsGroup;
import com.blitz.app.utilities.animations.AnimHelperSpringsPresets;
import com.blitz.app.utilities.animations.AnimHelperSpringsView;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.imageview.BlitzImageView;
import com.blitz.app.utilities.textview.BlitzTextView;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDraft;

import java.util.List;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
public class DraftScreen extends BaseActivity implements ViewModelDraft.ViewModelDraftCallbacks {

    // region Member Variables
    // =============================================================================================

    // Draft intro containers.
    @InjectView(R.id.draft_intro)                 View mDraftIntroContainer;
    @InjectView(R.id.draft_intro_container_left)  View mDraftIntroContainerLeft;
    @InjectView(R.id.draft_intro_container_right) View mDraftIntroContainerRight;
    @InjectView(R.id.draft_intro_container_vs)    View mDraftIntroContainerVs;

    // Matchup containers.
    @InjectView(R.id.draft_matchup_player_left)            View mDraftMatchupPlayerLeft;
    @InjectView(R.id.draft_matchup_player_right)           View mDraftMatchupPlayerRight;
    @InjectView(R.id.draft_matchup_helmet_left)  BlitzImageView mDraftMatchupHelmetLeft;
    @InjectView(R.id.draft_matchup_helmet_right) BlitzImageView mDraftMatchupHelmetRight;
    @InjectView(R.id.draft_matchup_username_left)      TextView mDraftMatchupUsernameLeft;
    @InjectView(R.id.draft_matchup_username_right)     TextView mDraftMatchupUsernameRight;

    // Player containers.
    @InjectView(R.id.draft_player_tl) View mDraftPlayerTl;
    @InjectView(R.id.draft_player_tr) View mDraftPlayerTr;
    @InjectView(R.id.draft_player_bl) View mDraftPlayerBl;
    @InjectView(R.id.draft_player_br) View mDraftPlayerBr;

    @InjectViews({
            R.id.draft_player_image_tl,
            R.id.draft_player_image_tr,
            R.id.draft_player_image_bl,
            R.id.draft_player_image_br
    }) List<BlitzImageView> mDraftPlayerImages;

    @InjectViews({
            R.id.draft_player_name_tl,
            R.id.draft_player_name_tr,
            R.id.draft_player_name_bl,
            R.id.draft_player_name_br
    }) List<BlitzTextView> mDraftPlayerNames;

    @InjectViews({
            R.id.draft_player_info_tl,
            R.id.draft_player_info_tr,
            R.id.draft_player_info_bl,
            R.id.draft_player_info_br
    }) List<BlitzTextView> mDraftPlayerPositions;

    @InjectViews({
            R.id.draft_player_opponent_tl,
            R.id.draft_player_opponent_tr,
            R.id.draft_player_opponent_bl,
            R.id.draft_player_opponent_br
    }) List<BlitzTextView> mDraftPlayerOpponents;

    // Loading spinner for the draft.
    @InjectView(R.id.draft_loading) ProgressBar mDraftLoadingSpinner;

    // Header view for the draft.
    @InjectView(R.id.draft_header) TextView mDraftHeader;

    // Drafting container view.
    @InjectView(R.id.draft_container_drafting) ViewGroup mDraftContainerDrafting;

    // Actual content views of the intro section.
    @InjectView(R.id.draft_intro_helmet_left)  BlitzImageView mDraftIntroHelmetLeft;
    @InjectView(R.id.draft_intro_helmet_right) BlitzImageView mDraftIntroHelmetRight;
    @InjectView(R.id.draft_intro_username_left)      TextView mDraftIntroUsernameLeft;
    @InjectView(R.id.draft_intro_username_right)     TextView mDraftIntroUsernameRight;
    @InjectView(R.id.draft_intro_scorecard_left)     TextView mDraftIntroScorecardLeft;
    @InjectView(R.id.draft_intro_scorecard_right)    TextView mDraftIntroScorecardRight;
    @InjectView(R.id.draft_intro_elo_left)           TextView mDraftIntroEloLeft;
    @InjectView(R.id.draft_intro_elo_right)          TextView mDraftIntroEloRight;

    // View model object.
    private ViewModelDraft mViewModelDraft;

    // Page animations.
    private AnimHelperSpringsGroup mAnimations;

    // Flag to see if animations are running.
    private boolean mAnimationsRunning;

    // Pointer to the current state of the draft.
    private ViewModelDraft.DraftState mDraftState;
    private ViewModelDraft.DraftState mDraftStateCurrent;

    // Tracks if player info loaded.
    private String mPlayer1Id;
    private String mPlayer2Id;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Initialize the UI state.
     *
     * @param savedInstanceState Instance parameters.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Blank screen on create.
        setIntroUIVisibility(false);
    }

    /**
     * Sync UI on resume.
     */
    @Override
    protected void onResume() {
        super.onResume();

        syncDraftUIState();
    }

    /**
     * Kill all UI animations on pause.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Hard stop animations.
        mAnimationsRunning = false;

        // Disable animations.
        getAnimations().disable();
    }

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModelDraft == null) {
            mViewModelDraft = new ViewModelDraft(this, this);
        }

        return mViewModelDraft;
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Attempt to set the draft UI state based
     * on the state of the view model and timing
     * it with the actual animations.
     */
    private void syncDraftUIState() {

        // Do nothing if animating or no state.
        if (mAnimationsRunning || mDraftState == mDraftStateCurrent) {

            return;
        }

        switch (mDraftState) {
            case DRAFT_PREVIEW:

                // If player data is loaded.
                if (mPlayer1Id != null && mPlayer2Id != null) {

                    // Show intro UI.
                    playAnimationsIntro();
                }

                break;
            case DRAFT_DRAFTING:

                // If in the draft preview state.
                if (mDraftStateCurrent == ViewModelDraft.DraftState.DRAFT_PREVIEW) {

                    // Hide intro UI and show draft UI.
                    playAnimationsIntroReversed();

                } else {

                    // Show the draft UI.
                    playAnimationsDrafting();
                }

                break;
            case DRAFT_COMPLETE:

                // Immediately switch state.
                mDraftStateCurrent = mDraftState;

                // Navigate to the Matchup screen for the completed draft.
                Intent intent = new Intent(this, MatchupScreen.class);
                intent.putExtra(MatchInfoAdapter.DRAFT_ID, AuthHelper.instance().getCurrentDraft().getId());
                intent.putExtra(MatchupScreen.NAVIGATE_TO_PLAY_SCREEN, true);
                AuthHelper.instance().setCurrentDraft(null); // We're done with this draft.
                startActivity(intent);

                break;
        }
    }

    /**
     * If animations are no longer running,
     * check to see if we need to update UI state.
     *
     * @param animationsRunning Are animations running.
     */
    private void setAnimationsRunning(boolean animationsRunning) {

        mAnimationsRunning = animationsRunning;

        if (!mAnimationsRunning) {

            // Animations might require buffering
            // of the UI state, verify we are
            // int he correct state.
            syncDraftUIState();
        }
    }

    /**
     * Lazy load the animations group. There is no single
     * point of initialization because animations
     * can start from many entry points.
     *
     * @return Animations group, used for bouncy animations.
     */
    private AnimHelperSpringsGroup getAnimations() {

        if (mAnimations == null) {
            mAnimations = AnimHelperSpringsGroup.from(this);
        }

        return mAnimations;
    }

    /**
     * Set visibility of the intro UI.
     *
     * @param visible Is visible.
     */
    private void setIntroUIVisibility(boolean visible) {

        // Set the intro UI visibility.
        mDraftIntroContainer
                .setVisibility(visible ? View.VISIBLE : View.GONE);
        mDraftHeader
                .setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    // endregion

    // region Animation Methods
    // =============================================================================================

    /**
     * Show the intro UI.
     */
    private void playAnimationsIntro() {

        setAnimationsRunning(true);

        setIntroUIVisibility(true);

        // Containers smash.
        getAnimations().createHelper(20, 4)
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerLeft, AnimHelperSpringsPresets.SLIDE_RIGHT))
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerRight, AnimHelperSpringsPresets.SLIDE_LEFT));

        // Header flies down.
        getAnimations().createHelper(5, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftHeader, AnimHelperSpringsPresets.SLIDE_DOWN));

        // Set a springs completion listener.
        getAnimations().setOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Now in the preview state.
                mDraftStateCurrent = ViewModelDraft.DraftState.DRAFT_PREVIEW;

                // Fade in the vs text and the spinner.
                AnimHelperFade.setVisibility(mDraftIntroContainerVs, View.VISIBLE);
                AnimHelperFade.setVisibility(mDraftLoadingSpinner, View.VISIBLE);

                setAnimationsRunning(false);
            }
        });

        // Enable animations.
        getAnimations().enable();
    }

    /**
     * Reverse the intro UI.
     */
    private void playAnimationsIntroReversed() {

        setAnimationsRunning(true);

        // Remove current helpers.
        getAnimations().removeHelpers();

        // Containers smash.
        getAnimations().createHelper(20, 4)
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerLeft, AnimHelperSpringsPresets.SLIDE_RIGHT_REVERSED))
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerRight, AnimHelperSpringsPresets.SLIDE_LEFT_REVERSED));

        // Header flies up.
        getAnimations().createHelper(5, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftHeader, AnimHelperSpringsPresets.SLIDE_DOWN_REVERSED));

        // Fade out vs and spinner.
        AnimHelperFade.setVisibility(mDraftIntroContainerVs, View.INVISIBLE);
        AnimHelperFade.setVisibility(mDraftLoadingSpinner, View.INVISIBLE);

        // Set a springs completion listener.
        getAnimations().setOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Show drafting UI.
                playAnimationsDrafting();
            }
        });

        // Start it up.
        getAnimations().enable();
    }

    /**
     * Show the drafting UI.
     */
    private void playAnimationsDrafting() {

        setAnimationsRunning(true);

        // Hide the intro UI.
        mDraftIntroContainer.setVisibility(View.GONE);
        mDraftLoadingSpinner.setVisibility(View.GONE);

        // Bring back the drafting container.
        mDraftContainerDrafting.setVisibility(View.VISIBLE);

        // Remove current helpers.
        getAnimations().removeHelpers();

        // Header flies down.
        getAnimations().createHelper(5, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftHeader, AnimHelperSpringsPresets.SLIDE_DOWN));

        // Containers smash.
        getAnimations().createHelper(20, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftMatchupPlayerLeft,  AnimHelperSpringsPresets.SLIDE_RIGHT))
                .addHelperView(AnimHelperSpringsView.from(mDraftMatchupPlayerRight, AnimHelperSpringsPresets.SLIDE_LEFT));

        // Players pop in.
        getAnimations().createHelper(20, 10)
                .addHelperView(AnimHelperSpringsView.from(mDraftPlayerTl, AnimHelperSpringsPresets.SCALE_UP))
                .addHelperView(AnimHelperSpringsView.from(mDraftPlayerTr, AnimHelperSpringsPresets.SCALE_UP))
                .addHelperView(AnimHelperSpringsView.from(mDraftPlayerBl, AnimHelperSpringsPresets.SCALE_UP))
                .addHelperView(AnimHelperSpringsView.from(mDraftPlayerBr, AnimHelperSpringsPresets.SCALE_UP));

        getAnimations().setOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Now in the draft state.
                mDraftStateCurrent = ViewModelDraft.DraftState.DRAFT_DRAFTING;

                setAnimationsRunning(false);
            }
        });

        // Start it up.
        getAnimations().enable();
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    @OnClick({
            R.id.draft_player_tl,
            R.id.draft_player_tr,
            R.id.draft_player_bl,
            R.id.draft_player_br })
    @SuppressWarnings("unused")
    public void playerClicked(View playerView) {

        if (playerView.getTag() != null) {

            // Pick player and provide player id.
            mViewModelDraft.pickPlayer(playerView.getTag().toString());
        }
    }

    // endregion

    // region View Model Callbacks
    // =============================================================================================

    /**
     * Received when a users information is received.
     *
     * @param userId Id.
     * @param userName Username.
     * @param rating Elo rating.
     * @param wins Wins.
     * @param losses Losses.
     * @param ties Ties.
     * @param itemAvatarUrl Avatar url.
     */
    @Override
    public void onUserSynced(String userId, String userName, int rating, int wins, int losses, int ties, String itemAvatarUrl) {

        if (AuthHelper.instance().getUserId().equals(userId)) {

            // Set id.
            mPlayer1Id = userId;

            mDraftIntroHelmetLeft
                    .setImageUrl(itemAvatarUrl);
            mDraftIntroUsernameLeft
                    .setText(userName);
            mDraftIntroEloLeft
                    .setText(Integer.toString(rating));
            mDraftIntroScorecardLeft
                    .setText(Integer.toString(wins) + "-" +
                             Integer.toString(ties) + "-" +
                             Integer.toString(losses));

            mDraftMatchupHelmetLeft
                    .setImageUrl(itemAvatarUrl);
            mDraftMatchupUsernameLeft
                    .setText(userName);

        } else {

            // Set id.
            mPlayer2Id = userId;

            mDraftIntroHelmetRight
                    .setImageUrl(itemAvatarUrl);
            mDraftIntroUsernameRight
                    .setText(userName);
            mDraftIntroEloRight
                    .setText(Integer.toString(rating));
            mDraftIntroScorecardRight
                    .setText(Integer.toString(wins) + "-" +
                             Integer.toString(ties) + "-" +
                             Integer.toString(losses));

            mDraftMatchupHelmetRight
                    .setImageUrl(itemAvatarUrl);
            mDraftMatchupUsernameRight
                    .setText(userName);
        }

        syncDraftUIState();
    }

    /**
     * Save the draft state on change and
     * if possible update the UI.
     *
     * @param state New draft state.
     */
    @Override
    public void onDraftStateChanged(ViewModelDraft.DraftState state) {

        // Update state.
        mDraftState = state;

        syncDraftUIState();
    }

    /**
     * Update header label when new round
     * string comes in.
     *
     * @param roundAndPosition New round and position if available.
     */
    @Override
    public void onRoundAndPositionChanged(String roundAndPosition) {

        if (mDraftHeader != null) {
            mDraftHeader.setText(roundAndPosition);
        }
    }

    /**
     * Update the player choices UI when we
     * receive new information.
     *
     * @param playerIds Player ids.
     * @param playerPhotoUrls Photo urls.
     * @param playerFullNames Full names.
     * @param playerPositions Positions.
     * @param playerOpponents Opponents.
     */
    @Override
    public void onPlayerChoicesChanged(
            List<String> playerIds,
            List<String> playerPhotoUrls,
            List<String> playerFullNames,
            List<String> playerPositions,
            List<String> playerOpponents) {

        mDraftPlayerTl.setTag(playerIds.get(0));
        mDraftPlayerTr.setTag(playerIds.get(1));
        mDraftPlayerBl.setTag(playerIds.get(2));
        mDraftPlayerBr.setTag(playerIds.get(3));

        for (int i = 0; i < playerPhotoUrls.size(); i++) {

            mDraftPlayerImages
                    .get(i).setImageUrl(playerPhotoUrls.get(i), "images/raw_player_mask.png");
            mDraftPlayerNames
                    .get(i).setText(playerFullNames.get(i));
            mDraftPlayerPositions
                    .get(i).setText(playerPositions.get(i));
            mDraftPlayerOpponents
                    .get(i).setText(playerOpponents.get(i));
        }
    }

    // endregion
}