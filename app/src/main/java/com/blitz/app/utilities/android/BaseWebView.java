package com.blitz.app.utilities.android;

import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.blitz.app.utilities.animations.AnimHelper;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.blitz.BlitzDelay;

/**
 * Created by mrkcsc on 8/20/14. Copyright 2014 Blitz Studios
 */
public class BaseWebView extends WebView {

    // region Member Variables
    // ============================================================================================================

    // Progress bar for loading.
    private ProgressBar mLoadingView;

    // endregion

    // region Constructors
    // ============================================================================================================

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

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Set the loading view.
     *
     * @param loadingView A progress bar spinner.
     */
    public void setLoadingView(ProgressBar loadingView) {

        mLoadingView = loadingView;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Configure the web view.
     */
    private void configureWebView() {

        // Hide initially.
        setVisibility(INVISIBLE);

        // Set web-view as transparent.
        setBackgroundColor(0x00000000);

        // Disable hardware acceleration on older SDKs.
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {

            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // Add a custom web-view client.
        setWebViewClient(new WebViewClient() {

            /**
             * Ignore SSL errors - note this defeats the purpose
             * of SSL so this should be revised someday.
             */
            @Override
            public void onReceivedSslError(WebView view,
                                           @NonNull SslErrorHandler handler, SslError error) {

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

                // Wait for page transition.
                BlitzDelay.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // Fade in the web view.
                        AnimHelperFade.setVisibility(BaseWebView.this, VISIBLE,
                                AnimHelper.getConfigAnimTimeStandard() * 2);
                    }

                }, AnimHelper.getConfigAnimTimeStandard());
            }
        });
    }

    // endregion
}