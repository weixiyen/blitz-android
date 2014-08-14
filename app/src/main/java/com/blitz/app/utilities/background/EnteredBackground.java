package com.blitz.app.utilities.background;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class EnteredBackground {

    // Singleton instance.
    private static EnteredBackground instance = null;

    // Maximum time to transition activities.
    private static final long MAX_TRANSITION_TIME = 1000;

    // Timer and task object.
    private Timer     mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;

    // Track background state.
    private boolean mIsInBackground = true;

    private EnteredBackgroundInterface mEnteredBackgroundInterface;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Initialization method.
     */
    public static void init(EnteredBackgroundInterface enteredBackgroundInterface) {
        instance().mEnteredBackgroundInterface = enteredBackgroundInterface;
    }

    /**
     * When we leave an activity, start a timer.  If
     * timer hits, we know we have entered background.
     */
    public static void startActivityTransitionTimer() {
        instance().clearTimer();

        // Create new timers.
        instance().mActivityTransitionTimer     = new Timer();
        instance().mActivityTransitionTimerTask = new TimerTask() {
            public void run() {

                // Set background flag.
                instance().mIsInBackground = true;

                // Now entered the background.
                instance().mEnteredBackgroundInterface.onEnterBackground();
            }
        };

        // Schedule timer to tick off and detect going into the background.
        instance().mActivityTransitionTimer.schedule(instance().mActivityTransitionTimerTask, MAX_TRANSITION_TIME);
    }

    /**
     * When we enter an activity, cancel any existing
     * timers and see if we came from background.
     */
    public static void stopActivityTransitionTimer() {
        instance().clearTimer();

        if (instance().mIsInBackground) {
            instance().mIsInBackground = false;

            // Now exited the background.
            instance().mEnteredBackgroundInterface.onExitBackground();
        }
    }

    /**
     * Are we currently in the background.
     *
     * @return True or false.
     */
    public static boolean isInBackground() {

        // Return flag.
        return instance().mIsInBackground;
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Fetch singleton instance.
     *
     * @return Singleton instance.
     */
    private static EnteredBackground instance() {

        if (instance == null) {
            synchronized (EnteredBackground.class) {
                if (instance == null) {
                    instance = new EnteredBackground();
                }
            }
        }

        return instance;
    }

    /**
     * Clear any running timer.
     */
    private void clearTimer() {
        if (mActivityTransitionTimerTask != null) {
            mActivityTransitionTimerTask.cancel();

            mActivityTransitionTimerTask = null;
        }

        if (mActivityTransitionTimer != null) {
            mActivityTransitionTimer.cancel();

            mActivityTransitionTimer = null;
        }
    }
}