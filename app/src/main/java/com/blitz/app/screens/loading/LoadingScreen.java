package com.blitz.app.screens.loading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.blitz.BlitzDelay;
import com.blitz.app.utilities.date.DateUtils;
import com.blitz.app.utilities.gcm.GcmRegistrationHelper;
import com.blitz.app.utilities.sound.SoundHelper;
import com.crashlytics.android.Crashlytics;

import butterknife.InjectView;

/**
 * Created by Miguel Gaeta on 6/28/14. Copyright 2014 Blitz Studios
 */
public class LoadingScreen extends BaseActivity {

    // region Member Variables
    // =============================================================================================

    // Splash screen timer
    private static final int MINIMUM_TIME_TO_DISPLAY = 1500;

    // Loading spinner.
    @InjectView(R.id.loading_spinner) ProgressBar mSpinner;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup and jump to associated activity.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppConfig.isProduction()) {

            // Enable crash logging.
            Crashlytics.start(this);
        }

        // Disable custom transitions.
        setCustomTransitions(null);

        // Clear app data if configured.
        if (AppConfig.isAppDataClearedOnLaunch()) {

            AppData.clear();
        }

        // Try to register for GCM - used for notifications.
        boolean gcmRegistrationResult =
                GcmRegistrationHelper.tryRegistration(this);

        // If ignoring registration result, or have registration result.
        if (AppConfig.isGcmRegistrationIgnored() || gcmRegistrationResult) {

            // Stop all music.
            SoundHelper.instance().stopMusic();

            // Proceed after short delay.
            tryEnterMainApp();
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Try to enter the main application but also enforce
     * a minimum loading time, and have a clean UI
     * transition when we are ready.
     */
    private void tryEnterMainApp() {

        // First fetch the starting time.
        final long startTime = DateUtils.getDateInGMTMilliseconds();

        // Try to enter the application.
        AuthHelper.instance().tryEnterMainApp(this, new AuthHelper.EnterMainAppCallback() {

            @Override
            public void onReady(final Class targetActivity) {


                transitionIntoApp(targetActivity,
                        DateUtils.getDateInGMTMilliseconds() - startTime);
            }
        });
    }

    /**
     * Transition into the main app activity.
     *
     * @param targetActivity Target activity.
     * @param loadingTime Loading time so far.
     */
    private void transitionIntoApp(final Class targetActivity, long loadingTime) {

        // Show loading screen for initial loading.
        BlitzDelay.postDelayed(new Runnable() {

            @Override
            public void run() {

                // Stop spinner animation to create a smooth transition.
                AnimHelperFade.setVisibility(mSpinner, View.GONE, new Runnable() {

                    @Override
                    public void run() {

                        // Start target activity, clear the history.
                        LoadingScreen.this.startActivity(new Intent(LoadingScreen.this, targetActivity), true);

                        // Always destroy the loading
                        // activity as we leave.
                        LoadingScreen.this.finish();
                    }
                });
            }
        }, Math.max(MINIMUM_TIME_TO_DISPLAY - loadingTime, 0));
    }

    // endregion
}