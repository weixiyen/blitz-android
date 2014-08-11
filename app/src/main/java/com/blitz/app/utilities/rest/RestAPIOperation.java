package com.blitz.app.utilities.rest;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.dialogs.DialogError;
import com.blitz.app.dialogs.DialogLoading;


/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public abstract class RestAPIOperation {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Parent activity.
    private Activity mActivity;

    // Loading dialog.
    private DialogLoading mDialogLoading;
    private DialogError mDialogError;

    private static boolean mOperationThrottle;
    private static Handler mOperationThrottleHandler;
    private static Runnable mOperationThrottleRunnable;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Empty constructor not allowed (for now).
     */
    @SuppressWarnings("unused")
    private RestAPIOperation() {

    }

    /**
     * Provide an activity so we can create
     * shared loading/error dialogs.
     *
     * @param activity Target activity.
     */
    public RestAPIOperation(Activity activity) {
        mActivity = activity;
    }

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Triggered when a model operation begins.
     */
    public void start() {

        // Setup operation throttling.
        setOperationThrottle();

        // Show loading dialog.
        if (getDialogLoading() != null) {
            getDialogLoading().delayedShow();
        }
    }

    /**
     * Finish helper method that handles
     * finishing the operation in sync with
     * any dialogs/running UI events, etc.
     *
     * @param success Operation status.
     */
    public void finish(final boolean success) {

        finish(success, null);
    }

    /**
     * Finish helper method that handles
     * finishing the operation in sync with
     * any dialogs/running UI events, etc.
     *
     * @param success Operation status.
     * @param httpStatusCode HTTP status code.
     */
    public void finish(final boolean success, final Integer httpStatusCode) {

        // Trigger operation callbacks after hide.
        DialogLoading.HideListener hideListener = new DialogLoading.HideListener() {

            @Override
            public void didHide() {

                if (success) {
                    success();
                } else {
                    failure(httpStatusCode != null &&
                            httpStatusCode == 401);
                }
            }
        };

        // Hide only if dialog present.
        if (getDialogLoading() != null) {
            getDialogLoading().hide(hideListener);
        } else {

            hideListener.didHide();
        }
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Triggered when a model operation is successful.
     */
    public abstract void success();


    /**
     * Triggered when a model operation fails.
     *
     * @param logout Should also log out the user.
     */
    public void failure(boolean logout) {

        // Show the error dialog.
        if (getDialogError() != null) {
            getDialogError().show(true, logout);
        }
    }

    public static boolean shouldThrottle() {

        // In progress if loading or error dialog is on screen.
        return mOperationThrottle;
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Set a throttle that can be checked externally
     * and prevent operation spam.
     */
    private void setOperationThrottle() {

        // Enable the throttle.
        mOperationThrottle = true;

        // Clear any existing callbacks, and init.
        if (mOperationThrottleHandler != null) {
            mOperationThrottleHandler.removeCallbacks(mOperationThrottleRunnable);
        } else {
            mOperationThrottleHandler = new Handler();
        }

        // Initialize the runnable callback.
        if (mOperationThrottleRunnable == null) {
            mOperationThrottleRunnable = new Runnable() {

                @Override
                public void run() {

                    // No longer throttling.
                    mOperationThrottle = false;
                }
            };
        }

        // Set the de-throttle callback.
        mOperationThrottleHandler.postDelayed(mOperationThrottleRunnable, 250);
    }

    /**
     * Lazy load the error dialog.
     *
     * @return Error dialog.
     */
    private DialogError getDialogError() {
        if (mDialogError == null && mActivity != null) {
            mDialogError = new DialogError(mActivity);
        }

        return mDialogError;
    }

    /**
     * Lazy load the loading dialog.
     *
     * @return Loading dialog.
     */
    private DialogLoading getDialogLoading() {
        if (mDialogLoading == null && mActivity != null) {
            mDialogLoading = new DialogLoading(mActivity);
        }

        return mDialogLoading;
    }
}