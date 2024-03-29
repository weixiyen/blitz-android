package com.blitz.app.utilities.android;

import android.app.Application;

import com.amplitude.api.Amplitude;
import com.blitz.app.R;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.background.EnteredBackground;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.sound.SoundHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Miguel Gaeta on 6/1/14. Copyright 2014 Blitz Studios
 */
public class BaseApplication extends Application implements EnteredBackground.Callbacks {

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Initialize custom font on application start.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize app-data.
        AppData.init(this);

        // Initialize configuration.
        AppConfig.init(this);

        // Initialize the keyboard utility.
        KeyboardUtility.init(this);

        // Initialize sound helper.
        SoundHelper.init(this);

        // Initialize background timer.
        EnteredBackground.init(this);

        // Set the regular font weight to be the default.
        CalligraphyConfig.initDefault(getResources().getString(R.string.app_font_light),
                R.attr.fontPath);
    }

    /**
     * Reports when the application has
     * entered the background.
     */
    @Override
    public void onEnterBackground() {

        // Pause the track dj.
        SoundHelper.instance().pauseMusic();

        // Stop the session.
        Amplitude.endSession();
    }

    /**
     * Reports when the application
     * has exited the background.
     */
    @Override
    public void onExitBackground() {

        // Play the loud noises.
        SoundHelper.instance().resumeMusic();

        // Start user session.
        Amplitude.startSession();
    }

    // endregion
}