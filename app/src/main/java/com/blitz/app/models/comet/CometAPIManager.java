package com.blitz.app.models.comet;

import android.app.Activity;

/**
 * Created by Miguel on 7/21/2014.
 */
public class CometAPIManager {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Current activity being displayed,
    // can be null if app not active.
    private Activity mCurrentActivity;

    public static void setChannelSubscribed(String channel) {

    }

    public static void unsubscribeFromChannel(String channel) {

    }

    /**
     * Add a callback to receive messages for a specified channel.
     * Assumes user is already subscribed to this channel.
     *
     * @param channel Channel to attach callback to.
     *
     * @param global Is this a global callback.  Global callbacks will NOT
     *               be cleaned up across activities, so make sure anything
     *               in a global callback will not cause a memory leak.
     *
     * @param callback Callback.
     */
    public static void addChannelCallback(String channel, boolean global, CallbackChannel callback) {

        // Callbacks dictionaries.
    }

    public interface CallbackChannel {

        public void messageReceived(Activity activity);
    }

    private static CometAPIManager instance() {

        return null;
    }

    //==============================================================================================
    // Configuration
    //==============================================================================================

    // Configuration object.
    private Config mConfig;

    /**
     * Fetch config object.
     *
     * @return Config obkect.
     */
    public static Config config() {

        // Fetch from singleton.
        return instance().getConfig();
    }

    private Config getConfig() {
        if (mConfig == null) {
            mConfig = new Config();
        }

        return mConfig;
    }

    public class Config {

        private Config() {  }

        public void setCurrentActivity(Activity activity) {

            // If activity = null, clear all non-global callbacks.

            // Update current activity.
            mCurrentActivity = activity;
        }
    }
}