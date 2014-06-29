package com.blitz.app.models.rest;

import com.blitz.app.models.objects.ObjectModelCode;
import com.blitz.app.models.operation.ModelOperationInterface;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class BlitzRestApiCallback<T> implements Callback<T> {

    private ObjectModelCode mObjectModelCode;
    private ModelOperationInterface mOperation;

    public BlitzRestApiCallback(ObjectModelCode fart, ModelOperationInterface operation) {
        mOperation = operation;
        mObjectModelCode = fart;
    }

    @Override
    public void success(T t, Response response) {

        mObjectModelCode.setApiObject((BlitzRestAPI.Code)t);

        if (mObjectModelCode.getApiObject().hasErrors()) {
            mOperation.failure();
        } else {
            mOperation.success();
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {

        mOperation.failure();
    }
}