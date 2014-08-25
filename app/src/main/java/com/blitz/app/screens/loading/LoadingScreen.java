package com.blitz.app.screens.loading;

import android.os.Bundle;
import android.os.Handler;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.gcm.GcmRegistrationHelper;
import com.blitz.app.utilities.sound.SoundHelper;

/**
 * Created by Miguel Gaeta on 6/28/14. Copyright 2014 Blitz Studios
 */
public class LoadingScreen extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 1500;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Setup and jump to associated activity.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            setupLoadingScreenTimeout();
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Artificial loading screen delay.
     */
    private void setupLoadingScreenTimeout() {

        // Show loading screen for initial loading.
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // Try to enter the application.
                AuthHelper.tryEnterMainApp(LoadingScreen.this);

                // Play the lobby music after loading.
                SoundHelper.instance().startMusic(R.raw.music_lobby_loop0, R.raw.music_lobby_loopn);
                SoundHelper.instance().setMusicDisabled(AppDataObject.settingsMusicDisabled.get());

                // Always destroy the loading
                // activity as we leave.
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}