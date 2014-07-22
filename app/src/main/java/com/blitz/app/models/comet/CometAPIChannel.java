package com.blitz.app.models.comet;

import android.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Miguel on 7/21/2014.
 */
class CometAPIChannel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Channel name.
    private String mName;

    // Channel cursor.
    private int mCursor;

    // Channel callbacks.
    private HashMap<String, Pair<CometAPIManager.CallbackChannel, Boolean>> mCallbacks;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Private empty constructor.
     */
    @SuppressWarnings("unused")
    private CometAPIChannel() {

        // Initialize hash map of callbacks.
        mCallbacks = new HashMap<String, Pair<CometAPIManager.CallbackChannel, Boolean>>();
    }

    /**
     * Public channel initialized.
     *
     * @param name Channel name.
     * @param cursor Channel cursor.
     */
    public CometAPIChannel(String name, int cursor) {
        super();

        // Set channel name.
        mName = name;

        // Set channel cursor.
        mCursor = cursor;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Get a json string of this channel to
     * send to the websocket.
     *
     * @param subscribe Subscribe or unsubscribe.
     *
     * @return Json string.
     */
    @SuppressWarnings("unused")
    public String getJsonString(boolean subscribe) {

        // Initialize dictionary.
        HashMap<String, Object> jsonDictionary = new HashMap<String, Object>();

        // Place parameters into dictionary.
        jsonDictionary.put("action", subscribe ? "subscribe" : "unsubscribe");
        jsonDictionary.put("channel", mName);
        jsonDictionary.put("cursor", mCursor);

        // Convert to json string.
        return new Gson().toJsonTree(jsonDictionary).toString();
    }

    /**
     * Add a callback to this channel.
     *
     * @param callback Callback.
     * @param callbackIdentifier Callback identifier.
     * @param global Is callback global.
     */
    @SuppressWarnings("unused")
    public void addCallback(CometAPIManager.CallbackChannel callback, String callbackIdentifier, boolean global) {

        // Random identifier if null.
        if (callbackIdentifier == null) {
            callbackIdentifier = UUID.randomUUID().toString();
        }

        // Add callback.
        mCallbacks.put(callbackIdentifier,
                new Pair<CometAPIManager.CallbackChannel, Boolean>(callback, global));
    }

    /**
     * Remove callback with specified identifier.
     *
     * @param callbackIdentifier Callback identifier.
     */
    @SuppressWarnings("unused")
    public void removeCallback(String callbackIdentifier) {

        // Remove requested callbacks.
        mCallbacks.remove(callbackIdentifier);
    }

    /**
     * Remove all associated callbacks.
     */
    @SuppressWarnings("unused")
    public void removeCallbacks() {

        // Clear callbacks.
        mCallbacks.clear();
    }

    /**
     * Fetch a specified callback by identifier.
     *
     * @param callbackIdentifier Callback identifier.
     *
     * @return Callback, or null if not found.
     */
    @SuppressWarnings("unused")
    public CometAPIManager.CallbackChannel getCallback(String callbackIdentifier) {

        // Fetch requested callback.
        Pair<CometAPIManager.CallbackChannel, Boolean> callback =
                mCallbacks.get(callbackIdentifier);

        // Return callback if it exists.
        return callback == null ? null : callback.first;
    }

    /**
     * Fetch list of associated callbacks.
     *
     * @param global Fetch global or non global.
     *
     * @return List of callbacks.
     */
    @SuppressWarnings("unused")
    public ArrayList<CometAPIManager.CallbackChannel> getCallbacks(boolean global) {

        // Create list of callbacks.
        ArrayList<CometAPIManager.CallbackChannel> callbacks =
                new ArrayList<CometAPIManager.CallbackChannel>();

        for (Pair<CometAPIManager.CallbackChannel, Boolean> callback : mCallbacks.values()) {

            // If matches global param.
            if (callback.second == global) {

                // Add to list of callbacks.
                callbacks.add(callback.first);
            }
        }

        return callbacks;
    }
}