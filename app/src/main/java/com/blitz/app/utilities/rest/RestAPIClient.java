package com.blitz.app.utilities.rest;

import com.blitz.app.utilities.app.AppDataObject;

import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/26/14. Copyright 2014 Blitz Studios
 */
public class RestAPIClient extends RestAPIClientBase {

    // region Member Variables
    // =============================================================================================

    // Singleton instance for API and client.
    private static RestAPI       mInstanceApi = null;
    private static RestAPIClient mInstance;

    // endregion

    // region Protected Methods
    // =============================================================================================

    /**
     * Add cookies to the request.
     *
     * @return Rest adapter builder.
     */
    @Override
    protected RestAdapter.Builder getRestBuilder() {

        // Call super to make the builder.
        RestAdapter.Builder builder = super.getRestBuilder();

        // Intercept requests and add user cookie if available.
        builder.setRequestInterceptor(new retrofit.RequestInterceptor() {

            @Override
            public void intercept(RequestFacade request) {

                // Attempt to fetch the app data cookie.
                String cookie = AppDataObject.userCookie.get();

                if (cookie != null) {

                    // Add to header if exists.
                    request.addHeader("Cookie", cookie);
                }
            }
        });

        // Return builder.
        return builder;
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Given a response, try to find and
     * set the users cookies.
     *
     * @param response Response.
     */
    public static void trySetUserCookies(Response response) {

        // If headers exists.
        if (response.getHeaders() != null) {

            String cookie = null;

            // Look through each header.
            for (Header header : response.getHeaders()) {

                // If set cookie command.
                if (header.getName() != null &&
                    header.getName().equals("Set-Cookie")) {

                    if (cookie == null) {
                        cookie = header.getValue();
                    } else {
                        cookie += "; " + header.getValue();
                    }
                }
            }

            if (cookie != null) {

                // Persist the cookie.
                AppDataObject.userCookie.set(cookie);
            }
        }
    }

    /**
     * Fetch class singleton instance.
     */
    public static RestAPIClient instance() {

        if (mInstance == null) {
            synchronized (RestAPIClient.class) {
                if (mInstance == null) {
                    mInstance = new RestAPIClient();
                }
            }
        }

        return mInstance;
    }

    /**
     * Fetch singleton instance for api client.
     */
    public static RestAPI getAPI() {

        if (mInstanceApi == null) {
            synchronized (RestAPI.class) {
                if (mInstanceApi == null) {
                    mInstanceApi = new RestAPIClient().getRestAdapter().create(RestAPI.class);
                }
            }
        }

        return mInstanceApi;
    }

    // endregion
}