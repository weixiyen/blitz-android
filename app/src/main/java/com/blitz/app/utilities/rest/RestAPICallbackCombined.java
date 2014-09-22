package com.blitz.app.utilities.rest;

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

    // The model operation object.
    // TODO: Revise private RestAPIOperation mOperation;

    // Is this an auth call.
    private boolean mIsAuthentication;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Assign our member variables.
     */
    public RestAPICallbackCombined() {

        // Set the operation.
        // TODO: Revise mOperation = operation;

        // Start operation as soon as initialized.
        // TODO: Revise mOperation.start();
    }

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

    public void failure(Response response, boolean networkError) {

    }

    // endregion

    // region Abstract Methods
    // =============================================================================================

    public abstract void success(T jsonObject);

    // endregion

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

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
}