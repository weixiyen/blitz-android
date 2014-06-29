package com.blitz.app.models.rest;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class BlitzRestAPIObject implements BlitzRestAPIObjectInterface {

    // The api object this class represents.
    public BlitzRestAPI.BaseApiObject mApiObject;

    /**
     * Query the object to see if it
     * contains any errors.
     *
     * @return True/false.
     */
    @Override
    public boolean hasErrors() {

        return mApiObject != null &&
               mApiObject.hasErrors();
    }

    /**
     * Set the api object.
     *
     * @param apiObject Provided object.
     */
    @Override
    public void setApiObject(BlitzRestAPI.BaseApiObject apiObject) {
        mApiObject = apiObject;
    }
}