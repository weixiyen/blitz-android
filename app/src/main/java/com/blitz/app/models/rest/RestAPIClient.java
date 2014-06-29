package com.blitz.app.models.rest;

import com.blitz.app.base.api.BaseAPIClient;

public class RestAPIClient extends BaseAPIClient {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Singleton instance.
    private static RestAPI api = null;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Fetch singleton client.
     *
     * @return Singleton object.
     */
    public static RestAPI getAPI() {

        if (api == null) {
            synchronized (RestAPIClient.class) {
                if (api == null) {
                    api = getInstance().getRestAdapter().create(RestAPI.class);
                }
            }
        }

        return api;
    }
}