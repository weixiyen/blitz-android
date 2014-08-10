package com.blitz.app.models.objects;

import com.blitz.app.utilities.rest.RestAPIObject;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/9/14.
 */
public class ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Rest api code object.
    protected RestAPIObject mRestApiObject;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Empty constructor disallowed.
     */
    public ObjectModel() {

        // Instantiate the api object which we
        // can use to make REST calls.
        mRestApiObject = new RestAPIObject();
    }

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * Fetch JSON object.
     *
     * @return Json object.
     */
    protected JsonObject getJsonObject() {

        return mRestApiObject.getJsonObject();
    }
}