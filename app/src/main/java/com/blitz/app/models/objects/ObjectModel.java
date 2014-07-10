package com.blitz.app.models.objects;

import com.blitz.app.models.rest.RestAPIObject;
import com.blitz.app.models.rest_objects.JsonObject;

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
     * @param type Object type.
     * @param <T> Type.
     *
     * @return Json object.
     */
    protected <T extends JsonObject> T getJsonObject(Class<T> type) {

        return mRestApiObject.getJsonObject(type);
    }
}