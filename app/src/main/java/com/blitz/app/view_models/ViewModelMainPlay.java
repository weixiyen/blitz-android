package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.blitz.app.object_models.ObjectModelItem;
import com.blitz.app.object_models.ObjectModelQueue;
import com.blitz.app.object_models.ObjectModelUser;
import com.blitz.app.screens.main.MainScreenFragmentPlay;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.comet.CometAPICallback;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.google.gson.JsonObject;

/**
 * Created by Miguel on 7/26/2014. Copyright 2014 Blitz Studios
 */
public class ViewModelMainPlay extends ViewModel {

    // region Member Variables
    // =============================================================================================

    // Queue timer variables.
    private int      mSecondsAtSuspension;
    private int      mSecondsInQueue = -1;
    private Handler  mSecondsInQueueHandler;
    private Runnable mSecondsInQueueRunnable;

    // Object model.
    private ObjectModelQueue mModelQueue = new ObjectModelQueue();
    private ObjectModelUser mModelUser = new ObjectModelUser();

    // Are we in queue.
    private boolean mInQueue;

    private static final String STATE_SECONDS = "stateSeconds";
    private static final String STATE_TIME_SUSPENDED = "timeSuspected";
    private static final String STATE_IN_QUEUE = "stateInQueue";

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelMainPlay(Activity activity, ViewModelCallbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

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
            mInQueue = savedInstanceState.getBoolean(STATE_IN_QUEUE);
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
        savedInstanceState.putBoolean(STATE_IN_QUEUE, mInQueue);

        // Stop timer.
        stopQueueTimer(false);

        return savedInstanceState;
    }

    /**
     * Initialize the view model.
     */
    @Override
    public void initialize() {

        if (mSecondsInQueue != -1) {

            // Get seconds since state saved.
            int secondsSinceSinceSaved = (int)
                    ((System.currentTimeMillis() / 1000) - mSecondsAtSuspension);

            mSecondsInQueue += secondsSinceSinceSaved;
        }

        // Initialize container state.
        showQueueContainer(null, false);

        // Fetch user info.
        fetchUserInfo();

        // Setup comet.
        setupCometCallbacks();
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Toggle the draft queue state (either join
     * or leave it).
     */
    public void toggleQueue() {

        if (mInQueue) {

            // Leave the queue.
            mModelQueue.leaveQueue(mActivity, null);

        } else {

            // Enter the queue.
            mModelQueue.queueUp(mActivity, new Runnable() {

                @Override
                public void run() {

                    showQueueContainer(true, true);
                }
            });
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Fetch and broadcast user information relevant
     * to the play view model.
     */
    private void fetchUserInfo() {

        // Fetch current user id.
        String userId = AuthHelper.instance().getUserId();

        // Fetch the current user.
        ObjectModelUser.getUser(mActivity, userId, new ObjectModelUser.CallbackUser() {

            @Override
            public void onSuccess(ObjectModelUser user) {

                // Fetch callbacks.
                final ViewModelMainPlayCallbacks callbacks =
                        getCallbacks(ViewModelMainPlayCallbacks.class);

                if (callbacks != null) {
                    callbacks.onUsername(mModelUser.getUsername());
                    callbacks.onRating  (mModelUser.getRating());
                    callbacks.onWins    (mModelUser.getWins());
                    callbacks.onLosses  (mModelUser.getLosses());
                    callbacks.onCash    (mModelUser.getCash());
                }

                // Fetch associated item model.
                ObjectModelItem.get(mActivity, mModelUser.getAvatarId(),
                        new ObjectModelItem.ItemCallback() {

                            @Override
                            public void onSuccess(ObjectModelItem item) {

                                if (callbacks != null) {
                                    callbacks.onImgPath(item.getDefaultImgPath());
                                }
                            }
                        });
            }
        });
    }

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

            mInQueue = true;

            // Now in the queue.
            getCallbacks(ViewModelMainPlayCallbacks.class).onQueueUp(animate);

            // Start timer.
            startQueueTimer();

        } else {

            mInQueue = false;

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
        String userCometChannel = "user:" + AppDataObject.userId.get();

        // Subscribe to channel, set callback.
        CometAPIManager

                // Subscribe to user channel.
                .subscribeToChannel(userCometChannel)

                        // Set callback action.
                .addCallback(MainScreenFragmentPlay.class, new CometAPICallback<MainScreenFragmentPlay>() {

                    @Override
                    public void messageReceived(MainScreenFragmentPlay receivingClass, JsonObject message) {

                        // Handle the action.
                        ((ViewModelMainPlay)receivingClass.onFetchViewModel())
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

            ((ViewModelMainPlay)receivingClass.onFetchViewModel())
                    .showQueueContainer(false, true);
        }
    }

    /**
     * Start the queue timer and increment
     * it every second.
     */
    private void startQueueTimer() {

        // Stop and reset.
        stopQueueTimer(true);

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

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface ViewModelMainPlayCallbacks extends ViewModelCallbacks {

        public void onQueueUp(boolean animate);
        public void onQueueCancel(boolean animate);
        public void onQueueTick(String secondsInQueue);
        public void onUsername(String username);
        public void onRating(int rating);
        public void onWins(int wins);
        public void onLosses(int losses);
        public void onCash(int cash);
        public void onImgPath(String imgPath);
    }

    // endregion
}