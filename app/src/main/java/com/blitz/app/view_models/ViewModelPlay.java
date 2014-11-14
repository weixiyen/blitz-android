package com.blitz.app.view_models;

import android.os.Bundle;
import android.os.Handler;

import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelItem;
import com.blitz.app.rest_models.RestModelPreferences;
import com.blitz.app.rest_models.RestModelQueue;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.screens.play.PlayScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.blitz.BlitzDelay;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.google.gson.JsonObject;

/**
 * Created by Miguel on 7/26/2014. Copyright 2014 Blitz Studios
 */
public class ViewModelPlay extends ViewModel {

    // region Member Variables
    // ============================================================================================================

    // Queue timer variables.
    private int      mSecondsAtSuspension;
    private int      mSecondsInQueue = -1;
    private Handler  mSecondsInQueueHandler;

    // Object model.
    private RestModelQueue mModelQueue = new RestModelQueue();

    // Are we in queue.
    private boolean mInQueue;

    // Is the queue available.
    private boolean mQueueAvailable;

    // State identifiers.
    private static final String STATE_SECONDS = "stateSeconds";
    private static final String STATE_TIME_SUSPENDED = "timeSuspected";
    private static final String STATE_IN_QUEUE = "stateInQueue";

    // Current user avatar.
    private String mUserAvatarId;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelPlay(BaseActivity activity, ViewModel.Callbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

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
     */
    @Override
    public void saveInstanceState(Bundle savedInstanceState) {
        super.saveInstanceState(savedInstanceState);

        // Set seconds at suspension.
        mSecondsAtSuspension = (int) (System.currentTimeMillis() / 1000);

        // Save info needed to restore state.
        savedInstanceState.putInt(STATE_TIME_SUSPENDED, mSecondsAtSuspension);
        savedInstanceState.putInt(STATE_SECONDS, mSecondsInQueue);
        savedInstanceState.putBoolean(STATE_IN_QUEUE, mInQueue);

        // Stop timer.
        stopQueueTimer(false);
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

        // Fetch user info.
        fetchUserInfo();

        // Initialize container state.
        showQueueContainer(null, false);

        // Setup comet.
        setupCometCallbacks();

        // Setup preferences.
        setupPreferences();
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Toggle the draft queue state (either join
     * or leave it).
     */
    public void toggleQueue() {

        if (mInQueue) {

            // Leave the queue.
            mModelQueue.leaveQueue(mActivity, () -> { });

        } else {

            // Enter the queue.
            mModelQueue.queueUp(mActivity, () -> { });
        }

        showQueueContainer(!mInQueue, true);
    }

    /**
     * Fetch and broadcast user information relevant
     * to the play view model.
     */
    public void fetchUserInfo() {

        // Fetch current user id.
        final String userId = AuthHelper.instance().getUserId();

        // Fetch the current user.
        RestModelUser.getUser(mActivity, userId, new RestModelCallback<RestModelUser>() {

            @Override
            public void onSuccess(RestModelUser user) {

                // Fetch callbacks.
                final Callbacks callbacks = getCallbacks(Callbacks.class);

                if (callbacks != null) {
                    callbacks.onUsername(user.getUsername());
                    callbacks.onRating(user.getRating());
                    callbacks.onWins(user.getWins());
                    callbacks.onLosses(user.getLosses());
                    callbacks.onCash(user.getCash());
                }

                // If a new avatar id is found.
                if (!user.getAvatarId().equals(mUserAvatarId)) {

                    // Assign it.
                    mUserAvatarId = user.getAvatarId();

                    // Update helmet.
                    updateHelmet();
                }
            }
        }, true);
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Attempt to update the user helmet.
     */
    private void updateHelmet() {

        // Fetch associated item model.
        RestModelItem.fetchItem(mActivity, mUserAvatarId,
                item -> {

                    if (getCallbacks(Callbacks.class) != null) {
                        getCallbacks(Callbacks.class).onAvatarUrl(item.getDefaultImgPath());
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

        // Only fire if state changes.
        if (showQueueContainer == mInQueue) {

            return;
        }

        if (showQueueContainer) {

            mInQueue = true;

            // Now in the queue.
            getCallbacks(Callbacks.class).onQueueUp(animate);

            // Start timer.
            startQueueTimer();

        } else {

            mInQueue = false;

            // Now left the queue.
            getCallbacks(Callbacks.class).onQueueCancel(animate);

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
                .addCallback(PlayScreen.class, (PlayScreen receivingClass, JsonObject message) ->
                        ((ViewModelPlay)receivingClass.onFetchViewModel())
                                .handleDraftAction(receivingClass, message), "draftUserCallback");
    }

    /**
     * Handle a draft callback action.
     *
     * @param receivingClass Instance of this activity.
     * @param message Json message sent.
     */
    private void handleDraftAction(PlayScreen receivingClass, JsonObject message) {

        // Fetch sent action.
        String action = message.get("action").getAsString();

        // If left the queue or entered the draft.
        if (action.equals("left_queue") || action.equals("enter_draft")) {

            ((ViewModelPlay)receivingClass.onFetchViewModel())
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

        // Start callback on a loop.
        mSecondsInQueueHandler = BlitzDelay.postDelayed(() -> {

            mSecondsInQueue++;

            // Queue timer has ticked.
            getCallbacks(Callbacks.class).onQueueTick(String.format
                    ("%02d:%02d", mSecondsInQueue / 100, mSecondsInQueue % 100));
        }, 1000, true, true);
    }

    /**
     * Stop timer and reset seconds
     * in the queue.
     */
    private void stopQueueTimer(boolean resetSecondsInQueue) {

        // Stop callback loop.
        BlitzDelay.remove(mSecondsInQueueHandler);

        if (resetSecondsInQueue) {

            // Reset seconds.
            mSecondsInQueue = -1;
        }
    }

    /**
     * Setup preferences and update UI based on
     * various draft and queue available states.
     */
    private void setupPreferences() {

        AuthHelper.instance().getPreferences(mActivity, false,
                new RestModelCallback<RestModelPreferences>() {

                    @Override
                    public void onSuccess(RestModelPreferences object) {

                        // Get the queue availability.
                        mQueueAvailable = object.getIsQueueAvailable();

                        if (getCallbacks(Callbacks.class) != null) {
                            getCallbacks(Callbacks.class).onQueueAvailable(mQueueAvailable);
                        }
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();

                        // Queue is not available.
                        mQueueAvailable = false;

                        if (getCallbacks(Callbacks.class) != null) {
                            getCallbacks(Callbacks.class).onQueueAvailable(mQueueAvailable);
                        }
                    }
                });
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onQueueAvailable(boolean queueAvailable);
        public void onQueueUp(boolean animate);
        public void onQueueCancel(boolean animate);
        public void onQueueTick(String secondsInQueue);
        public void onUsername(String username);
        public void onRating(int rating);
        public void onWins(int wins);
        public void onLosses(int losses);
        public void onCash(int cash);
        public void onAvatarUrl(String avatarUrl);
    }

    // endregion
}