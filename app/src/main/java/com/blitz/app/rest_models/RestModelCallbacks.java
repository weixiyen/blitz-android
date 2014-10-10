package com.blitz.app.rest_models;

import java.util.List;

/**
 * Created by mrkcsc on 10/9/14. Copyright 2014 Blitz Studios
 */
public abstract class RestModelCallbacks<T extends RestModel> {

    /**
     * Emit response object list on success
     * where object is a subclass of RestModel.
     *
     * @param object List of object result.
     */
    public abstract void onSuccess(List<T> object);

    @SuppressWarnings("unused")
    public void onFailure() {

    }
}