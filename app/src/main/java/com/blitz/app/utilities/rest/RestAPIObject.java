package com.blitz.app.utilities.rest;


import com.google.gson.JsonObject;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class RestAPIObject implements RestAPIObjectInterface {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // The api object this class represents.
    private JsonObject mJsonObject;

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

        // Check for presence of errors result.
        return mJsonObject != null && mJsonObject.has("errors");
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

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Fetch json object.
     *
     * @return Json object.
     */
    public JsonObject getJsonObject() {

        return mJsonObject;
    }
}