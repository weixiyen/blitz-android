package com.blitz.app.models.rest;

import com.blitz.app.models.rest_objects.JsonObject;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class RestAPIObject implements RestAPIObjectInterface {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // The api object this class represents.
    public JsonObject mJsonObject;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Query the object to see if it
     * contains any errors.
     *
     * @return True/false.
     */
    @Override
    public boolean hasErrors() {

        return mJsonObject != null &&
               mJsonObject.hasErrors();
    }

    /**
     * Set the json object.
     *
     * @param jsonObject Provided object.
     */
    @Override
    public void setJsonObject(JsonObject jsonObject) {
        mJsonObject = jsonObject;
    }
}