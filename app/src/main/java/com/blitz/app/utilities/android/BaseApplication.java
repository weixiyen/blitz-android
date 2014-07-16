package com.blitz.app.utilities.android;

import android.app.Application;

import com.blitz.app.R;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.background.EnteredBackground;
import com.blitz.app.utilities.background.EnteredBackgroundInterface;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.sound.SoundHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
public class BaseApplication extends Application implements EnteredBackgroundInterface {

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Initialize custom font on application start.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize app-data.
        AppData.init(this);

        // Initialize the keyboard utility.
        KeyboardUtility.init(this);

        // Initialize sound helper.
        SoundHelper.instance().init(this);

        // Initialize background timer.
        EnteredBackground.init(this);

        // Set the regular font weight to be the default.
        CalligraphyConfig.initDefault(getResources().getString(R.string.app_font_light));
    }

    @Override
    public void onEnterBackground() {
        SoundHelper.instance().pauseMusic();
    }

    @Override
    public void onExitBackground() {
        SoundHelper.instance().resumeMusic();
    }
}