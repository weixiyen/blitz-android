package com.blitz.app.screens.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.dialogs.DialogInfo;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.logging.LogHelper;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.blitz.app.view_models.ViewModelMainPlay;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreenFragmentPlay extends BaseFragment implements ViewModelMainPlay.ViewModelMainPlayCallbacks {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Play button related views.
    @InjectView(R.id.main_play_button)                View mPlayButton;
    @InjectView(R.id.main_play_button_highlight) ImageView mPlayButtonHighlight;
    @InjectView(R.id.main_play_button_outline)   ImageView mPlayButtonOutline;
    @InjectView(R.id.main_play_button_text)       TextView mPlayButtonText;
    @InjectView(R.id.main_play_button_time)       TextView mPlayButtonTime;
    @InjectView(R.id.main_play_button_waiting)        View mPlayButtonWaiting;

    @InjectView(R.id.main_play_cash_available) TextView mCashAvailable;

    @InjectView(R.id.main_play_stats_username) TextView mStatsUserName;
    @InjectView(R.id.main_play_stats_rating)   TextView mStatsRating;
    @InjectView(R.id.main_play_stats_wins)     TextView mStatsWins;
    @InjectView(R.id.main_play_stats_losses)   TextView mStatsLosses;

    private DialogInfo mInfoDialog;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Initialize the fragment.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        // Initialize view model.
        setViewModel(new ViewModelMainPlay(), savedInstanceState);

        // Spin baby.
        setupSpinningPlayButton();

        mInfoDialog = new DialogInfo(getActivity());
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Animate play button.
     */
    private void setupSpinningPlayButton() {

        // Create an animator for rotation.
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofFloat(mPlayButtonHighlight, "rotation", 0, 360);

        // Time to spin.
        objectAnimator.setDuration(1750);

        // Repeat forever.
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);

        // Spin linearly.
        objectAnimator.setInterpolator(new LinearInterpolator());

        // Start animating.
        objectAnimator.start();
    }

    //==============================================================================================
    // View Model Callbacks
    //==============================================================================================

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

        mPlayButtonTime.setText(secondsInQueue);
    }

    /**
     * When username changes.
     */
    @Override
    public void onUsername(String username) {

        mStatsUserName.setText(username);
    }

    /**
     * When rating changes.
     */
    @Override
    public void onRating(int rating) {

        mStatsRating.setText(Integer.toString(rating));
    }

    /**
     * When wins change.
     */
    @Override
    public void onWins(int wins) {

        mStatsWins.setText(Integer.toString(wins));
    }

    /**
     * When losses change.
     */
    @Override
    public void onLosses(int losses) {

        mStatsLosses.setText(Integer.toString(losses));
    }

    /**
     * When cash changes.
     */
    @Override
    public void onCash(int cash) {

        // Set available cash, formatted.
        mCashAvailable.setText("You have $" + String.format("%.2f", cash / 100.0f));
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Join (or leave) the draft queue!
     */
    @OnClick(R.id.main_play_button) @SuppressWarnings("unused")
    public void playButtonClicked() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        // Toggle the queue.
        getViewModel(ViewModelMainPlay.class).toggleQueue();
    }

    /**
     * Add money clicked.
     */
    @OnClick(R.id.main_play_add_money) @SuppressWarnings("unused")
    public void addMoneyClicked() {

        // Set coming soon text with standard OK button.
        mInfoDialog.setInfoText(R.string.add_money_coming_soon);
        mInfoDialog.setInfoLeftButton(R.string.ok, new Runnable() {

            @Override
            public void run() {
                mInfoDialog.hide(null);
            }
        });

        // Show the dialog.
        mInfoDialog.show(true);
    }

    /**
     * Rules clicked.
     */
    @OnClick(R.id.main_play_rules) @SuppressWarnings("unused")
    public void rulesClicked() {

        LogHelper.log("Rules.");
    }
}