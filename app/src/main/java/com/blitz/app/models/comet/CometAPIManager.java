package com.blitz.app.models.comet;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
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

    // Current activities, fragments displayed,
    // can be null if app not active.
    private ArrayList<Object> mCurrentActivityAndFragments;

    // Active channels user is subscribed to.
    private HashMap<String, CometAPIChannel> mActiveChannels;

    //==============================================================================================
    // Public Methods - Config
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

        // Initialize activities and fragments.
        if (instance().mCurrentActivityAndFragments == null) {
            instance().mCurrentActivityAndFragments = new ArrayList<Object>();
        }

        // Open the web socket.
        instance().mWebsocket.openWebSocket();
    }

    /**
     * Add activity to current list.
     *
     * @param activity Target activity.
     */
    public static void configAddActivity(Activity activity) {

        // Add if not already present.
        if (!instance().mCurrentActivityAndFragments.contains(activity)) {
             instance().mCurrentActivityAndFragments.add(activity);
        }
    }

    /**
     * Remove activity from current list.
     *
     * @param activity Target activity.
     */
    public static void configRemoveActivity(Activity activity) {

        // Remove if present.
        if (instance().mCurrentActivityAndFragments.contains(activity)) {
            instance().mCurrentActivityAndFragments.remove(activity);
        }
    }

    /**
     * Add fragment to current list.
     *
     * @param fragment Target fragment.
     */
    public static void configAddFragment(Fragment fragment) {

        // Add if not already present.
        if (!instance().mCurrentActivityAndFragments.contains(fragment)) {
             instance().mCurrentActivityAndFragments.add(fragment);
        }
    }

    /**
     * Remove fragment from current list.
     *
     * @param fragment Target fragment.
     */
    public static void configRemoveFragment(Fragment fragment) {

        // Remove if present.
        if (instance().mCurrentActivityAndFragments.contains(fragment)) {
            instance().mCurrentActivityAndFragments.remove(fragment);
        }
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Given a channel name, subscribe to that
     * channel if not already subscribed.
     *
     * @param channelName Specified channel.
     *
     * @return Subscribed channel object, guaranteed to not be null.
     */
    @SuppressWarnings("unused")
    public static CometAPIChannel setChannelSubscribed(String channelName) {

        // Look for channel in active channel list.
        CometAPIChannel channel = instance().mActiveChannels.get(channelName);

        if (channel == null) {
            channel = new CometAPIChannel(channelName, -1);

            // Create and add channel if not already subscribed.
            instance().mActiveChannels.put(channelName, channel);
        }

        // Send subscribe message to the websocket.
        instance().mWebsocket.sendMessageToWebSocket(channel.getJsonString(true));

        // Return channel.
        return channel;
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

    public static <T> void addChannelCallback(T receivingClass, CometAPICallback<T> callback, String channelName) {


        /*
        Activity activity = instance().mCurrentActivity;

        Class c = receivingClass.getClass();

        Class a = activity.getClass();

        if (receivingClass.getClass().isAssignableFrom(activity.getClass())) {

        }

        if (c.equals(activity.getClass())) {

            callback.messageReceived((T) activity, "Test message: " + channelName);
        } else {

            callback.messageReceived(null, "Test message: " + channelName);
        }
        */
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
     *               Local callbacks will automatically be cleared when
     *               an activity or fragment resumes in order to help guard
     *               against memory leaks.  It is up to the user to make
     *               sure to re-initialize them.
     *
     * @param callback Callback.
     * @param callbackIdentifier Callback identifier, used to remove callback.
     */
    @SuppressWarnings("unused")
    private static void addChannelCallback(String channelName, boolean global, CallbackChannel callback, String callbackIdentifier) {

        // Look for channel in active channel list.
        CometAPIChannel channel = instance().mActiveChannels.get(channelName);

        // Add callback.
        if (channel != null) {
            channel.addCallback(callback, callbackIdentifier);
        }
    }

    /**
     * Add channel shorthand.
     */
    @SuppressWarnings("unused")
    private static void addChannelCallback(String channelName, boolean global, CallbackChannel callback) {

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
    private static void removeChannelCallback(String channelName, String callbackIdentifier) {

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

                    // Init.
                    init();
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