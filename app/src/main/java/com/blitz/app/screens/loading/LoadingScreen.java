package com.blitz.app.screens.loading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blitz.app.R;
import com.blitz.app.screens.access_queue.AccessQueueScreen;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.screens.splash.SplashScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.gcm.GcmRegistrationHelper;
import com.blitz.app.utilities.sound.SoundHelper;

/**
 * Created by Miguel Gaeta on 6/28/14.
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
    @SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable custom transitions.
        setCustomTransitions(null);

        // Clear app data if configured.
        if (AppConfig.CLEAR_APP_DATA_ON_LAUNCH) {

            AppData.clear();
        }

        // Try to register for GCM - used for notifications.
        boolean gcmRegistrationResult =
                GcmRegistrationHelper.tryRegistration(this);

        // If ignoring registration result, or have registration result.
        if (AppConfig.IGNORE_GCM_REGISTRATION_RESULT || gcmRegistrationResult) {

            // If we want to jump to some activity.
            if (AppConfig.JUMP_TO_ACTIVITY != null) {

                // Do it - this would only really be done for debugging.
                startActivity(new Intent(this, AppConfig.JUMP_TO_ACTIVITY));
            } else {

                setupLoadingScreenTimeout();
            }

            // Stop all music.
            SoundHelper.instance().stopMusic();
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

                // After delay, jump into
                // the first activity.
                jumpToFirstActivity();

            }
        }, SPLASH_TIME_OUT);
    }

    /**
     * Based on various AppData and AppConfig
     * preferences, figure out which activity
     * to take the user to.
     */
    private void jumpToFirstActivity() {

        // If we want to jump to some activity.
        if (AppConfig.JUMP_TO_ACTIVITY != null) {

            // Do it - this would only really be done for debugging.
            startActivity(new Intent(LoadingScreen.this, AppConfig.JUMP_TO_ACTIVITY));
        } else {

            if (AuthHelper.isPassedQueue()) {

                if (AuthHelper.isSignedIn()) {

                    // User is already logged in, go to main app.
                    startActivity(new Intent(LoadingScreen.this, MainScreen.class));

                } else {

                    // User must go to splash screen and sign-in/register.
                    startActivity(new Intent(LoadingScreen.this, SplashScreen.class));
                }

            } else {

                // User is blocked on Queue screen.
                startActivity(new Intent(LoadingScreen.this, AccessQueueScreen.class));
            }
        }

        // Play the lobby music after loading.
        SoundHelper.instance().startMusic(R.raw.music_lobby_loop0, R.raw.music_lobby_loopn);
        SoundHelper.instance().setMusicDisabled(AppDataObject.settingsMusicDisabled.getBoolean());

        // close this activity
        finish();
    }
}