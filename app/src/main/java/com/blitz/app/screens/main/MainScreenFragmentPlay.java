package com.blitz.app.screens.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.animations.AnimHelperCrossFade;
import com.blitz.app.utilities.animations.AnimHelperFade;
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

        ObjectAnimator oa = ObjectAnimator.ofFloat(mPlayButtonHighlight, "rotation", 0, 360);

        oa.setDuration(1750);
        oa.setRepeatCount(ValueAnimator.INFINITE);
        oa.setInterpolator(new LinearInterpolator());
        oa.start();
    }

    //==============================================================================================
    // View Model Callbacks
    //==============================================================================================

    /**
     * Setup cool button state.
     */
    @Override
    public void onQueueUp(boolean animate) {

        LogHelper.log("Queue");

        mPlayButton.setBackgroundResource(R.drawable.drawable_button_play_cancel);

        AnimHelperCrossFade.setImageResource(mPlayButtonHighlight, R.drawable.asset_play_button_cancel_highlight);
        AnimHelperCrossFade.setImageResource(mPlayButtonOutline,   R.drawable.asset_play_button_cancel_outline);

        AnimHelperFade.setVisibility(mPlayButtonText,    View.GONE);
        AnimHelperFade.setVisibility(mPlayButtonTime,    View.VISIBLE);
        AnimHelperFade.setVisibility(mPlayButtonWaiting, View.VISIBLE);
    }

    /**
     * Restore normal button state.
     */
    @Override
    public void onQueueCancel(boolean animate) {

        LogHelper.log("Cancel");

        mPlayButton.setBackgroundResource(R.drawable.drawable_button_play);

        AnimHelperCrossFade.setImageResource(mPlayButtonHighlight, R.drawable.asset_play_button_play_highlight);
        AnimHelperCrossFade.setImageResource(mPlayButtonOutline, R.drawable.asset_play_button_play_outline);

        AnimHelperFade.setVisibility(mPlayButtonText,    View.VISIBLE);
        AnimHelperFade.setVisibility(mPlayButtonTime,    View.GONE);
        AnimHelperFade.setVisibility(mPlayButtonWaiting, View.GONE);
    }

    /**
     * Update time display on tick.
     */
    @Override
    public void onQueueTick(String secondsInQueue) {

        // Set to current second count.
        mPlayButtonTime.setText(secondsInQueue);
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Join (or leave) the draft queue!
     */
    @OnClick(R.id.main_play_button) @SuppressWarnings("unused")
    public void main_play() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        // Toggle the queue.
        getViewModel(ViewModelMainPlay.class).toggleQueue();
    }
}