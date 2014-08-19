package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.blitz.app.object_models.ObjectModelQueue;
import com.blitz.app.screens.main.MainScreenFragmentPlay;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.comet.CometAPICallback;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.google.gson.JsonObject;

/**
 * Created by Miguel on 7/26/2014.
 */
public class ViewModelMainPlay extends ViewModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Queue timer variables.
    private int      mSecondsAtSuspension;
    private int      mSecondsInQueue = -1;
    private Handler  mSecondsInQueueHandler;
    private Runnable mSecondsInQueueRunnable;

    // Object model.
    private ObjectModelQueue mModelQueue;

    static final String STATE_SECONDS = "stateSeconds";
    static final String STATE_TIME_SUSPENDED = "timeSuspected";

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Restore model variables and re-initialize
     * the UI for the screen.
     *
     * @param savedInstanceState Contains state information.
     */
    @Override
    public void restoreInstanceState(Bundle savedInstanceState) {
        super.restoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {

            // Restore state primitives.
            mSecondsInQueue = savedInstanceState.getInt(STATE_SECONDS);
            mSecondsAtSuspension = savedInstanceState.getInt(STATE_TIME_SUSPENDED);
        }
    }

    /**
     * Clean up the UI and persist model state.
     *
     * @param savedInstanceState State information bundle.
     *
     * @return Bundle with added state.
     */
    @Override
    public Bundle saveInstanceState(Bundle savedInstanceState) {
        super.saveInstanceState(savedInstanceState);

        // Set seconds at suspension.
        mSecondsAtSuspension = (int) (System.currentTimeMillis() / 1000);

        // Save info needed to restore state.
        savedInstanceState.putInt(STATE_TIME_SUSPENDED, mSecondsAtSuspension);
        savedInstanceState.putInt(STATE_SECONDS, mSecondsInQueue);

        // Stop timer.
        stopQueueTimer(false);

        return savedInstanceState;
    }

    /**
     * Initialize the view model.
     */
    @Override
    public void initialize(Activity activity, ViewModelCallbacks callbacks) {
        super.initialize(activity, callbacks);

        if (mSecondsInQueue != -1) {

            // Get seconds since state saved.
            int secondsSinceSinceSaved = (int)
                    ((System.currentTimeMillis() / 1000) - mSecondsAtSuspension);

            mSecondsInQueue += secondsSinceSinceSaved;
        }

        // Initialize container state.
        showQueueContainer(null, false);

        // Setup comet.
        setupCometCallbacks();
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Join the draft queue.
     */
    public void queueUp() {

        // Enter the queue.
        getModelQueue().queueUp(mActivity, new Runnable() {

            @Override
            public void run() {

                showQueueContainer(true, true);
            }
        });
    }

    /**
     * Leave the draft queue.
     */
    public void leaveQueue() {

        // Leave the queue.
        getModelQueue().leaveQueue(mActivity, null);
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Show the queue container to either hidden or
     * visible.
     *
     * @param showQueueContainer Boolean toggle, if null uses internal
     *                           state synchronize UI with that state.
     */
    private void showQueueContainer(Boolean showQueueContainer, boolean animate) {

        if (showQueueContainer == null) {
            showQueueContainer = mSecondsInQueue != -1;
        }

        if (showQueueContainer) {

            // Now in the queue.
            getCallbacks(ViewModelMainPlayCallbacks.class).onQueueUp(animate);

            // Start timer.
            startQueueTimer();

        } else {

            // Now left the queue.
            getCallbacks(ViewModelMainPlayCallbacks.class).onQueueCancel(animate);

            // Stop timer.
            stopQueueTimer(true);
        }
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
                .addCallback(MainScreenFragmentPlay.class, new CometAPICallback<MainScreenFragmentPlay>() {

                    @Override
                    public void messageReceived(MainScreenFragmentPlay receivingClass, JsonObject message) {

                        // Handle the action.
                        receivingClass.getViewModel(ViewModelMainPlay.class)
                                .handleDraftAction(receivingClass, message);
                    }
                }, "draftUserCallback");
    }

    /**
     * Handle a draft callback action.
     *
     * @param receivingClass Instance of this activity.
     * @param message Json message sent.
     */
    private void handleDraftAction(MainScreenFragmentPlay receivingClass, JsonObject message) {

        // Fetch sent action.
        String action = message.get("action").getAsString();

        // If left the queue or entered the draft.
        if (action.equals("left_queue") || action.equals("enter_draft")) {

            receivingClass.getViewModel(ViewModelMainPlay.class)
                    .showQueueContainer(false, true);
        }
    }

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

                    // Queue timer has ticked.
                    getCallbacks(ViewModelMainPlayCallbacks.class).onQueueTick(
                            String.format("%02d:%02d", mSecondsInQueue / 100, mSecondsInQueue % 100));
                }
            };
        }

        // Start the timer.
        mSecondsInQueueHandler.post(mSecondsInQueueRunnable);
    }

    /**
     * Stop timer and reset seconds
     * in the queue.
     */
    private void stopQueueTimer(boolean resetSecondsInQueue) {

        if (mSecondsInQueueHandler  != null &&
            mSecondsInQueueRunnable != null) {

            // Stop the timer.
            mSecondsInQueueHandler.removeCallbacks(mSecondsInQueueRunnable);
        }

        if (resetSecondsInQueue) {

            // Reset seconds.
            mSecondsInQueue = -1;
        }
    }

    /**
     * Fetch queue model instance.
     *
     * @return Queue model instance.
     */
    private ObjectModelQueue getModelQueue() {

        // Lazy load the model.
        if (mModelQueue == null) {
            mModelQueue = new ObjectModelQueue();
        }

        return mModelQueue;
    }

    //==============================================================================================
    // Callbacks Interface
    //==============================================================================================

    public interface ViewModelMainPlayCallbacks extends ViewModelCallbacks {

        public void onQueueUp(boolean animate);
        public void onQueueCancel(boolean animate);
        public void onQueueTick(String secondsInQueue);
    }
}