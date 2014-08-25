package com.blitz.app.screens.sign_up;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseWebView;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.authentication.AuthHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 8/19/14. Copyright 2014 Blitz Studios
 */
public class SignUpScreenLegal extends BaseActivity {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.sign_up_terms_web) BaseWebView mWebView;
    @InjectView(R.id.sign_up_legal_progress) ProgressBar mProgressBar;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup the web view.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the spinner.
        mWebView.setLoadingView(mProgressBar);

        // Set terms of use url.
        mWebView.loadUrl(AppConfig.getLegalUrl());
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    /**
     * User accepts legal agreement.
     */
    @OnClick(R.id.sign_up_legal_accept) @SuppressWarnings("unused")
    public void onLegalAcceptClick() {

        // User has accepted legal terms.
        AuthHelper.setLegalAccepted();

        // Now try to enter the app.
        AuthHelper.tryEnterMainApp(this);
    }

    // endregion
}