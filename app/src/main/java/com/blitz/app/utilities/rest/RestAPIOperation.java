package com.blitz.app.utilities.rest;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.dialogs.error.DialogError;
import com.blitz.app.dialogs.loading.DialogLoading;

import java.util.Date;

import retrofit.RetrofitError;
import retrofit.client.Response;

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
    void finish(final RestAPIObject restAPIObject, final RetrofitError retrofitError) {

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

                // If no error, or error result from the REST call.
                if (retrofitError == null) {

                    if (restAPIObject.hasErrors()) {

                        // General failure.
                        failure(null, false);

                    } else {

                        // Successful.
                        success(restAPIObject);
                    }

                } else {

                    // Pass failed response and network error flags.
                    failure(retrofitError.getResponse(), retrofitError.isNetworkError());
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
     * Triggered when a REST operation suffers
     * an error of some kind.
     * @param response Response object (can be null).
     * @param networkError Network error flag.
     */
    public void failure(Response response, boolean networkError) {

        // Fetch the error dialog.
        DialogError dialogError = getDialogError();

        if (dialogError != null) {

            if (networkError) {

                // Network error dialog.
                dialogError.showNetworkError();

            } else if (response != null) {

                // Fetch HTTP status code.
                int httpStatusCode = response.getStatus();

                switch (httpStatusCode) {

                    case 401:

                        // Show unauthorized.
                        dialogError.showUnauthorized();

                        break;
                    default:

                        // Show generic error.
                        getDialogError().show(true);

                        break;
                }
            } else {

                // Show generic error.
                getDialogError().show(true);
            }
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
    protected Date getOperationTimeEnd() {
        return mOperationTimeEnd;
    }

    /**
     * Lazy load the error dialog.
     *
     * @return Error dialog.
     */
    protected DialogError getDialogError() {
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
    protected DialogLoading getDialogLoading() {
        if (mDialogLoading == null && mActivity != null) {
            mDialogLoading = new DialogLoading(mActivity);
        }

        return mDialogLoading;
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
}