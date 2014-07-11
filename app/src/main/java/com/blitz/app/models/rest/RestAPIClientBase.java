package com.blitz.app.models.rest;

import com.blitz.app.utilities.appconfig.AppConfig;
import com.blitz.app.utilities.cookiestore.PersistentCookieStore;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

/**
 * Created by Miguel Gaeta on 6/25/14.
 */
public class RestAPIClientBase {

    // Endpoint URL for the REST API.
    private static final String API_URL = AppConfig.API_URL;

    // Singleton instance.
    private static RestAPIClientBase instance = null;

    private RestAdapter mRestAdapter;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Fetch rest adapter for subsequent calls.
     *
     * @return Rest adapter.
     */
    public RestAdapter getRestAdapter() {

        if (mRestAdapter == null) {

            // Initialize the builder.
            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setClient(new OkClient(getOkHttpClient()))
                    .setEndpoint(API_URL);

            if (AppConfig.ENABLE_REST_DEBUGGING) {

                // Add logging if enabled.
                builder.setLogLevel(RestAdapter.LogLevel.FULL)
                       .setLog(new AndroidLog("REST"));
            }

            // Create the adapter.
            mRestAdapter = builder.build();
        }

        return mRestAdapter;
    }

    /**
     * Get instance of the base API client.
     *
     * @return Singleton instance.
     */
    public static RestAPIClientBase getInstance() {

        if (instance == null) {
            synchronized (RestAPIClientBase.class) {
                if (instance == null) {
                    instance = new RestAPIClientBase();
                }
            }
        }

        return instance;
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Fetch an http client that ignores, SSL
     * security certificates.
     *
     * @return Custom HTTP client.
     */
    private OkHttpClient getOkHttpClient() {

        try {

            // Create a new client.
            OkHttpClient okHttpClient = new OkHttpClient();

            // Setup hostname verifier.
            setupHostnameVerifier(okHttpClient);

            // Setup SSL.
            setupSSL(okHttpClient);

            // Setup cookie handler.
            setupCookieHandler(okHttpClient);

            return okHttpClient;

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
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
     *
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    private void setupSSL(OkHttpClient okHttpClient) throws KeyManagementException, NoSuchAlgorithmException {

        // Create trust manager that does not validate certs chains.
        final TrustManager[] trustAllCerts = new TrustManager[] {

                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                }
        };

        // Install the all-trusting trust manager.
        final SSLContext sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // Create an ssl socket factory with our all-trusting manager.
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        // Assign it to the client.
        okHttpClient.setSslSocketFactory(sslSocketFactory);
    }

    /**
     * Setup a custom cookie handler to
     * store and persist all cookies.
     *
     * @param okHttpClient Target client.
     */
    private void setupCookieHandler(OkHttpClient okHttpClient) {

        // Create a persistent cookie store.
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore();

        // Accept all cookies.
        CookieManager cookieManager = new CookieManager
                (persistentCookieStore, CookiePolicy.ACCEPT_ALL);

        // Set the cookie manager as handler.
        okHttpClient.setCookieHandler(cookieManager);
    }
}