package com.blitz.app.screens.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.dialogs.info.DialogInfo;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.blitz.app.utilities.ui.UIObserver;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelMainPlay;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observer;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenFragmentPlay extends BaseFragment implements ViewModelMainPlay.ViewModelMainPlayCallbacks {

    // region Member Variables
    // =============================================================================================

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

    // View model object.
    private ViewModelMainPlay mViewModelMainPlay;

    // Observers for text views. Will be wired later when the view is created in onCreate(0
    private Observer<String> mUserNameObserver;
    private Observer<Integer> mWinsObserver;
    private Observer<Integer> mLossesObserver;
    private Observer<Integer> mRatingObserver;
    private Observer<Integer> mCashObserver;


    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Initialize the fragment.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        mUserNameObserver = UIObserver.textField(mStatsUserName);
        mWinsObserver = UIObserver.textField(mStatsWins);
        mLossesObserver = UIObserver.textField(mStatsLosses);
        mRatingObserver = UIObserver.textField(mStatsRating);
        mCashObserver = new UIObserver.DefaultObserver<Integer>() {

            @Override
            public void onNext(Integer cash) {
                // Set available cash, formatted.
                mCashAvailable.setText("You have $" + String.format("%.2f", cash / 100.0f));
            }
        };

        // Spin baby.
        setupSpinningPlayButton();
    }

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModelMainPlay == null) {
            mViewModelMainPlay = new ViewModelMainPlay();
        }

        mViewModelMainPlay.subscribe(mUserNameObserver, mWinsObserver, mLossesObserver, mRatingObserver, mCashObserver);

        return mViewModelMainPlay;
    }

    // endregion

    // region Private Methods
    // =============================================================================================

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

    /**
     * Create a normal info dialog, one button, with
     * the specified text.
     */
    private void createBasicInfoDialog(int dialogTextResourceId) {

        // Create a new info dialog instance.
        final DialogInfo dialogInfo = new DialogInfo(getActivity());

        // Set coming soon text with standard OK button.
        dialogInfo.setInfoText(dialogTextResourceId);
        dialogInfo.setInfoLeftButton(R.string.ok, new Runnable() {

            @Override
            public void run() {
                dialogInfo.hide(null);
            }
        });

        // Show the dialog.
        dialogInfo.show(true);
    }

    // endregion

    // region View Model Callbacks
    // =============================================================================================

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

    // endregion

    // region Click Methods
    // =============================================================================================

    /**
     * Join (or leave) the draft queue!
     */
    @OnClick(R.id.main_play_button) @SuppressWarnings("unused")
    public void playButtonClicked() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        if (AppConfig.isProduction()) {

            // Drafting is coming soon.
            createBasicInfoDialog(R.string.play_coming_soon);

        } else {

            // Toggle the queue.
            mViewModelMainPlay.toggleQueue();
        }
    }

    /**
     * Add money clicked.
     */
    @OnClick(R.id.main_play_add_money) @SuppressWarnings("unused")
    public void addMoneyClicked() {

        // Add money coming soon.
        createBasicInfoDialog(R.string.add_money_coming_soon);
    }

    /**
     * Rules clicked.
     */
    @OnClick(R.id.main_play_rules) @SuppressWarnings("unused")
    public void rulesClicked() {

        // Rules coming soon.
        createBasicInfoDialog(R.string.rules_coming_soon);
    }

    // endregion
}