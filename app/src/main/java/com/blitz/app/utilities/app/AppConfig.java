package com.blitz.app.utilities.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.blitz.app.R;

/**
 * Intended to setup configuration constants for the App.
 *
 * Created by Miguel Gaeta on 6/1/14.
 */
@SuppressWarnings("unused")
public class AppConfig {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Base url, set on initialization.
    private static String mBaseURL;
    private static String mBaseURLComet;

    // Production flag.
    private static Boolean mIsProduction;

    //==============================================================================================
    // Configuration Methods (Modify with care)
    //==============================================================================================

    /**
     * Should we jump to an arbitrary activity on start.
     */
    public static Class getJumpToActivity() {

        return null;
    }

    /**
     * Should we clear app data on launch.
     */
    public static boolean isAppDataClearedOnLaunch() {

        return false;
    }

    /**
     * Should we have rest debugging.
     */
    public static boolean isRestDebuggingEnabled() {

        return false;
    }

    /**
     * Disable or enable sound for debugging.
     */
    public static boolean isSoundDisabled() {

        return false;
    }

    /**
     * Allow landscape for debugging purposes.
     */
    public static boolean isLandscapeEnabled() {

        return false;
    }

    /**
     * Should we ignore GCM registration result (can fail w/o play store).
     */
    public static boolean isGcmRegistrationIgnored() {

        return true;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Get production flag, this value is read
     * directly from the manifest.  Do not
     * modify this function directly.
     */
    public static boolean isProduction() {

        // Production flag.
        return mIsProduction;
    }

    /**
     * Fetch the URL for the CDN.
     */
    public static String getCDNUrl() {

        return "http://blitzcdn.com/static/content/";
    }

    /**
     * Fetch URL for the terms of use.
     */
    public static String getTermsOfUseUrl() {

        return mBaseURL + "terms?android=true";
    }

    /**
     * Fetch URL for the privacy policy.
     */
    public static String getPrivacyPolicyUrl() {

        return mBaseURL + "privacy?android=true";
    }

    /**
     * Fetch URL for legal terms.
     */
    public static String getLegalUrl() {

        return mBaseURL + "legal?android=true";
    }

    /**
     * Endpoint URL for RESTful API. Do not modify unless
     * API version is changed.
     */
    public static String getApiUrl() {

        // Base with rest API.
        return mBaseURL + "api";
    }

    /**
     * Endpoint URL for Comet Server. Do not modify unless
     * API version is changed.
     */
    public static String getWebsocketUrl() {

        // Base with comet API.
        return mBaseURLComet + "comet/u/";
    }

    /**
     * Initialize the application config.  Most of its internal
     * configuration is based on the environment string
     * which we read from the manifest.
     *
     * Carefully change configuration values from within this
     * file.  The only recommended changes would be QA related.
     *
     * @param context Application context.
     */
    public static void init(Context context) {

        try {

            // Fetch application into object.
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            // Obtain the metadata bundle.
            Bundle bundle = applicationInfo.metaData;

            switch (bundle.getInt("com.blitz.app.config.environment")) {

                case R.string.environment_production:
                case R.string.environment_staging:

                    mBaseURL = "https://blitz.zone/";
                    mBaseURLComet = "wss://blitz.zone/";

                    mIsProduction = true;

                    break;

                case R.string.environment_qa:

                    mBaseURL = "https://snapdraft.us/";
                    mBaseURLComet = "wss://snapdraft.us/";

                    mIsProduction = false;

                    break;
            }
        } catch (Exception e) {

            // Cannot proceed without configuration set.
            throw new RuntimeException("Unable to ready configuration values.");
        }
    }
}