package com.blitz.app.models.comet;

import android.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Miguel on 7/21/2014.
 */
public class CometAPIChannel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Channel name.
    private String mName;

    // Channel cursor.
    private int mCursor;

    // Channel callbacks.
    private HashMap<String, Pair<CometAPICallback, Class>> mCallbacks;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Private empty constructor.
     */
    @SuppressWarnings("unused")
    private CometAPIChannel() {

        // Initialize hash map of callbacks.
        mCallbacks = new HashMap<String, Pair<CometAPICallback, Class>>();
    }

    /**
     * Initialize a channel.
     *
     * @param name Channel name.
     * @param cursor Channel cursor.
     */
     CometAPIChannel(String name, int cursor) {
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
     * Add a callback to the channel that is executed when
     * messages are received from the websocket.
     *
     * @param receivingClassObject The class object that is going to be receiving this
     *                             callback. Must be either a fragment or activity.
     *
     * @param callback The callback to be executed.  Guaranteed to execute
     *                 with a current and active instance of the receiving class
     *                 as a callback parameter.  Use this class to perform
     *                 operations if needed to avoid creating memory leaks.
     *
     * @param callbackIdentifier String to identify this callback, must be
     *                           non-null.  Used for callback removal and
     *                           to prevent duplicate callbacks.
     *
     * @param <T> Type of the receiving class.
     */
    @SuppressWarnings({"unused", "unchecked"})
    public <T> void addCallback(T receivingClassObject, CometAPICallback<T> callback, String callbackIdentifier) {

        // Fetch class from the class object provided.
        Class receivingClass = receivingClassObject.getClass();

        // Add callback using class parameter.
        addCallback(receivingClass, callback, callbackIdentifier);
    }

    /**
     * @see com.blitz.app.models.comet.CometAPIChannel#addCallback(Object, CometAPICallback, String)
     *
     * @param receivingClass Class that is going to be receiving
     *                       this callback. Must be either a fragment
     *                       or activity.
     */
    @SuppressWarnings("unused")
    public <T> void addCallback(Class<T> receivingClass, CometAPICallback<T> callback, String callbackIdentifier) {

        // TODO: Enforce supported classes.
        // TODO: Execute callbacks.

        // If identifier provided.
        if (callbackIdentifier != null) {

            // Add callback, with receiving class.
            mCallbacks.put(callbackIdentifier,
                    new Pair<CometAPICallback, Class>(callback, receivingClass));
        }

        /*
        Activity activity = instance().mCurrentActivity;

        Class c = receivingClassObject.getClass();

        Class a = activity.getClass();

        if (receivingClassObject.getClass().isAssignableFrom(activity.getClass())) {

        }

        if (c.equals(activity.getClass())) {

            callback.messageReceived((T) activity, "Test message: " + channelName);
        } else {

            callback.messageReceived(null, "Test message: " + channelName);
        }
        */
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

    //==============================================================================================
    // Private/Package Only Methods
    //==============================================================================================

    /**
     * Fetch callback.
     *
     * @param callbackIdentifier Callback identifier.
     *
     * @return Returns a pair, first item is the callback object,
     *         second object is the receiving class.
     */
    @SuppressWarnings("unused")
    Pair<CometAPICallback, Class> getCallback(String callbackIdentifier) {

        // Return associated callback.
        return mCallbacks.get(callbackIdentifier);
    }

    /**
     * Fetch all callbacks.
     *
     * @return List of callbacks, each callback is a
     *         pair of callback, receiving class objects.
     */
    @SuppressWarnings("unused")
    ArrayList<Pair<CometAPICallback, Class>> getCallbacks() {

        // Create list of callbacks.
        ArrayList<Pair<CometAPICallback, Class>> callbacks =
                new ArrayList<Pair<CometAPICallback, Class>>();

        for (Pair<CometAPICallback, Class> callback : mCallbacks.values()) {

            // Add to list of callbacks.
            callbacks.add(callback);
        }

        return callbacks;
    }

    /**
     * Get a json string of this channel to
     * send to the websocket.
     *
     * @param subscribe Subscribe or unsubscribe.
     *
     * @return Json string.
     */
    @SuppressWarnings("unused")
    String getJsonString(boolean subscribe) {

        // Initialize dictionary.
        HashMap<String, Object> jsonDictionary = new HashMap<String, Object>();

        // Place parameters into dictionary.
        jsonDictionary.put("action", subscribe ? "subscribe" : "unsubscribe");
        jsonDictionary.put("channel", mName);
        jsonDictionary.put("cursor", mCursor);

        // Convert to json string.
        return new Gson().toJsonTree(jsonDictionary).toString();
    }
}