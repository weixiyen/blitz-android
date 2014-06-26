package com.blitz.app.models.api;

import com.blitz.app.base.api.BaseAPIClient;

public class BlitzAPIClient extends BaseAPIClient {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Singleton instance.
    private static BlitzAPI api = null;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Fetch singleton client.
     *
     * @return Singleton object.
     */
    public static BlitzAPI getAPI() {

        if (api == null) {
            synchronized (BlitzAPIClient.class) {
                if (api == null) {
                    api = getInstance().getRestAdapter().create(BlitzAPI.class);
                }
            }
        }

        return api;
    }
}