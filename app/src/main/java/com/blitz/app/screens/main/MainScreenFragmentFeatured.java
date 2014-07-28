package com.blitz.app.screens.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.views.ViewModelMainFeatured;
import com.blitz.app.utilities.android.BaseFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreenFragmentFeatured extends BaseFragment {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Container views.
    @InjectView(R.id.main_featured_timeline_container) View mTimelineContainer;
    @InjectView(R.id.main_featured_queued_container) View mQueuedContainer;

    // Queue timer.
    @InjectView(R.id.main_featured_queued_timer) TextView mQueuedTimerTextView;

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
        setViewModel(new ViewModelMainFeatured(), savedInstanceState);

        // Set the views.
        getViewModel(ViewModelMainFeatured.class)
                .setViews(mTimelineContainer, mQueuedContainer, mQueuedTimerTextView);
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Join the draft queue!
     */
    @OnClick(R.id.main_featured_play) @SuppressWarnings("unused")
    public void main_featured_play() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        // Enter the queue.
        getViewModel(ViewModelMainFeatured.class).queueUp();
    }

    /**
     * Leave the draft queue.
     */
    @OnClick(R.id.main_featured_cancel) @SuppressWarnings("unused")
    public void main_featured_cancel() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        // Leave the queue.
        getViewModel(ViewModelMainFeatured.class).leaveQueue();
    }
}