package com.blitz.app.screens.main;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.models.objects.ObjectModelPlay;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.logging.LogHelper;

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

    // View model.
    private ObjectModelPlay mModelPlay;

    // Queue timer variables.
    private int      mSecondsInQueue;
    private Handler  mSecondsInQueueHandler;
    private Runnable mSecondsInQueueRunnable;

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Start the queue timer and increment
     * it every second.
     */
    private void startQueueTimer() {

        if (mSecondsInQueueHandler ==  null) {
            mSecondsInQueueHandler = new Handler();
        }

        if (mSecondsInQueueRunnable == null) {
            mSecondsInQueueRunnable = new Runnable() {

                @Override
                public void run() {

                    mSecondsInQueue++;
                    mSecondsInQueueHandler.postDelayed(mSecondsInQueueRunnable, 1000);

                    // Update the timer text view.
                    mQueuedTimerTextView.setText(String.format("%02d:%02d",
                            mSecondsInQueue / 100, mSecondsInQueue % 100));
                }
            };
        }

        // Start the timer.
        mSecondsInQueueHandler.post(mSecondsInQueueRunnable);
        mSecondsInQueue = -1;
    }

    /**
     * Stop timer and reset seconds
     * in the queue.
     */
    private void stopQueueTimer() {

        if (mSecondsInQueueHandler  != null &&
            mSecondsInQueueRunnable != null) {

            // Stop the timer.
            mSecondsInQueueHandler.removeCallbacks(mSecondsInQueueRunnable);
        }
    }

    /**
     * Show a container with animation.
     *
     * @param containerFrom Animating from.
     * @param containerTo Animating to.
     */
    private void showContainer(final View containerFrom, final View containerTo) {

        // First fade out the from container.
        containerFrom.animate().alpha(0.0f).setDuration(250).withEndAction(new Runnable() {

            @Override
            public void run() {

                // Then remove it from the layout.
                containerFrom.setVisibility(View.GONE);

                // Then fade in the to container.
                containerTo.setVisibility(View.VISIBLE);
                containerTo.setAlpha(0.0f);
                containerTo.animate().alpha(1.0f).setDuration(250);
            }
        });
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.main_featured_play) @SuppressWarnings("unused")
    public void main_featured_play() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        if (mModelPlay == null) {
            mModelPlay = new ObjectModelPlay();
        }

        // Enter the queue.
        mModelPlay.enterQueue(getActivity(), new ObjectModelPlay.EnterQueueCallback() {

            @Override
            public void onEnterQueue() {

                // Show the queue UI.
                showContainer(mTimelineContainer, mQueuedContainer);

                // Start timer.
                startQueueTimer();
            }
        });
    }

    @OnClick(R.id.main_featured_cancel) @SuppressWarnings("unused")
    public void main_featured_cancel() {

        // Hide the timeline UI.
        showContainer(mQueuedContainer, mTimelineContainer);

        // Stop timer.
        stopQueueTimer();

        LogHelper.log("TODO: Send the cancel call.");
    }
}