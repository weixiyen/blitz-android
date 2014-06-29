package com.blitz.app.base.config;

/**
 * Intended to setup configuration constants for the App.
 * Currently hard coded but eventually will pull
 * constants down from server.
 *
 * Created by Miguel Gaeta on 6/1/14.
 */
@SuppressWarnings("unused")
public class BaseConfig {

    // Should we authenticate with Facebook.
    public static final boolean AUTH_WITH_FACEBOOK = false;

    // Should we jump to an arbitrary activity on start.
    public static final Class JUMP_TO_ACTIVITY = null;

    // Endpoint URL for RESTful API.
    public static final String API_URL = "https://snapdraft.us/api";
}