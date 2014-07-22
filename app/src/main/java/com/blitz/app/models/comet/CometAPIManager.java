package com.blitz.app.models.comet;

import android.app.Activity;

import java.util.HashMap;

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

    // Active channels user is subscribed to.
    private HashMap<String, CometAPIChannel> mActiveChannels;

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

        // Initialize active channels map.
        if (instance().mActiveChannels == null) {
            instance().mActiveChannels = new HashMap<String, CometAPIChannel>();
        }

        // Open the web socket.
        instance().mWebsocket.openWebSocket();
    }

    public void setCurrentActivity(Activity activity) {

        // TODO: If activity = null, clear all non-global callbacks.

        // Update current activity.
        mCurrentActivity = activity;
    }

    /**
     * Given a channel name, subscribe to that
     * channel if not already subscribed.
     *
     * @param channelName Specified channel.
     */
    @SuppressWarnings("unused")
    public static void setChannelSubscribed(String channelName) {

        // Look for channel in active channel list.
        CometAPIChannel channel = instance().mActiveChannels.get(channelName);

        if (channel == null) {
            channel = new CometAPIChannel(channelName, -1);

            // Create and add channel if not already subscribed.
            instance().mActiveChannels.put(channelName, channel);
        }

        // Send subscribe message to the websocket.
        instance().mWebsocket.sendMessageToWebSocket(channel.getJsonString(true));
    }

    /**
     * Given a channel name, unsubscribe to
     * that channel if not already unsubscribed.
     *
     * @param channelName Specified channel.
     */
    @SuppressWarnings("unused")
    public static void setChannelUnsubscribed(String channelName) {

        // Look for channel in active channel list.
        CometAPIChannel channel = instance().mActiveChannels.get(channelName);

        if (channel != null) {

            // Send unsubscribe message to the websocket.
            instance().mWebsocket.sendMessageToWebSocket(channel.getJsonString(false));

            // Remove from active channels.
            instance().mActiveChannels.remove(channelName);
        }
    }

    /**
     * Add a callback to receive messages for a specified channel.
     * Assumes user is already subscribed to this channel.
     *
     * @param channelName Channel to attach callback to.
     *
     * @param global Is this a global callback.  Global callbacks will NOT
     *               be cleaned up across activities, so make sure anything
     *               in a global callback will not cause a memory leak.
     *
     * @param callback Callback.
     * @param callbackIdentifier Callback identifier, used to remove callback.
     */
    @SuppressWarnings("unused")
    public static void addChannelCallback(String channelName, boolean global, CallbackChannel callback, String callbackIdentifier) {

        // Look for channel in active channel list.
        CometAPIChannel channel = instance().mActiveChannels.get(channelName);

        // Add callback.
        if (channel != null) {
            channel.addCallback(callback, callbackIdentifier, global);
        }
    }

    /**
     * Add channel shorthand.
     */
    @SuppressWarnings("unused")
    public static void addChannelCallback(String channelName, boolean global, CallbackChannel callback) {

        // Call with no identifier specified.
        addChannelCallback(channelName, global, callback, null);
    }

    /**
     * Remove channel callback.
     *
     * @param channelName Channel name.
     * @param callbackIdentifier Callback identifier, used to remove callback.
     */
    @SuppressWarnings("unused")
    public static void removeChannelCallback(String channelName, String callbackIdentifier) {

        // Look for channel in active channel list.
        CometAPIChannel channel = instance().mActiveChannels.get(channelName);

        // Remove callback.
        if (channel != null) {
            channel.removeCallback(callbackIdentifier);
        }
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