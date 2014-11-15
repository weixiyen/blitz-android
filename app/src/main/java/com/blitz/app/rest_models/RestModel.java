package com.blitz.app.rest_models;

/**
 * Created by mrkcsc on 9/7/14. Copyright 2014 Blitz Studios
 */
public class RestModel {

    // region Member Variables
    // ============================================================================================================

    // Object models have a final static instance of the rest API.
    protected static final RestAPI mRestAPI = RestAPIClient.getAPI();

    // endregion

    // region Member Variables
    // ============================================================================================================

    /**
     * Should this operation be throttled.
     *
     * @return Operation throttle flag.
     */
    @SuppressWarnings("unused")
    public static boolean shouldThrottle() {

        // In progress if loading or error dialog is on screen.
        return RestAPICallback.shouldThrottle();
    }

    // endregion
}