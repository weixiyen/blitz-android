package com.blitz.app.models.rest;

import com.blitz.app.utilities.app.AppDataObject;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.Response;

public class RestAPIClient extends RestAPIClientBase {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Singleton instance.
    private static RestAPI api = null;

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

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
        RequestInterceptor requestInterceptor = new RequestInterceptor() {

            @Override
            public void intercept(RequestFacade request) {

                // Attempt to fetch the app data cookie.
                String cookie = AppDataObject.userCookie.getString();

                if (cookie != null) {

                    // Add to header if exists.
                    request.addHeader("Cookie", cookie);
                }
            }
        };

        // Set interceptor.
        builder.setRequestInterceptor(requestInterceptor);

        // Return builder.
        return builder;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

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
     * Fetch singleton client.
     *
     * @return Singleton object.
     */
    public static RestAPI getAPI() {

        if (api == null) {
            synchronized (RestAPIClient.class) {
                if (api == null) {
                    api = new RestAPIClient().getRestAdapter().create(RestAPI.class);
                }
            }
        }

        return api;
    }
}