package com.blitz.app.utilities.rest;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class RestAPICallback<T> implements Callback<T> {

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    // The model operation object.
    private RestAPIOperation mOperation;

    // Is this an auth call.
    private boolean mIsAuthentication;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Assign our member variables.
     *
     * @param operation Operation object.
     * @param isAuthentication Is this an authentication call.
     */
    private RestAPICallback(RestAPIOperation operation, boolean isAuthentication) {

        mOperation = operation;

        // Start operation as soon as initialized.
        mOperation.start();

        // Set authentication flag.
        mIsAuthentication = isAuthentication;
    }

    /**
     * Create callback object.
     *
     * @param operation Operation object.
     * @param isAuthentication Is this an authentication call.
     *
     * @return New instance.
     */
    public static RestAPICallback<JsonObject> create(RestAPIOperation operation, boolean isAuthentication) {

        return new RestAPICallback<JsonObject>(operation, isAuthentication);
    }

    /**
     * Create callback object.
     *
     * @param operation Operation object.
     *
     * @return New instance.
     */
    public static RestAPICallback<JsonObject> create(RestAPIOperation operation) {

        return new RestAPICallback<JsonObject>(operation, false);
    }

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
        RestAPIObject restAPIObject = new RestAPIObject((JsonObject) jsonObject);

        // Finish the operation.
        mOperation.finish(restAPIObject);
    }

    /**
     * Unsuccessful REST call is made.
     *
     * @param retrofitError Retrofit error object.
     */
    @Override
    public void failure(RetrofitError retrofitError) {

        // Fetch response.
        Response response = retrofitError.getResponse();

        if (response == null) {
            mOperation.finish(null, false, null);
        } else {
            mOperation.finish(null, false, response.getStatus());
        }
    }
}