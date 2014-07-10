package com.blitz.app.base.application;

import android.app.Application;

import com.blitz.app.R;
import com.blitz.app.utilities.appdata.AppData;
import com.blitz.app.utilities.keyboard.KeyboardUtility;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
public class BaseApplication extends Application {

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

        // Set the regular font weight to be the default.
        CalligraphyConfig.initDefault(getResources().getString(R.string.app_font_light));
    }
}