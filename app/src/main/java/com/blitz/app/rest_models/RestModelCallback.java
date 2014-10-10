package com.blitz.app.rest_models;

/**
 * Created by mrkcsc on 10/9/14. Copyright 2014 Blitz Studios
 */
public abstract class RestModelCallback<T extends RestModel> {

    /**
     * Emit response object on success
     * where object is a subclass of RestModel.
     *
     * @param object Object result.
     */
    public abstract void onSuccess(T object);

    @SuppressWarnings("unused")
    public void onFailure() {

    }
}