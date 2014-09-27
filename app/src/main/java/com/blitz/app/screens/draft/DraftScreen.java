package com.blitz.app.screens.draft;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.animations.AnimHelperSpringsGroup;
import com.blitz.app.utilities.animations.AnimHelperSpringsPresets;
import com.blitz.app.utilities.animations.AnimHelperSpringsView;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDraft;

import butterknife.InjectView;

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

    // Matching containers.
    @InjectView(R.id.draft_matchup_player_left)  View mDraftMatchupPlayerLeft;
    @InjectView(R.id.draft_matchup_player_right) View mDraftMatchupPlayerRight;

    // Player containers.
    @InjectView(R.id.draft_player_tl) View mDraftPlayerTl;
    @InjectView(R.id.draft_player_tr) View mDraftPlayerTr;
    @InjectView(R.id.draft_player_bl) View mDraftPlayerBl;
    @InjectView(R.id.draft_player_br) View mDraftPlayerBr;

    // Loading spinner for the draft.
    @InjectView(R.id.draft_loading) ProgressBar mDraftLoadingSpinner;

    // Header view for the draft.
    @InjectView(R.id.draft_header) TextView mDraftHeader;

    @InjectView(R.id.draft_container_drafting) ViewGroup mDraftContainerDrafting;

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

    private String mPlayer1UserName;
    private String mPlayer2UserName;

    private int mPlayer1Wins;
    private int mPlayer1Losses;
    private int mPlayer1Ties;
    private int mPlayer1Rating;

    private int mPlayer2Wins;
    private int mPlayer2Losses;
    private int mPlayer2Ties;
    private int mPlayer2Rating;

    private String mPlayer1AvatarUrl;
    private String mPlayer2AvatarUrl;

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

                // Re-enter the main application.
                AuthHelper.instance().setCurrentDraft(null);
                AuthHelper.instance().tryEnterMainApp(this);

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

            mPlayer1Id        = userId;
            mPlayer1UserName  = userName;
            mPlayer1Rating    = rating;
            mPlayer1Wins      = wins;
            mPlayer1Losses    = losses;
            mPlayer1Ties      = ties;
            mPlayer1AvatarUrl = itemAvatarUrl;

        } else {

            mPlayer2Id        = userId;
            mPlayer2UserName  = userName;
            mPlayer2Rating    = rating;
            mPlayer2Wins      = wins;
            mPlayer2Losses    = losses;
            mPlayer2Ties      = ties;
            mPlayer2AvatarUrl = itemAvatarUrl;
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

    // endregion
}