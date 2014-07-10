package com.blitz.app.utilities.appconfig;

/**
 * Intended to setup configuration constants for the App.
 * Currently hard coded but eventually will pull
 * constants down from server.
 *
 * Created by Miguel Gaeta on 6/1/14.
 */
@SuppressWarnings("unused")
public class AppConfig {

    // Should we authenticate with Facebook.
    public static final boolean AUTH_WITH_FACEBOOK = false;

    // Should we jump to an arbitrary activity on start.
    public static final Class JUMP_TO_ACTIVITY = null;

    // Endpoint URL for RESTful API.
    public static final String API_URL = "https://snapdraft.us/api";

    // Should we clear app data on launch.
    public static final boolean CLEAR_APP_DATA_ON_LAUNCH = false;

    // Should we have rest debugging.
    public static final boolean ENABLE_REST_DEBUGGING = false;
}