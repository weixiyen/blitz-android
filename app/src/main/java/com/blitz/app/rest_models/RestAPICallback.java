package com.blitz.app.rest_models;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.dialogs.error.DialogErrorSingleton;
import com.blitz.app.dialogs.loading.DialogLoading;
import com.blitz.app.utilities.blitz.BlitzDelay;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/29/14. Copyright 2014 Blitz Studios
 */
class RestAPICallback<T> implements Callback<T> {

    // region Member Variables
    // ============================================================================================================

    // Parent activity.
    private Activity mActivity;

    // Loading dialog.
    private DialogLoading mDialogLoading;

    // Throttle rest calls.
    private static boolean mOperationThrottle;
    private static Handler mOperationThrottleHandler;

    // Is this an authentication call.
    private boolean mIsAuthentication;

    // Should log out on failure.
    private boolean mLogoutOnFailure;

    // Callback interfaces.
    private OnSuccess<T> mOnSuccess;
    private OnFailure mOnFailure;

    // endregion

    // region Constructors
    // ============================================================================================================

    /**
     * Private constructor disallowed.  Encourages
     * usage of activity version.
     */
    @SuppressWarnings("unused")
    private RestAPICallback() {

    }

    /**
     * Creates a rest API callback object (wrapper around retrofit).
     *
     * @param activity Activity which is used for dialogs - TODO: Remove it.
     *
     * @param onSuccess On success interface.
     * @param onFailure On failure interface.
     */
    @SuppressWarnings("unused")
    public RestAPICallback(Activity activity, OnSuccess<T> onSuccess, OnFailure onFailure) {

        // Set the activity.
        mActivity = activity;

        // Set callbacks.
        mOnSuccess = onSuccess;
        mOnFailure = onFailure;

        // Setup operation throttling.
        setOperationThrottle();

        // Show loading dialog.
        if (getLoadingDialog() != null) {
            getLoadingDialog().delayedShow();
        }
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

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
    // ============================================================================================================

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
     * Should we logout on general failure.
     *
     * @param logoutOnFailure Logout on failure.
     */
    @SuppressWarnings("unused")
    public void setLogoutOnFailure(boolean logoutOnFailure) {

        mLogoutOnFailure = logoutOnFailure;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Finish helper method that handles
     * finishing the operation in sync with
     * any dialogs/running UI events, etc.
     *
     * @param jsonObject Resulting JSON parsed object. object.
     */
    private void finish(final T jsonObject, final RetrofitError retrofitError) {

        // Trigger operation callbacks after hide.
        DialogLoading.HideListener hideListener = () -> {

            // If no error, or error result from the REST call.
            if (retrofitError == null) {

                // Check for generic server error in the json object.
                if (jsonObject != null &&
                    jsonObject instanceof RestAPIResult
                        && ((RestAPIResult) jsonObject).hasErrors()) {

                    // Generic failure.
                    failure(null, false);

                } else if (mOnSuccess != null) {

                    // Trigger interface.
                    mOnSuccess.onSuccess(jsonObject);
                }

            } else {

                // Pass failed response and network error flags.
                failure(retrofitError.getResponse(), retrofitError.isNetworkError());
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
     * Triggered when a REST operation suffers
     * an error of some kind.
     *
     * @param response Response object (can be null).
     * @param networkError Network error flag.
     */
    private void failure(Response response, boolean networkError) {

        if (mActivity != null) {

            if (mLogoutOnFailure) {

                // Not authorized bro.
                DialogErrorSingleton.showUnauthorized();

            } else  if (networkError) {

                // Network error dialog.
                DialogErrorSingleton.showNetwork();

            } else if (response != null) {

                // Fetch HTTP status code.
                int httpStatusCode = response.getStatus();

                switch (httpStatusCode) {

                    case 401:

                        // Not authorized bro.
                        DialogErrorSingleton.showUnauthorized();

                        break;
                    default:

                        // Show generic error.
                        DialogErrorSingleton.showGeneric();

                        break;
                }
            } else {

                // Show generic error.
                DialogErrorSingleton.showGeneric();
            }
        }

        if (mOnFailure != null) {
            mOnFailure.onFailure(response, networkError);
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

        // Clear any existing callbacks.
        BlitzDelay.remove(mOperationThrottleHandler);

        // Init throttle callback.
        mOperationThrottleHandler = BlitzDelay.postDelayed(() -> mOperationThrottle = false, 250);
    }

    /**
     * Lazy load the loading dialog.
     *
     * @return Loading dialog.
     */
    private DialogLoading getLoadingDialog() {

        if (mDialogLoading == null && mActivity != null) {
            mDialogLoading = new DialogLoading(mActivity);
        }

        return mDialogLoading;
    }

    // endregion

    // region Interfaces
    // ============================================================================================================

    public interface OnSuccess<T> {
        public void onSuccess(T result);
    }

    public interface OnFailure {
        public void onFailure(Response response, boolean networkError);
    }

    // endregion
}