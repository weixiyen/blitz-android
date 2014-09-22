package com.blitz.app.utilities.rest;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.dialogs.error.DialogError;
import com.blitz.app.dialogs.loading.DialogLoading;

import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/29/14. Copyright 2014 Blitz Studios
 */
public abstract class RestAPICallbackCombined<T> implements Callback<T> {

    //==============================================================================================
    // Overwritten Methods
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

    // Is this an auth call.
    private boolean mIsAuthentication;

    // region Abstract Methods
    // =============================================================================================

    public abstract void success(T jsonObject);

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Assign our member variables.
     */
    public RestAPICallbackCombined() {

        // Set the operation.
        // TODO: Revise mOperation = operation;

        // Start operation as soon as initialized.
        // TODO: Revise mOperation.start();
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Successful REST call is made.
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

        // Create a new rest API object from the result.
        // TODO: Revise RestAPIObject restAPIObject = new RestAPIObject((JsonObject) jsonObject);

        // Finish the operation.
        // TODO: Revise mOperation.finish(restAPIObject, null);

        // TODO: Revise
        success(jsonObject);
    }

    /**
     * Unsuccessful REST call is made.
     *
     * @param retrofitError Retrofit error object.
     */
    @Override
    public void failure(RetrofitError retrofitError) {

        // Finish with error.
        // TODO: Revise mOperation.finish(null, retrofitError);
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Is this an authentication callback.
     *
     * @param isAuthentication Is this an authentication call.
     */
    @SuppressWarnings("unused")
    public void setIsAuthentication(boolean isAuthentication) {
        mIsAuthentication = isAuthentication;
    }

    @SuppressWarnings("unused")
    public void failure(Response response, boolean networkError) {

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
}