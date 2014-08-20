package com.blitz.app.utilities.android;

import android.content.Context;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by mrkcsc on 8/20/14.
 */
public class BaseWebView extends WebView {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Progress bar for loading.
    private ProgressBar mLoadingView;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Configure custom web view.
     */
    @SuppressWarnings("unused")
    public BaseWebView(Context context) {
        super(context);

        configureWebView();
    }

    /**
     * Configure custom web view.
     */
    @SuppressWarnings("unused")
    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        configureWebView();
    }

    /**
     * Configure custom web view.
     */
    @SuppressWarnings("unused")
    public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        configureWebView();
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Set the loading view.
     *
     * @param loadingView A progress bar spinner.
     */
    public void setLoadingView(ProgressBar loadingView) {

        mLoadingView = loadingView;
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Configure the web view.
     */
    private void configureWebView() {

        // Set web-view as transparent.
        setBackgroundColor(0x00000000);

        // Add a custom web-view client.
        setWebViewClient(new WebViewClient() {

            /**
             * Ignore SSL errors - note this defeats the purpose
             * of SSL so this should be revised someday.
             */
            @Override
            public void onReceivedSslError(WebView view, @NonNull SslErrorHandler handler, SslError error) {

                // Continue.
                handler.proceed();
            }

            /**
             * Hide loading progress view
             * when the page is loaded.
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Remove the progress bar.
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(View.GONE);
                }
            }
        });
    }
}