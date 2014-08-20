package com.blitz.app.screens.sign_up;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseWebView;
import com.blitz.app.utilities.app.AppConfig;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 8/19/14.
 */
public class SignUpScreenTerms extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.sign_up_terms_web) BaseWebView mWebView;
    @InjectView(R.id.sign_up_terms_progress) ProgressBar mProgressBar;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Setup the web view.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the spinner.
        mWebView.setLoadingView(mProgressBar);

        // Set terms of use url.
        mWebView.loadUrl(AppConfig.getTermsOfUseUrl());
    }
}
