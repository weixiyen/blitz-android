package com.blitz.app.models.rest;

import com.blitz.app.models.rest_objects.JsonObject;

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

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Assign our member variables.
     *
     * @param restAPIObjectInterface Interface object.
     * @param operation Operation object.
     */
    public RestAPICallback(RestAPIObjectInterface restAPIObjectInterface, RestAPIOperation operation) {

        mRestAPIObjectInterface = restAPIObjectInterface;
        mOperation = operation;

        // Start operation as soon as initialized.
        mOperation.start();
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

        // Operation is finished, provide http status code.
        mOperation.finish(false, retrofitError.getResponse().getStatus());
    }
}