package com.blitz.app.models.rest;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public interface RestAPIObjectInterface {

    // Sets the API object.
    public void setApiObject(RestAPI.BaseApiObject apiObject);

    // Check for errors.
    public boolean hasErrors();
}