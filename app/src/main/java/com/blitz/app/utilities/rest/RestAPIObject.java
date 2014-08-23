package com.blitz.app.utilities.rest;


import com.google.gson.JsonObject;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class RestAPIObject {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // The api object this class represents.
    private JsonObject mJsonObject;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Private empty constructor.
     */
    @SuppressWarnings("unused")
    private RestAPIObject() {

    }

    /**
     * Create a rest API object with a
     * json object payload.
     *
     * @param jsonObject Json object.
     */
    @SuppressWarnings("unused")
    public RestAPIObject(JsonObject jsonObject) {

        mJsonObject = jsonObject;
    }

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Query the object to see if it
     * contains any errors.
     *
     * @return True/false.
     */
    public boolean hasErrors() {

        // Check for presence of errors result.
        return mJsonObject != null && mJsonObject.has("errors");
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Fetch json object.
     *
     * @return Json object.
     */
    @SuppressWarnings("unused")
    public JsonObject getJsonObject() {

        return mJsonObject;
    }
}