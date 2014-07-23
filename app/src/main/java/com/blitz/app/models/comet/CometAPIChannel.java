package com.blitz.app.models.comet;

import android.util.Pair;

import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // Channel callbacks and queued messages.
    // TODO: Use a single map, use custom class, not a pair.
    private HashMap<String, Pair<CometAPICallback, Class>> mCallbacks;
    private HashMap<String, ArrayList<JsonObject>> mCallbacksQueuedMessages;

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

        // Initialize array of queued messages.
        mCallbacksQueuedMessages = new HashMap<String, ArrayList<JsonObject>>();
    }

    /**
     * Initialize a channel.
     *
     * @param name Channel name.
     * @param cursor Channel cursor.
     */
     CometAPIChannel(String name, int cursor) {
        this();

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

        // Verify receiving class is supported.
        if (!BaseActivity.class.isAssignableFrom(receivingClass) &&
            !BaseFragment.class.isAssignableFrom(receivingClass)) {

            // Throw exception.
            throw new RuntimeException("Receiving class must be a fragment or activity.");
        }

        // If identifier provided.
        if (callbackIdentifier != null) {

            // Add callback, with receiving class.
            mCallbacks.put(callbackIdentifier,
                    new Pair<CometAPICallback, Class>(callback, receivingClass));
        }
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

        // Also remove queued messages.
        mCallbacksQueuedMessages.remove(callbackIdentifier);
    }

    /**
     * Remove all associated callbacks.
     */
    @SuppressWarnings("unused")
    public void removeCallbacks() {

        // Clear callbacks.
        mCallbacks.clear();

        // And queued messages.
        mCallbacksQueuedMessages.clear();
    }

    //==============================================================================================
    // Private/Package Only Methods
    //==============================================================================================

    /**
     * Add a message to the queue.  Each message is
     * associated with a particular callback.
     *
     * @param callbackIdentifier Callback identifier.
     *
     * @param message Message to queue.
     */
    void addQueuedMessage(String callbackIdentifier, JsonObject message) {

        // Fetch queued messages for this callback identifier.
        ArrayList<JsonObject> queuedMessages = mCallbacksQueuedMessages.get(callbackIdentifier);

        // Initialize if needed.
        if (queuedMessages == null) {
            queuedMessages = new ArrayList<JsonObject>();
        }

        // Add to queue.
        queuedMessages.add(message);

        // Insert into dictionary.
        mCallbacksQueuedMessages.put(callbackIdentifier, queuedMessages);
    }

    /**
     * Return all callbacks, mapped
     * by their identifiers.
     *
     * @return All callbacks.
     */
    @SuppressWarnings("unused")
    HashMap<String, Pair<CometAPICallback, Class>> getCallbacks() {

        return mCallbacks;
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

    /**
     * Try to send off queued messages, when
     * an activity or fragment becomes active.
     *
     * @param activityOrFragment Activity or fragment.
     */
    @SuppressWarnings("unchecked")
    void trySendQueuedMessages(Object activityOrFragment) {

        // Iterate over each callback associated with the channel.
        for (Map.Entry<String, Pair<CometAPICallback, Class>> callbackEntry : mCallbacks.entrySet()) {

            // Fetch callback activityOrFragment pair (receiving class, to callback).
            Pair<CometAPICallback, Class> callback = callbackEntry.getValue();

            // If callbackEntry receiving class matched.
            if (callback.second.equals(activityOrFragment.getClass())) {

                // Attempt to fetch queued json objects.
                ArrayList<JsonObject> jsonObjects = mCallbacksQueuedMessages.get(callbackEntry.getKey());

                if (jsonObjects != null) {

                    // Iterate over queued messages.
                    for (JsonObject jsonObject :jsonObjects) {

                        // Send the callbackEntry with current activity/fragment as the receiving class.
                        callback.first.messageReceived(activityOrFragment, jsonObject.getAsJsonObject("data"));
                    }

                    // Remove message for key from queue.
                    mCallbacksQueuedMessages.remove(callbackEntry.getKey());
                }
            }
        }
    }
}