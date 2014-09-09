package com.blitz.app.utilities.rest;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.dialogs.error.DialogError;
import com.blitz.app.dialogs.loading.DialogLoading;

import java.util.Date;

/**
 * Created by Miguel Gaeta on 6/29/14. Copyright 2014 Blitz Studios
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

    // Operation time.
    private long mOperationTimeMilliseconds;
    private Date mOperationTimeStart;
    private Date mOperationTimeEnd;

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
    // Package Methods
    //==============================================================================================

    /**
     * Triggered when a model operation begins.
     */
    void start() {

        // Set the start time.
        mOperationTimeStart = new Date();

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
     * @param restAPIObject Resulting rest object.
     */
    void finish(RestAPIObject restAPIObject) {

        // Operation is finished, pass in success/fail boolean.
        finish(restAPIObject, !restAPIObject.hasErrors(), null);
    }

    /**
     * Finish helper method that handles
     * finishing the operation in sync with
     * any dialogs/running UI events, etc.
     *
     * @param httpStatusCode HTTP status code.
     * @param success Operation success.
     * @param restAPIObject Resulting rest object.
     */
    void finish(final RestAPIObject restAPIObject, final boolean success, final Integer httpStatusCode) {

        // Set the end time.
        mOperationTimeEnd = new Date();

        // Calculate operation time.
        mOperationTimeMilliseconds =
                mOperationTimeEnd.getTime() -
                        mOperationTimeStart.getTime();

        // Trigger operation callbacks after hide.
        DialogLoading.HideListener hideListener = new DialogLoading.HideListener() {

            @Override
            public void didHide() {

                if (success) {
                    success(restAPIObject);
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
     *
     * @param restAPIObject Populated rest api.
     */
    public abstract void success(RestAPIObject restAPIObject);

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
    // Protected Methods
    //==============================================================================================

    /**
     * Fetch how long it took to
     * run this operation.
     *
     * @return Time in milliseconds.
     */
    @SuppressWarnings("unused")
    protected Long getOperationTime() {
        return mOperationTimeMilliseconds;
    }

    @SuppressWarnings("unused")
    protected Date getOperationTimeStart() {
        return mOperationTimeStart;
    }

    @SuppressWarnings("unused")
    protected Date getmOperationTimeEnd() {
        return mOperationTimeEnd;
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Set a throttle that can be checked externally
     * and prevent operation spam.
     */
    private void setOperationThrottle() {

        // If this operation does not have an activity (and thus no
        // dialog support) there is no need to throttle it.
        if (mActivity == null) {

            return;
        }

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