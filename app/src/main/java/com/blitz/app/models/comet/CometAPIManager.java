package com.blitz.app.models.comet;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Pair;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miguel on 7/21/2014.
 */
public class CometAPIManager implements CometAPIWebsocket.OnMessageCallback {

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
            instance().mWebsocket = new CometAPIWebsocket(instance());
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

        // Add it.
        instance().configAddActivityOrFragment(activity);
    }

    /**
     * Add fragment to current list.
     *
     * @param fragment Target fragment.
     */
    public static void configAddFragment(Fragment fragment) {

        // Add it.
        instance().configAddActivityOrFragment(fragment);
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
    public static CometAPIChannel subscribeToChannel(String channelName) {

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
    public static void unsubscribeFromChannel(String channelName) {

        // Look for channel in active channel list.
        CometAPIChannel channel = instance().mActiveChannels.get(channelName);

        if (channel != null) {

            // Send unsubscribe message to the websocket.
            instance().mWebsocket.sendMessageToWebSocket(channel.getJsonString(false));

            // Remove from active channels.
            instance().mActiveChannels.remove(channelName);
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * When a message is received via the websocket.
     * Method cannot be accessed from outside
     * of the manager so it is private.
     *
     * TODO: The body of this should be a channel method, not
     *       only that the channel method should be merged
     *       with sending a queued message.
     *
     * @param jsonObject JSON sent.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onMessage(JsonObject jsonObject) {

        // Fetch channel message was sent to.
        String channel = jsonObject.get("channel").getAsString();

        // Try to fetch associated channel object.
        CometAPIChannel channelObject = mActiveChannels.get(channel);

        if (channelObject != null) {

            // Iterate over each callback associated with the channel.
            for (Map.Entry<String, Pair<CometAPICallback, Class>> callbackEntry
                    : channelObject.getCallbacks().entrySet()) {

                // Fetch callback object pair (receiving class, to callback).
                Pair<CometAPICallback, Class> callback = callbackEntry.getValue();

                // If we cannot find a current activity
                // or fragment that matches the callbacks
                // receiving class, we need to store it
                // to be sent later.
                boolean callbackShouldBeQueued = true;

                // Iterate over active activity and fragments.
                for (Object currentActivityOrFragment : mCurrentActivityAndFragments) {

                    // If callbackEntry receiving class matched.
                    if (callback.second.equals(currentActivityOrFragment.getClass())) {

                        // Send the callbackEntry with current activity/fragment as the receiving class.
                        callback.first.messageReceived(currentActivityOrFragment, jsonObject.getAsJsonObject("data"));

                        // Callback ran, no need to queue.
                        callbackShouldBeQueued = false;
                    }
                }

                if (callbackShouldBeQueued) {

                    // Add a queued message for this particular callback identifier.
                    channelObject.addQueuedMessage(callbackEntry.getKey(), jsonObject);
                }
            }
        }
    }

    /**
     * When an activity or fragment is added, iterate
     * over all active channels and attempt to send
     * queued messages.
     *
     * @param object Activity or fragment that
     *               just became active.
     */
    private void configAddActivityOrFragment(Object object) {

        // Add if not already present.
        if (!mCurrentActivityAndFragments.contains(object)) {
             mCurrentActivityAndFragments.add(object);

            // Iterate over active channels.
            for (CometAPIChannel channelObject : mActiveChannels.values()) {

                // Attempt to send queued messages.
                channelObject.trySendQueuedMessages(object);
            }
        }
    }

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
}