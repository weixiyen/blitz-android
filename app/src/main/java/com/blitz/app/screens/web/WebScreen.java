package com.blitz.app.screens.web;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseWebView;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 8/14/14. Copyright 2014 Blitz Studios
 */
public class WebScreen extends BaseActivity {

    // region Member Variables
    // =============================================================================================

    // Url for this view set based on this.
    public static final String PARAM_URL = "PARAM_URL";

    // Title for this view set based on this.
    public static final String PARAM_TITLE = "PARAM_TITLE";

    @InjectView(R.id.web_view_container) BaseWebView mWebView;
    @InjectView(R.id.web_view_loading) ProgressBar mProgressBar;
    @InjectView(R.id.web_header) TextView mHeader;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Run custom transitions if needed.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch intent with params.
        Intent intent = getIntent();

        if (intent != null) {

            // Set the spinner.
            mWebView.setLoadingView(mProgressBar);

            // Load target url.
            mWebView.loadUrl(intent.getStringExtra(PARAM_URL));

            // Load target header text.
            mHeader.setText(intent.getStringExtra(PARAM_TITLE));
        }
    }

    // endregion
}