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

    // Associated view model.
    private ViewModelMainFeatured mViewModel;

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
        mViewModel = new ViewModelMainFeatured();
        mViewModel.setActivity(getActivity());
        mViewModel.setViews(mTimelineContainer, mQueuedContainer, mQueuedTimerTextView);

        // Restore state.
        mViewModel.restoreInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Initialize view model.
        mViewModel.initialize();
    }

    /**
     * Save this screen fragments state.
     *
     * @param outState Outbound state bundle.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save state.
        mViewModel.saveInstanceState(outState);
    }

    /**
     * Fetch the view model.
     *
     * @return View model.
     */
    public ViewModelMainFeatured getViewModel() {
        return mViewModel;
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
        mViewModel.queueUp();
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
        mViewModel.leaveQueue();
    }
}