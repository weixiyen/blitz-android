package com.blitz.app.screens.play;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.dialogs.rules.DialogRules;
import com.blitz.app.screens.leaderboard.LeaderboardScreen;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.animations.AnimHelper;
import com.blitz.app.utilities.animations.AnimHelperCrossFade;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.image.BlitzImageView;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelPlay;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class PlayScreen extends BaseFragment implements ViewModelPlay.Callbacks {

    // region Member Variables
    // =============================================================================================

    // Play button related views.
    @InjectView(R.id.main_play_button)                View mPlayButton;
    @InjectView(R.id.main_play_button_highlight) ImageView mPlayButtonHighlight;
    @InjectView(R.id.main_play_button_outline)   ImageView mPlayButtonOutline;
    @InjectView(R.id.main_play_button_text)       TextView mPlayButtonText;
    @InjectView(R.id.main_play_button_time)       TextView mPlayButtonTime;
    @InjectView(R.id.main_play_button_waiting)        View mPlayButtonWaiting;

    // UI Containers.
    @InjectView(R.id.main_play_action)  View mPlayContainerAction;
    @InjectView(R.id.main_play_blocked) View mPlayContainerBlocked;
    @InjectView(R.id.main_play_footer)  View mPlayContainerFooter;

    @InjectView(R.id.main_play_cash_available) TextView mCashAvailable;

    @InjectView(R.id.main_play_add_money)         View mPlayAddMoney;
    @InjectView(R.id.main_play_add_money_divider) View mPlayAddMoneyDivider;

    @InjectView(R.id.main_play_stats_username)     TextView mStatsUserName;
    @InjectView(R.id.main_play_stats_rating)       TextView mStatsRating;
    @InjectView(R.id.main_play_stats_wins)         TextView mStatsWins;
    @InjectView(R.id.main_play_stats_losses)       TextView mStatsLosses;
    @InjectView(R.id.main_play_stats_avatar) BlitzImageView mStatsAvatar;

    // View model object.
    private ViewModelPlay mViewModelPlay;

    // Powers animations.
    private ObjectAnimator mObjectAnimator;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Show rules dialog if needed.
     *
     * @param savedInstanceState Saved state.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        if (!AppDataObject.hasSeenRules.get()) {

            // User has no seen the rules.
            AppDataObject.hasSeenRules.set(true);

            // Manually show.
            rulesClicked();
        }

        if (AppConfig.isProduction()) {

            // Disable cash games for the time being.
            mCashAvailable.setText(R.string.add_money_coming_soon);

            mPlayAddMoney
                    .setVisibility(View.GONE);
            mPlayAddMoneyDivider
                    .setVisibility(View.GONE);
        }
    }

    /**
     * Start animations.
     */
    @Override
    public void onPause() {
        super.onPause();

        setupSpinningPlayButton(false);
    }

    /**
     * Every time this fragment becomes visible,
     * we should make sure the helmet is updated
     * to the most recent one.
     *
     * @param isVisibleToUser Is fragment visible to user.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Try to update helmet if needed.
        if (mViewModelPlay != null && isVisibleToUser) {
            mViewModelPlay.fetchUserInfo();
        }
    }

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModelPlay == null) {
            mViewModelPlay = new ViewModelPlay(getBaseActivity(), this);
        }

        return mViewModelPlay;
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Animate the play button.
     *
     * @param suspend Suspend the animation.
     */
    private void setupSpinningPlayButton(boolean suspend) {

        if (mObjectAnimator == null) {

            // Create an animator for rotation.
            mObjectAnimator = ObjectAnimator
                    .ofFloat(mPlayButtonHighlight, "rotation", 0, 360);

            // Time to spin.
            mObjectAnimator.setDuration(1750);

            // Repeat forever.
            mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);

            // Spin linearly.
            mObjectAnimator.setInterpolator(new LinearInterpolator());
        }

        if (suspend) {

            mObjectAnimator.start();
        } else {

            mObjectAnimator.end();
        }
    }

    // endregion

    // region View Model Callbacks
    // =============================================================================================

    /**
     * Show roadblock if the queue is not available.
     *
     * @param queueAvailable Is queue available.
     */
    @Override
    public void onQueueAvailable(boolean queueAvailable) {

        // Animation time for showing the resulting UI is twice the usual.
        int animationTime = AnimHelper.getConfigAnimTimeStandard(this.getActivity()) * 3;

        if (queueAvailable) {

            // Show the play UI.
            AnimHelperFade.setVisibility(mPlayContainerAction, View.VISIBLE, animationTime);
            AnimHelperFade.setVisibility(mPlayContainerFooter, View.VISIBLE, animationTime);

            // Play button so shiny.
            setupSpinningPlayButton(true);
        } else {

            // Show the blocked container.
            AnimHelperFade.setVisibility(mPlayContainerBlocked, View.VISIBLE, animationTime);
        }
    }

    /**
     * Setup cool button state.
     */
    @Override
    public void onQueueUp(boolean animate) {

        mPlayButton
                .setBackgroundResource(R.drawable.drawable_button_play_cancel);
        mPlayButtonHighlight
                .setImageResource(R.drawable.asset_play_button_cancel_highlight);
        mPlayButtonOutline
                .setImageResource(R.drawable.asset_play_button_cancel_outline);

        mPlayButtonText
                .setVisibility(View.GONE);
        mPlayButtonTime
                .setVisibility(View.VISIBLE);
        mPlayButtonWaiting
                .setVisibility(View.VISIBLE);
    }

    /**
     * Restore normal button state.
     */
    @Override
    public void onQueueCancel(boolean animate) {

        mPlayButton
                .setBackgroundResource(R.drawable.drawable_button_play);
        mPlayButtonHighlight
                .setImageResource(R.drawable.asset_play_button_play_highlight);
        mPlayButtonOutline
                .setImageResource(R.drawable.asset_play_button_play_outline);

        mPlayButtonText
                .setVisibility(View.VISIBLE);
        mPlayButtonTime
                .setVisibility(View.GONE);
        mPlayButtonWaiting
                .setVisibility(View.GONE);
    }

    /**
     * Update time display on tick.
     */
    @Override
    public void onQueueTick(String secondsInQueue) {

        if (mPlayButtonTime != null) {
            mPlayButtonTime.setText(secondsInQueue);
        }
    }

    /**
     * When username changes.
     */
    @Override
    public void onUsername(String username) {

        if (mStatsUserName != null) {
            mStatsUserName.setText(username);
        }
    }

    /**
     * When rating changes.
     */
    @Override
    public void onRating(int rating) {

        if (mStatsRating != null) {
            mStatsRating.setText(Integer.toString(rating));
        }
    }

    /**
     * When wins change.
     */
    @Override
    public void onWins(int wins) {

        if (mStatsWins != null) {
            mStatsWins.setText(Integer.toString(wins));
        }
    }

    /**
     * When losses change.
     */
    @Override
    public void onLosses(int losses) {

        if (mStatsLosses != null) {
            mStatsLosses.setText(Integer.toString(losses));
        }
    }

    /**
     * When cash changes.
     */
    @Override
    public void onCash(int cash) {

        if (mCashAvailable != null && !AppConfig.isProduction()) {
            mCashAvailable.setText("You have $" + String.format("%.2f", cash / 100.0f));
        }
    }

    /**
     * When image path changes.
     */
    @Override
    public void onAvatarUrl(String avatarUrl) {

        // If container exists, and this is a unique url.
        if (mStatsAvatar != null && (mStatsAvatar.getImageUrl() == null ||
                !mStatsAvatar.getImageUrl().equals(avatarUrl))) {

            // Set image url with a nice cross fade effect.
            AnimHelperCrossFade.setImageUrl(mStatsAvatar, avatarUrl);
        }
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    /**
     * Join (or leave) the draft queue!
     */
    @OnClick(R.id.main_play_button) @SuppressWarnings("unused")
    public void playButtonClicked() {

        if (RestAPICallback.shouldThrottle()) {
            return;
        }

        // Toggle the queue.
        mViewModelPlay.toggleQueue();
    }

    /**
     * Add money clicked.
     */
    @OnClick(R.id.main_play_add_money) @SuppressWarnings("unused")
    public void addMoneyClicked() {

    }

    /**
     * Rules clicked, show a dialog that
     * contains rule cards.
     */
    @OnClick(R.id.main_play_rules) @SuppressWarnings("unused")
    public void rulesClicked() {

        // Show the rules dialog fragment.
        new DialogRules().show(getChildFragmentManager(),
                DialogRules.class.toString());
    }

    /**
     * Leaderboard clicked.
     */
    @OnClick(R.id.main_play_leaderboard_link) @SuppressWarnings("unused")
    public void leaderboardClicked() {

        // Navigate to leaderboard.
        Intent intent = new Intent(getActivity(), LeaderboardScreen.class);
        startActivity(intent);
    }

    // endregion
}