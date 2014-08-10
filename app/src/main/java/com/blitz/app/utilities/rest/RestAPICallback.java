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

    // The interface needed for callback operations on the API.
    private RestAPIObjectInterface mRestAPIObjectInterface;

    // The model operation object.
    private RestAPIOperation mOperation;

    // Is this an auth call.
    private boolean mIsAuthentication;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * @see com.blitz.app.utilities.rest.RestAPICallback
     */
    public RestAPICallback(RestAPIObjectInterface restAPIObjectInterface, RestAPIOperation operation) {

        // Not authenticated by default.
        this(restAPIObjectInterface, operation, false);
    }

    /**
     * Assign our member variables.
     *
     * @param restAPIObjectInterface Interface object.
     * @param operation Operation object.
     * @param isAuthentication Is this an authentication call.
     */
    public RestAPICallback(RestAPIObjectInterface restAPIObjectInterface, RestAPIOperation operation, boolean isAuthentication) {

        mRestAPIObjectInterface = restAPIObjectInterface;
        mOperation = operation;

        // Start operation as soon as initialized.
        mOperation.start();

        // Set authentication flag.
        mIsAuthentication = isAuthentication;
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

        // Set the api object which we receive on success.
        mRestAPIObjectInterface.setJsonObject((JsonObject) jsonObject);

        // Operation is finished, pass in success/fail boolean.
        mOperation.finish(!mRestAPIObjectInterface.hasErrors());
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
            mOperation.finish(false);
        } else {
            mOperation.finish(false, response.getStatus());
        }
    }
}