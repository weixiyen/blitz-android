package com.blitz.app.base.application;

import android.app.Application;

import com.blitz.app.R;

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

        // Set the regular font weight to be the default.
        CalligraphyConfig.initDefault(getResources().getString(R.string.app_font_light));
    }
}