package com.blitz.app.models.comet;

import android.app.Activity;

/**
 * Created by Miguel on 7/21/2014.
 */
public class CometAPIManager {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Instance object.
    private static CometAPIManager mInstance;

    // Websocket used for comet calls.
    private CometAPIWebsocket mWebsocket;

    // Current activity being displayed,
    // can be null if app not active.
    private Activity mCurrentActivity;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Initializes the manager.
     */
    public static void init() {

        // Initialize a web socket.
        if (instance().mWebsocket == null) {
            instance().mWebsocket = new CometAPIWebsocket();
        }

        // Open the web socket.
        instance().mWebsocket.openWebSocket();
    }

    public void setCurrentActivity(Activity activity) {

        // TODO: If activity = null, clear all non-global callbacks.

        // Update current activity.
        mCurrentActivity = activity;
    }

    public static void setChannelSubscribed(String channel) {

    }

    public static void setChannelUnsubscribed(String channel) {

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

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Instance method.
     *
     * @return Manager singleton.
     */
    private static CometAPIManager instance() {

        if (mInstance == null) {
            synchronized (CometAPIManager.class) {
                if (mInstance == null) {
                    mInstance = new CometAPIManager();
                }
            }
        }

        return mInstance;
    }

    //==============================================================================================
    // Interface
    //==============================================================================================

    public interface CallbackChannel {

        public void messageReceived(Activity activity);
    }
}