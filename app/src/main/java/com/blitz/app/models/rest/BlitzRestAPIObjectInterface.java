package com.blitz.app.models.rest;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public interface BlitzRestAPIObjectInterface {

    // Sets the API object.
    public void setApiObject(BlitzRestAPI.BaseApiObject apiObject);

    // Check for errors.
    public boolean hasErrors();
}