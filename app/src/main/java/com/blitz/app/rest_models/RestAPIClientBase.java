package com.blitz.app.rest_models;

import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.json.JsonHelper;
import com.blitz.app.utilities.ssl.SSLHelper;
import com.squareup.okhttp.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Miguel Gaeta on 6/25/14. Copyright 2014 Blitz Studios
 */
class RestAPIClientBase {

    // region Member Variables
    // ============================================================================================================

    // Endpoint URL for the REST API.
    private static final String API_URL = AppConfig.getApiUrl();

    // Adapter for REST client.
    private RestAdapter mRestAdapter;

    // endregion

    // region Protected Methods
    // ============================================================================================================

    /**
     * Fetch rest adapter for subsequent calls.
     *
     * @return Rest adapter.
     */
    protected RestAdapter getRestAdapter() {

        if (mRestAdapter == null) {

            // Initialize the builder.
            RestAdapter.Builder builder = getRestBuilder();

            // Create the adapter.
            mRestAdapter = builder.build();
        }

        return mRestAdapter;
    }

    /**
     * Fetch a builder for the rest adapter.
     *
     * @return Rest adapter builder.
     */
    protected RestAdapter.Builder getRestBuilder() {

        // Initialize the builder.
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setClient(new OkClient(getOkHttpClient()))
                .setConverter(new GsonConverter(JsonHelper.builder()))
                .setEndpoint(API_URL);

        // If rest debugging turned on.
        if (AppConfig.isRestDebuggingEnabled()) {

            // Add logging if enabled.
            builder.setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new AndroidLog("REST"));
        }

        return builder;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Fetch an http client that ignores, SSL
     * security certificates.
     *
     * @return Custom HTTP client.
     */
    private OkHttpClient getOkHttpClient() {


            // Create a new client.
            OkHttpClient okHttpClient = new OkHttpClient();

            // Setup hostname verifier.
            setupHostnameVerifier(okHttpClient);

            // Setup SSL.
            setupSSL(okHttpClient);

            return okHttpClient;
    }

    /**
     * Create a custom verifier that does not
     * attempt to verify host names.
     *
     * @param okHttpClient Target client.
     */
    private void setupHostnameVerifier(OkHttpClient okHttpClient) {

        // Create a verifier that does not verify host-names.
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {

                return true;
            }
        };

        // Assign it to the client.
        okHttpClient.setHostnameVerifier(hostnameVerifier);
    }

    /**
     * Setup a custom SSL that does not
     * validate certificates.
     *
     * @param okHttpClient Target client.
     */
    private void setupSSL(OkHttpClient okHttpClient) {

        // Create an ssl socket factory with our all-trusting manager.
        SSLSocketFactory sslSocketFactory = SSLHelper.createInsecureSSLSocketFactory();

        if (sslSocketFactory != null) {

            // Assign it to the client.
            okHttpClient.setSslSocketFactory(sslSocketFactory);
        }
    }

    // endregion
}