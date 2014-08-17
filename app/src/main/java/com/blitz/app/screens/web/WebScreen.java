package com.blitz.app.screens.web;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 8/14/14.
 */
public class WebScreen extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Url for this view set based on this.
    public static final String PARAM_URL = "PARAM_URL";

    // Title for this view set based on this.
    public static final String PARAM_TITLE = "PARAM_TITLE";

    @InjectView(R.id.web_view_container) WebView mWebView;
    @InjectView(R.id.web_view_loading) ProgressBar mProgressBar;
    @InjectView(R.id.web_header) TextView mHeader;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Run custom transitions if needed.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure web view.
        configureWebView();

        // Fetch intent with params.
        Intent intent = getIntent();

        if (intent != null) {

            // Load target url.
            mWebView.loadUrl(intent.getStringExtra(PARAM_URL));

            // Load target header text.
            mHeader.setText(intent.getStringExtra(PARAM_TITLE));
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Configure the web view.
     */
    private void configureWebView() {

        // Set web-view as transparent.
        mWebView.setBackgroundColor(0x00000000);
        mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        // Add a custom web-view client.
        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * Ignore SSL errors - note this defeats the purpose
             * of SSL so this should be revised someday.
             */
            @Override
            public void onReceivedSslError(WebView view, @NonNull SslErrorHandler handler, SslError error) {

                handler.proceed();
            }

            /**
             * Hide loading progress view
             * when the page is loaded.
             */
            @Override
            public void onPageFinished (WebView view, String url) {
                super.onPageFinished(view, url);

                // Remove the progress bar.
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
