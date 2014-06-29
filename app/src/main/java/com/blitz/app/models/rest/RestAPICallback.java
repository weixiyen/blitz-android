package com.blitz.app.models.rest;

import com.blitz.app.models.operation.ModelOperationInterface;

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
    private ModelOperationInterface mOperation;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Assign our member variables.
     *
     * @param restAPIObjectInterface Interface object.
     * @param operation Operation object.
     */
    public RestAPICallback(RestAPIObjectInterface restAPIObjectInterface, ModelOperationInterface operation) {

        mRestAPIObjectInterface = restAPIObjectInterface;
        mOperation = operation;
    }

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Successful REST call is made.
     *
     * @param t Resulting JSON parsed object.
     * @param response Response.
     */
    @Override
    public void success(T t, Response response) {

        // Set the api object which we receive on success.
        mRestAPIObjectInterface.setApiObject((RestAPI.BaseApiObject) t);

        // Report success or failure.
        if (mRestAPIObjectInterface.hasErrors()) {
            mOperation.failure();
        } else {
            mOperation.success();
        }
    }

    /**
     * Unsuccessful REST call is made.
     *
     * @param retrofitError Retrofit error object.
     */
    @Override
    public void failure(RetrofitError retrofitError) {

        mOperation.failure();
    }
}