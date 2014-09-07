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
import com.blitz.app.utilities.sound.SoundHelper;
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

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * When screen is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the fast music (TODO: Move into auth).
        SoundHelper.instance().stopMusic();
        SoundHelper.instance().startMusic(R.raw.music_fast_loop_0, R.raw.music_fast_loop_n);

        playAnimationsIntro();
    }

    /**
     * Runs when activity has been
     * presented to the user.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Enable animations.
        mAnimations.enable();
    }

    /**
     * Run custom transitions if needed, also
     * start timer to detect entering background.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Disable animations.
        mAnimations.disable();
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
     * Show the intro UI.
     */
    private void playAnimationsIntro() {

        // Create animation group.
        mAnimations = AnimHelperSpringsGroup.from(this);

        // Containers smash.
        mAnimations.createHelper(20, 4)
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerLeft, AnimHelperSpringsPresets.SLIDE_RIGHT))
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerRight, AnimHelperSpringsPresets.SLIDE_LEFT));

        // Header flies down.
        mAnimations.createHelper(5, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftHeader, AnimHelperSpringsPresets.SLIDE_DOWN));

        // Set a springs completion listener.
        mAnimations.setOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Fade in the vs text and the spinner.
                AnimHelperFade.setVisibility(mDraftIntroContainerVs, View.VISIBLE);
                AnimHelperFade.setVisibility(mDraftLoadingSpinner, View.VISIBLE);

                // Start the draft.
                mViewModelDraft.startDrafting();
            }
        });
    }

    /**
     * Reverse the intro UI.
     */
    private void playAnimationsIntroReversed() {

        // Remove current helpers.
        mAnimations.removeHelpers();

        // Containers smash.
        mAnimations.createHelper(20, 4)
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerLeft, AnimHelperSpringsPresets.SLIDE_RIGHT_REVERSED))
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerRight, AnimHelperSpringsPresets.SLIDE_LEFT_REVERSED));

        // Header flies up.
        mAnimations.createHelper(5, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftHeader, AnimHelperSpringsPresets.SLIDE_DOWN_REVERSED));

        // Fade out vs and spinner.
        AnimHelperFade.setVisibility(mDraftIntroContainerVs, View.INVISIBLE);
        AnimHelperFade.setVisibility(mDraftLoadingSpinner, View.INVISIBLE);

        // Set a springs completion listener.
        mAnimations.setOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Show drafting UI.
                playAnimationsDrafting();
            }
        });

        // Start it up.
        mAnimations.enable();
    }

    /**
     * Show the drafting UI.
     */
    private void playAnimationsDrafting() {

        // Hide the intro UI.
        mDraftIntroContainer.setVisibility(View.GONE);
        mDraftLoadingSpinner.setVisibility(View.GONE);

        // Bring back the drafting container.
        mDraftContainerDrafting.setVisibility(View.VISIBLE);

        // Remove current helpers.
        mAnimations.removeHelpers();

        // Header flies down.
        mAnimations.createHelper(5, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftHeader, AnimHelperSpringsPresets.SLIDE_DOWN));

        // Containers smash.
        mAnimations.createHelper(20, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftMatchupPlayerLeft,  AnimHelperSpringsPresets.SLIDE_RIGHT))
                .addHelperView(AnimHelperSpringsView.from(mDraftMatchupPlayerRight, AnimHelperSpringsPresets.SLIDE_LEFT));

        // Players pop in.
        mAnimations.createHelper(20, 10)
                .addHelperView(AnimHelperSpringsView.from(mDraftPlayerTl, AnimHelperSpringsPresets.SCALE_UP))
                .addHelperView(AnimHelperSpringsView.from(mDraftPlayerTr, AnimHelperSpringsPresets.SCALE_UP))
                .addHelperView(AnimHelperSpringsView.from(mDraftPlayerBl, AnimHelperSpringsPresets.SCALE_UP))
                .addHelperView(AnimHelperSpringsView.from(mDraftPlayerBr, AnimHelperSpringsPresets.SCALE_UP));

        // Start it up.
        mAnimations.enable();
    }

    // endregion

    // region View Model Callbacks
    // =============================================================================================

    @Override
    public void onDraftingStarted() {

        playAnimationsIntroReversed();
    }

    // endregion
}