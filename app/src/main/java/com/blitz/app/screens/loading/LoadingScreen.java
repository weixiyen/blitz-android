package com.blitz.app.screens.loading;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.base.config.BaseConfig;
import com.blitz.app.screens.queue.QueueScreen;
import com.blitz.app.screens.splash.SplashScreen;
import com.blitz.app.utilities.appdata.AppDataObject;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable custom transitions.
        setCustomTransitions(false);

        // If we want to jump to some activity.
        if (BaseConfig.JUMP_TO_ACTIVITY != null) {

            // Do it - this would only really be done for debugging.
            startActivity(new Intent(this, BaseConfig.JUMP_TO_ACTIVITY));
        } else {

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

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                if (AppDataObject.hasAccess.getBoolean()) {

                    // User can go to splash screen and sign-in/register.
                    startActivity(new Intent(LoadingScreen.this, SplashScreen.class));

                } else {

                    // User is blocked on Queue screen.
                    startActivity(new Intent(LoadingScreen.this, QueueScreen.class));
                }

                // close this activity
                finish();

            }
        }, SPLASH_TIME_OUT);
    }
}