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
    // ============================================================================================================

    // Url for this view set based on this.
    public static final String PARAM_URL = "PARAM_URL";

    // Title for this view set based on this.
    public static final String PARAM_TITLE = "PARAM_TITLE";

    // Transition type.
    public static final String PARAM_TRANSITION_TYPE = "PARAM_TRANSITION_TYPE";

    @InjectView(R.id.web_view_container) BaseWebView mWebView;
    @InjectView(R.id.web_view_loading) ProgressBar mProgressBar;
    @InjectView(R.id.web_header) TextView mHeader;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Setup creation.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseIntentParameters();
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Parse various intent parameters
     * passed as the settings to this activity.
     */
    private void parseIntentParameters() {

        // Fetch intent with params.
        Intent intent = getIntent();

        if (intent != null) {

            CustomTransition customTransitionType = (CustomTransition)intent
                    .getSerializableExtra(PARAM_TRANSITION_TYPE);

            if (customTransitionType != null) {

                setCustomTransitions(customTransitionType);
            }

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