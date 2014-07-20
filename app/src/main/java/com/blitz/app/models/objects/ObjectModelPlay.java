package com.blitz.app.models.objects;

import android.app.Activity;

/**
 * Created by mrkcsc on 7/20/14.
 */
public class ObjectModelPlay {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Required models.
    private ObjectModelPreferences mModelPreferences;
    private ObjectModelQueue       mModelQueue;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Initialize models needed for this one.
     */
    public ObjectModelPlay() {
        mModelPreferences = new ObjectModelPreferences();
        mModelQueue       = new ObjectModelQueue();
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * User wants to play, enter them into the queue.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void enterQueue(final Activity activity, final EnterQueueCallback callback) {

        // Now sync user preferences.
        mModelPreferences.Sync(activity, new ObjectModelPreferences.SyncCallback() {

            @Override
            public void onSync() {

                // Set draft key to the active queue.
                mModelQueue.setDraftKey(mModelPreferences.currentActiveQueue());

                // Queue up for the draft!
                mModelQueue.queueUp(activity, new ObjectModelQueue.QueueUpCallback() {

                    @Override
                    public void onQueueUp() {

                        // Now in queue.
                        callback.onEnterQueue();
                    }
                });
            }
        });
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    public interface EnterQueueCallback {

        public void onEnterQueue();
    }
}