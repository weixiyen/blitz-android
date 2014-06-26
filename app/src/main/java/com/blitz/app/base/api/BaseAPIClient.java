package com.blitz.app.base.api;

import com.blitz.app.base.config.BaseConfig;
import com.squareup.okhttp.OkHttpClient;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Miguel Gaeta on 6/25/14.
 */
public class BaseAPIClient {

    // Endpoint URL for the REST API.
    private static final String API_URL = BaseConfig.API_URL;

    // Singleton instance.
    private static BaseAPIClient instance = null;

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
            mRestAdapter = new RestAdapter.Builder()
                    .setClient(new OkClient(getOkHttpClient()))
                    .setEndpoint(API_URL)
                    .build();
        }

        return mRestAdapter;
    }

    /**
     * Get instance of the base API client.
     *
     * @return Singleton instance.
     */
    public static BaseAPIClient getInstance() {

        if (instance == null) {
            synchronized (BaseAPIClient.class) {
                if (instance == null) {
                    instance = new BaseAPIClient();
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

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}