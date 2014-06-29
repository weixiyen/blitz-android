package com.blitz.app.models.rest;

import com.blitz.app.base.api.BaseAPIClient;

public class BlitzRestAPIClient extends BaseAPIClient {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Singleton instance.
    private static BlitzRestAPI api = null;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Fetch singleton client.
     *
     * @return Singleton object.
     */
    public static BlitzRestAPI getAPI() {

        if (api == null) {
            synchronized (BlitzRestAPIClient.class) {
                if (api == null) {
                    api = getInstance().getRestAdapter().create(BlitzRestAPI.class);
                }
            }
        }

        return api;
    }
}