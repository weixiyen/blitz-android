package com.blitz.app.models.rest;

import com.blitz.app.models.rest_objects.JsonObject;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public interface RestAPIObjectInterface {

    // Sets the API object.
    public void setJsonObject(JsonObject jsonObject);

    // Check for errors.
    public boolean hasErrors();
}