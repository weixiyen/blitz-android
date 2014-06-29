package com.blitz.app.models.rest;

import com.blitz.app.models.objects.ObjectModelCode;
import com.blitz.app.models.objects.ObjectModelOperation;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class BlitzRestApiCallback<T extends BlitzRestAPI.BaseApiObject> implements Callback<T> {

    private ObjectModelCode mObjectModelCode;
    private ObjectModelOperation mOperation;

    public BlitzRestApiCallback(ObjectModelCode fart, ObjectModelOperation operation) {
        mOperation = operation;
        mObjectModelCode = fart;
    }

    @Override
    public void success(T t, Response response) {

        mObjectModelCode.setApiObject((BlitzRestAPI.Code)t);

        if (mObjectModelCode.getApiObject().hasErrors()) {
            mOperation.complete(false);
        } else {
            mOperation.complete(true);
        }
    }

    @Override
    public void failure(RetrofitError retrofitError) {

        mOperation.complete(false);
    }
}