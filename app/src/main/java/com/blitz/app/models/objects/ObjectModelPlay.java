package com.blitz.app.models.objects;

import android.app.Activity;

import com.blitz.app.utilities.logging.LogHelper;

/**
 * Created by mrkcsc on 7/20/14.
 */
public class ObjectModelPlay {

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    public void enterQueue(Activity activity, EnterQueueCallback callback) {

        // Create a preferences object.
        ObjectModelPreferences modelPreferences = new ObjectModelPreferences();

        // Now sync user preferences.
        modelPreferences.Sync(activity, new ObjectModelPreferences.SyncCallback() {

            @Override
            public void onSync() {

                LogHelper.log("Synced!");
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