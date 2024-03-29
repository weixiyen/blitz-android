package com.blitz.app.screens.draft;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.matchup.MatchupScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelper;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.animations.AnimHelperSpringsGroup;
import com.blitz.app.utilities.animations.AnimHelperSpringsPresets;
import com.blitz.app.utilities.animations.AnimHelperSpringsView;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.blitz.BlitzDelay;
import com.blitz.app.utilities.image.BlitzImage;
import com.blitz.app.utilities.image.BlitzImageView;
import com.blitz.app.utilities.textview.BlitzTextView;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDraft;

import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
public class DraftScreen extends BaseActivity implements ViewModelDraft.Callbacks {

    // region Member Variables
    // ============================================================================================================

    // Draft intro containers.
    @InjectView(R.id.draft_intro)                 View mDraftIntroContainer;
    @InjectView(R.id.draft_intro_container_left)  View mDraftIntroContainerLeft;
    @InjectView(R.id.draft_intro_container_right) View mDraftIntroContainerRight;
    @InjectView(R.id.draft_intro_container_vs)    View mDraftIntroContainerVs;

    // Actual content views of the intro section.
    @InjectView(R.id.draft_intro_header)             TextView mDraftIntroHeader;
    @InjectView(R.id.draft_intro_helmet_left)  BlitzImageView mDraftIntroHelmetLeft;
    @InjectView(R.id.draft_intro_helmet_right) BlitzImageView mDraftIntroHelmetRight;
    @InjectView(R.id.draft_intro_username_left)      TextView mDraftIntroUsernameLeft;
    @InjectView(R.id.draft_intro_username_right)     TextView mDraftIntroUsernameRight;
    @InjectView(R.id.draft_intro_scorecard_left)     TextView mDraftIntroScorecardLeft;
    @InjectView(R.id.draft_intro_scorecard_right)    TextView mDraftIntroScorecardRight;
    @InjectView(R.id.draft_intro_elo_left)           TextView mDraftIntroEloLeft;
    @InjectView(R.id.draft_intro_elo_right)          TextView mDraftIntroEloRight;

    // Matchup containers.
    @InjectView(R.id.draft_matchup_header)             TextView mDraftMatchupHeader;
    @InjectView(R.id.draft_matchup_username_left)      TextView mDraftMatchupUsernameLeft;
    @InjectView(R.id.draft_matchup_username_right)     TextView mDraftMatchupUsernameRight;
    @InjectView(R.id.draft_matchup_spinner)                View mDraftMatchupSpinner;
    @InjectView(R.id.draft_matchup_time_remaining)     TextView mDraftMatchupTimeRemaining;
    @InjectView(R.id.draft_matchup_round_complete)         View mDraftMatchupRoundComplete;

