package com.blitz.app.utilities.app;

/**
 * Intended to setup configuration constants for the App.
 * Currently hard coded but eventually will pull
 * constants down from server.
 *
 * Created by Miguel Gaeta on 6/1/14.
 */
@SuppressWarnings("unused")
public class AppConfig {

    // Production flag.
    public static final boolean IS_PRODUCTION = false;

    // Should we authenticate with Facebook.
    public static final boolean AUTH_WITH_FACEBOOK = false;

    // Should we jump to an arbitrary activity on start.
    public static final Class JUMP_TO_ACTIVITY = null;

    // Endpoint URL for RESTful API.
    public static final String API_URL = "https://snapdraft.us/api";

    // Endpoint URL for Comet Server.
    public static final String WEBSOCKET_URL = "wss://snapdraft.us/comet/u/";

    // Should we clear app data on launch.
    public static final boolean CLEAR_APP_DATA_ON_LAUNCH = true;

    // Should we have rest debugging.
    public static final boolean ENABLE_REST_DEBUGGING = false;

    // Allow landscape for debugging purposes.
    public static final boolean PORTRAIT_ONLY = true;
}