package com.blitz.app.screens.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
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

    // Container views.
    @InjectView(R.id.main_play_timeline_container) View mTimelineContainer;
    @InjectView(R.id.main_play_queued_container) View mQueuedContainer;

    // Queue timer.
    @InjectView(R.id.main_play_queued_timer) TextView mQueuedTimerTextView;

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
    }

    //==============================================================================================
    // View Model Callbacks
    //==============================================================================================

    @Override
    public void onQueueUp(boolean animate) {
        LogHelper.log("Joined the queue. " + animate);
    }

    @Override
    public void onQueueCancel(boolean animate) {
        LogHelper.log("Left the queue. " + animate);
    }

    @Override
    public void onQueueTick(String secondsInQueue) {
        LogHelper.log("Timer ticked: " + secondsInQueue + ".");
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Join the draft queue!
     */
    @OnClick(R.id.main_play_button) @SuppressWarnings("unused")
    public void main_play() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        // Enter the queue.
        getViewModel(ViewModelMainPlay.class).queueUp();
    }

    /**
     * Leave the draft queue.
     */
    @OnClick(R.id.main_play_cancel) @SuppressWarnings("unused")
    public void main_cancel() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        // Leave the queue.
        getViewModel(ViewModelMainPlay.class).leaveQueue();
    }
}