    @InjectViews({
            R.id.draft_player_tl,
            R.id.draft_player_tr,
            R.id.draft_player_bl,
            R.id.draft_player_br
    }) List<View> mDraftPlayers;

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
    }) List<TextView> mDraftPlayerPositions;

    @InjectViews({
            R.id.draft_player_opponent_tl,
            R.id.draft_player_opponent_tr,
            R.id.draft_player_opponent_bl,
            R.id.draft_player_opponent_br
    }) List<TextView> mDraftPlayerOpponents;

    @InjectViews({
            R.id.draft_player_stats_tl,
            R.id.draft_player_stats_tr,
            R.id.draft_player_stats_bl,
            R.id.draft_player_stats_br
    }) List<View> mDraftPlayerStats;

    // Loading spinner for the draft.
    @InjectView(R.id.draft_loading) ProgressBar mDraftLoadingSpinner;

    // Drafting container view.
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

    // Are players in initialized state.
    private boolean mPlayersInitialized;

    // Used for spinner animations.
    private ObjectAnimator mObjectAnimator;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Initialize the UI state.
     *
     * @param savedInstanceState Instance parameters.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Blank screen on create.
        setIntroVisibility(false);
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

        // Stop spinner.
        playSpinnerAnimation(true);
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
    // ============================================================================================================

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

                // Navigate to matchup screen for the completed draft.
                Intent intent = new Intent(this, MatchupScreen.class);
                intent.putExtra(MatchupScreen.PARAM_DRAFT_ID,
                        AuthHelper.instance().getCurrentDraft().getId());
                intent.putExtra(MatchupScreen.PARAM_IS_FROM_DRAFT, true);

                startActivity(intent);

                // Clear the active draft.
                AuthHelper.instance().setCurrentDraft(null);

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
    private void setIntroVisibility(boolean visible) {

        int visibility = visible ? View.VISIBLE : View.GONE;

        // Set the intro UI visibility.
        mDraftIntroContainer
                .setVisibility(visibility);
        mDraftIntroHeader
                .setVisibility(visibility);
        mDraftLoadingSpinner
                .setVisibility(visibility);
    }

    // endregion

    // region Animation Methods
    // ============================================================================================================

    /**
     * Show the intro UI.
     */
    private void playAnimationsIntro() {

        setAnimationsRunning(true);

        setIntroVisibility(true);

        // Containers smash.
        getAnimations().createHelper(20, 4)
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerLeft, AnimHelperSpringsPresets.SLIDE_RIGHT))
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroContainerRight, AnimHelperSpringsPresets.SLIDE_LEFT));

        // Header flies down.
        getAnimations().createHelper(5, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroHeader, AnimHelperSpringsPresets.SLIDE_DOWN));

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
                .addHelperView(AnimHelperSpringsView.from(mDraftIntroHeader, AnimHelperSpringsPresets.SLIDE_DOWN_REVERSED));

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

        setIntroVisibility(false);

        // Bring back the drafting container.
        mDraftContainerDrafting.setVisibility(View.VISIBLE);

        // Remove current helpers.
        getAnimations().removeHelpers();

        // Header flies down.
        getAnimations().createHelper(5, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftMatchupHeader, AnimHelperSpringsPresets.SLIDE_DOWN));

        // Containers smash.
        getAnimations().createHelper(20, 5)
                .addHelperView(AnimHelperSpringsView.from(mDraftMatchupUsernameLeft,  AnimHelperSpringsPresets.SLIDE_RIGHT))
                .addHelperView(AnimHelperSpringsView.from(mDraftMatchupUsernameRight, AnimHelperSpringsPresets.SLIDE_LEFT));

        getAnimations().setOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Now in the draft state.
                mDraftStateCurrent = ViewModelDraft.DraftState.DRAFT_DRAFTING;

                // Done animating.
                setAnimationsRunning(false);
            }
        });

        // Start it up.
        getAnimations().enable();
    }

    /**
     * Set visibility of the choices.
     *
     * @param visibility Visibility.
     * @param complete Complete handler.
     */
    private void setChoicesVisibility(int visibility, Runnable complete) {

        for (View player : mDraftPlayers) {

            // Show each player card.
            AnimHelperFade.setVisibility(player, visibility);
        }

        // Callback on a small delay.
        BlitzDelay.postDelayed(complete, AnimHelper.getConfigAnimTimeStandard() + 100);
    }

    /**
     * Initialize the UI state of the choices.
     */
    private void setChoicesInitialized() {

        for (int i = 0; i < mDraftPlayers.size(); i++) {

            // Set the unselected mask.
            mDraftPlayerStats.get(i).setBackgroundResource(R.drawable.drawable_draft_card);

            // Clear out existing images.
            mDraftPlayerImages.get(i).setImageBitmap(null);

            // Set the unselected alpha.
            mDraftPlayerImages.get(i).setAlpha(0.0f);
        }
    }

    /**
     * Animate the draft spinner.
     *
     * @param suspend Suspend the animation.
     */
    private void playSpinnerAnimation(boolean suspend) {

        if (mObjectAnimator == null) {

            // Create an animator for rotation.
            mObjectAnimator = ObjectAnimator
                    .ofFloat(mDraftMatchupSpinner, "rotation", 0, 360);

            // Time to spin.
            mObjectAnimator.setDuration(1750);

            // Repeat forever.
            mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);

            // Spin linearly.
            mObjectAnimator.setInterpolator(new LinearInterpolator());
        }

        if (suspend) {

            mObjectAnimator.end();
        } else {

            mObjectAnimator.start();
        }
    }

    private void setPlayerImages(
            final List<String> playerIds,
            final List<String> playerPositions,
            final List<String> playerPhotoUrls) {

        // Load new images asynchronously.
        BlitzImage.from(DraftScreen.this).loadImageUrls(playerPhotoUrls,
                "images/raw_player_mask.png", new BlitzImage.CallbackImageUrls() {

                    @Override
                    public void onSuccess(Map<String, Bitmap> images) {

                        for (int i = 0; i < playerIds.size(); i++) {

                            // Do a clean fade animation.
                            AnimHelperFade.setAlpha(mDraftPlayerImages.get(i), 0.0f, 1.0f);

                            // Fetch the bitmap we attempted to load.
                            Bitmap playerBitmap = images.get(playerPhotoUrls.get(i));

                            if (playerBitmap != null) {

                                // Set the player image.
                                mDraftPlayerImages.get(i).setImageBitmap
                                        (images.get(playerPhotoUrls.get(i)));
                            } else {

                                String playerPosition = playerPositions.get(i);

                                if (playerPosition != null) {

                                    if (playerPosition.equals("K")) {
                                        mDraftPlayerImages.get(i).setImageResource
                                                (R.drawable.asset_draft_placeholder_k);
                                    }

                                    if (playerPosition.equals("qb")) {
                                        mDraftPlayerImages.get(i).setImageResource
                                                (R.drawable.asset_draft_placeholder_qb);
                                    }

                                    if (playerPosition.equals("RB")) {
                                        mDraftPlayerImages.get(i).setImageResource
                                                (R.drawable.asset_draft_placeholder_rb);
                                    }

                                    if (playerPosition.equals("TE")) {
                                        mDraftPlayerImages.get(i).setImageResource
                                                (R.drawable.asset_draft_placeholder_te);
                                    }

                                    if (playerPosition.equals("WR")) {
                                        mDraftPlayerImages.get(i).setImageResource
                                                (R.drawable.asset_draft_placeholder_wr);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    // endregion

    // region Click Methods
    // ============================================================================================================

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
    // ============================================================================================================

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
                    .setText(Integer.toString(wins)   + "-" +
                             Integer.toString(losses) + "-" +
                             Integer.toString(ties));

            mDraftMatchupUsernameLeft.setText(userName);

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
                    .setText(Integer.toString(wins)   + "-" +
                             Integer.toString(losses) + "-" +
                             Integer.toString(ties) );

            mDraftMatchupUsernameRight.setText(userName);
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

        if (mDraftMatchupHeader != null) {
            mDraftMatchupHeader.setText(roundAndPosition);
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
            final List<String> playerIds,
            final List<String> playerPhotoUrls,
            final List<String> playerFullNames,
            final List<String> playerTeams,
            final List<String> playerPositions,
            final List<String> playerOpponents) {

        // Runnable to update player choices data.
        final Runnable updateChoicesData = new Runnable() {

            @Override
            public void run() {

                // Initialize.
                setChoicesInitialized();

                for (int i = 0; i < playerIds.size(); i++) {

                    mDraftPlayers.get(i).setTag(playerIds.get(i));

                    // Set name and size to fit.
                    mDraftPlayerNames.get(i).setText(playerFullNames.get(i));
                    mDraftPlayerNames.get(i).setSizeToFit();

                    if (!playerPositions.get(i).isEmpty() &&
                            !playerTeams.get(i).isEmpty()) {

                        mDraftPlayerPositions.get(i).setText(
                                playerPositions.get(i) + " - " + playerTeams.get(i));
                    } else {
                        mDraftPlayerPositions.get(i).setText("");
                    }

                    mDraftPlayerOpponents.get(i).setText(playerOpponents.get(i));
                }

                // Load and set player images.
                setPlayerImages(playerIds, playerPositions, playerPhotoUrls);
            }
        };

        if (!mPlayersInitialized) {
             mPlayersInitialized = true;

            // Set the data.
            updateChoicesData.run();

            // Show immediately if this is the first round.
            setChoicesVisibility(View.VISIBLE, null);

        } else {

            setChoicesVisibility(View.INVISIBLE, new Runnable() {

                @Override
                public void run() {

                    // Set the data.
                    updateChoicesData.run();

                    // Show after first hiding existing choices.
                    setChoicesVisibility(View.VISIBLE, null);
                }
            });
        }
    }

    /**
     * Update the player pick UI when
     * new picks are received.
     *
     * @param playerIds Players picked.
     * @param userIds Users who made the pick.
     */
    public void onPicksChanged(
            List<String> playerIds,
            List<String> userIds) {

        for (int i = 0; i < mDraftPlayers.size(); i++) {

            // Fetch player id inside the view tag.
            String draftPlayerId = (String)mDraftPlayers.get(i).getTag();

            // Continue if no tag set yet.
            if (draftPlayerId == null) {

                continue;
            }

            for (int j = 0; j < playerIds.size(); j++) {

                if (draftPlayerId.equals(playerIds.get(j))) {

                    // Check if this pick made by the user.
                    boolean pickedByUser = userIds.get(j).equals
                            (AuthHelper.instance().getUserId());

                    mDraftPlayerStats.get(i).setBackgroundResource(pickedByUser ?
                            R.drawable.drawable_draft_card_p1 :
                            R.drawable.drawable_draft_card_p2);
                }
            }
        }
    }

    /**
     * Update the text of the time remaining label, which
     * may or may not be currently visible to the user.
     *
     * @param roundTimeRemaining Time remaining.
     */
    @Override
    public void onRoundTimeRemainingChanged(int roundTimeRemaining) {

        // Update text view exists and not hidden.
        if (mDraftMatchupTimeRemaining != null) {
            mDraftMatchupTimeRemaining.setText(Integer.toString(roundTimeRemaining));
        }
    }

    /**
     * Toggles the visibility of the time remaining, this
     * includes the spinner.
     *
     * @param roundTimeRemainingHidden Is time remaining hidden.
     */
    @Override
    public void onRoundTimeRemainingHiddenChanged(boolean roundTimeRemainingHidden) {

        // Set visibility of the UI.
        AnimHelperFade.setVisibility(mDraftMatchupSpinner,
                roundTimeRemainingHidden ? View.GONE : View.VISIBLE);
        AnimHelperFade.setVisibility(mDraftMatchupTimeRemaining,
                roundTimeRemainingHidden ? View.GONE : View.VISIBLE);

        // Start or stop the spinner animation.
        playSpinnerAnimation(roundTimeRemainingHidden);
    }

    /**
     * Is the round complete label showing.
     *
     * @param completeHidden Round complete label.
     */
    @Override
    public void onRoundCompleteHiddenChanged(boolean completeHidden) {

        // Toggle visibility of round complete.
        AnimHelperFade.setVisibility(mDraftMatchupRoundComplete,
                completeHidden ? View.GONE : View.VISIBLE);
    }

    // endregion
}