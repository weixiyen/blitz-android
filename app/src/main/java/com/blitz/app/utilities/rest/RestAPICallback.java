package com.blitz.app.utilities.rest;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.dialogs.error.DialogError;
import com.blitz.app.dialogs.loading.DialogLoading;
import com.blitz.app.utilities.date.DateUtils;

import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/29/14. Copyright 2014 Blitz Studios
 */
public abstract class RestAPICallback<T> implements Callback<T> {

    // region Member Variables
    // =============================================================================================

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

    // Is this an auth call.
    private boolean mIsAuthentication;

    // endregion

    // region Abstract Methods
    // =============================================================================================

    public abstract void success(T jsonObject);

    // endregion

    // region Constructors
    // =============================================================================================

    /**
     * Private constructor disallowed.  Encourages
     * usage of activity version.
     */
    @SuppressWarnings("unused")
    private RestAPICallback() {

    }

    /**
     * Create callback class.
     *
     * @param activity Activity used for dialogs, can be null.
     */
    @SuppressWarnings("unused")
    public RestAPICallback(Activity activity) {

        // Set the activity.
        mActivity = activity;

        // Set the start time.
        mOperationTimeStart = DateUtils.getDateInGMT();

        // Setup operation throttling.
        setOperationThrottle();

        // Show loading dialog.
        if (getLoadingDialog() != null) {
            getLoadingDialog().delayedShow();
        }
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Successful REST call is made.  Prefer to NOT
     * override this method as it is a retrofit
     * method.
     *
     * Success may not really be indicated at this point
     * as the result returned from the server could
     * also contain errors.
     *
     * @param jsonObject Resulting JSON parsed object.
     * @param response Response.
     */
    @Override
    public void success(T jsonObject, Response response) {

        if (mIsAuthentication) {

            // Set user cookies if they exist.
            RestAPIClient.trySetUserCookies(response);
        }

        // Finish the operation.
        finish(jsonObject, null);
    }

    /**
     * Unsuccessful REST call is made.  Prefer to NOT
     * override this method, instead override the
     * failure method that does not expose retrofit
     * and provides a better parsed reason for failure.
     *
     * The other failure method can also be called
     * due to errors not originating from retrofit
     * (json parse errors, server result errors,
     * which makes it preferable.
     *
     * @param retrofitError Retrofit error object.
     */
    @Override
    public void failure(RetrofitError retrofitError) {

        // Finish with retrofit error.
        finish(null, retrofitError);
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Should this operation be throttled.
     *
     * @return Operation throttle flag.
     */
    @SuppressWarnings("unused")
    public static boolean shouldThrottle() {

        // In progress if loading or error dialog is on screen.
        return mOperationThrottle;
    }

    /**
     * Is this an authentication callback.
     *
     * @param isAuthentication Is this an authentication call.
     */
    @SuppressWarnings("unused")
    public void setIsAuthentication(boolean isAuthentication) {

        mIsAuthentication = isAuthentication;
    }

    /**
     * Triggered when a REST operation suffers
     * an error of some kind.
     *
     * @param response Response object (can be null).
     * @param networkError Network error flag.
     */
    @SuppressWarnings("unused")
    public void failure(Response response, boolean networkError) {

        // Fetch the error dialog.
        DialogError dialogError = getErrorDialog();

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
                        getErrorDialog().show(true);

                        break;
                }
            } else {

                // Show generic error.
                getErrorDialog().show(true);
            }
        }
    }

    // endregion

    // region Protected Methods
    // =============================================================================================

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

    /**
     * Fetch time the operation started.
     *
     * @return Operation start time.
     */
    @SuppressWarnings("unused")
    protected Date getOperationTimeStart() {

        return mOperationTimeStart;
    }

    /**
     * Fetch time operation ended.
     *
     * @return Operation end time.
     */
    @SuppressWarnings("unused")
    protected Date getOperationTimeEnd() {

        return mOperationTimeEnd;
    }

    /**
     * Lazy load the error dialog.
     *
     * @return Error dialog.
     */
    @SuppressWarnings("unused")
    protected DialogError getErrorDialog() {

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
    @SuppressWarnings("unused")
    protected DialogLoading getLoadingDialog() {

        if (mDialogLoading == null && mActivity != null) {
            mDialogLoading = new DialogLoading(mActivity);
        }

        return mDialogLoading;
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Finish helper method that handles
     * finishing the operation in sync with
     * any dialogs/running UI events, etc.
     *
     * @param jsonObject Resulting JSON parsed object. object.
     */
    private void finish(final T jsonObject, final RetrofitError retrofitError) {

        // Set the end time.
        mOperationTimeEnd = DateUtils.getDateInGMT();

        // Calculate operation time.
        mOperationTimeMilliseconds = mOperationTimeEnd.getTime() - mOperationTimeStart.getTime();

        // Trigger operation callbacks after hide.
        DialogLoading.HideListener hideListener = new DialogLoading.HideListener() {

            @Override
            public void didHide() {

                // If no error, or error result from the REST call.
                if (retrofitError == null) {

                    // Successful.
                    success(jsonObject);

                } else {

                    // Pass failed response and network error flags.
                    failure(retrofitError.getResponse(), retrofitError.isNetworkError());
                }
            }
        };

        // Hide only if dialog present.
        if (getLoadingDialog() != null) {
            getLoadingDialog().hide(hideListener);

        } else {

            hideListener.didHide();
        }
    }

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

    // endregion
}