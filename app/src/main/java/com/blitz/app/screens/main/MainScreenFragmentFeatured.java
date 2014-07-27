package com.blitz.app.screens.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.models.comet.CometAPICallback;
import com.blitz.app.models.comet.CometAPIManager;
import com.blitz.app.models.objects.ObjectModelQueue;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.app.AppDataObject;
import com.google.gson.JsonObject;

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
    private ObjectModelQueue mModelQueue;

    // Queue timer variables.
    private int      mSecondsInQueue;
    private Handler  mSecondsInQueueHandler;
    private Runnable mSecondsInQueueRunnable;

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

        // Lazy load the model.
        if (mModelQueue == null) {
            mModelQueue = new ObjectModelQueue();
        }

        // Setup comet.
        setupCometCallbacks();
    }

    /**
     * Cleanup on fragment pause.
     */
    @Override
    public void onPause () {
        super.onPause();

        // Stop timer if running.
        stopQueueTimer();
    }

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

    /**
     * Listen on user channel for callbacks
     * related to joining the game.
     */
    private void setupCometCallbacks() {

        // Fetch comet channel for this user.
        String userCometChannel = "user:" + AppDataObject.userId.getString();

        // Subscribe to channel, set callback.
        CometAPIManager

                // Subscribe to user channel.
                .subscribeToChannel(userCometChannel)

                // Set callback action.
                .addCallback(this, new CometAPICallback<MainScreenFragmentFeatured>() {

                    @Override
                    public void messageReceived(MainScreenFragmentFeatured receivingClass, JsonObject message) {

                        // Handle the action.
                        receivingClass.handleDraftAction(receivingClass, message);
                    }
                }, "draftUserCallback");
    }

    /**
     * Handle a draft callback action.
     *
     * @param receivingClass Instance of this activity.
     * @param message Json message sent.
     */
    private void handleDraftAction(MainScreenFragmentFeatured receivingClass, JsonObject message) {

        // Fetch sent action.
        String action = message.get("action").getAsString();

        // If left the queue.
        if (action.equals("left_queue")) {

            // Hide the timeline UI.
            receivingClass.showContainer(mQueuedContainer, mTimelineContainer);

            // Stop timer.
            receivingClass.stopQueueTimer();
        }
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.main_featured_play) @SuppressWarnings("unused")
    public void main_featured_play() {

        if (RestAPIOperation.shouldThrottle()) { return; }

        // Enter the queue.
        mModelQueue.queueUp(getActivity(), new ObjectModelQueue.QueueUpCallback() {

            @Override
            public void onQueueUp() {

                // Show the queue UI.
                showContainer(mTimelineContainer, mQueuedContainer);

                // Start timer.
                startQueueTimer();
            }
        });
    }

    @OnClick(R.id.main_featured_cancel) @SuppressWarnings("unused")
    public void main_featured_cancel() {

        if (RestAPIOperation.shouldThrottle()) { return; }

        // Leave the queue.
        mModelQueue.leaveQueue(getActivity(), null);
    }
}