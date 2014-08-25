package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.object_models.ObjectModelAccessQueue;
import com.blitz.app.utilities.gcm.GcmRegistrationHelper;

/**
 * Created by mrkcsc on 8/9/14. Copyright 2014 Blitz Studios
 */
public class ViewModelAccessQueue extends ViewModel {

    // region Member Variables
    // =============================================================================================

    private ObjectModelAccessQueue mModelAccessQueue;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Initialize and sync the access queue numbers.
     *
     * @param activity Target activity.
     * @param callbacks Callbacks.
     */
    @Override
    public void initialize(Activity activity, ViewModelCallbacks callbacks) {
        super.initialize(activity, callbacks);

        // Initialize access queue model.
        mModelAccessQueue = new ObjectModelAccessQueue();

        // Set the device id and sync.
        mModelAccessQueue.setDeviceId(GcmRegistrationHelper.getDeviceId(mActivity));
        mModelAccessQueue.sync(mActivity, new Runnable() {

            @Override
            public void run() {

                ViewModelAccessQueueCallbacks callbacks =
                        getCallbacks(ViewModelAccessQueueCallbacks.class);

                if (callbacks != null) {

                    // Run callbacks.
                    callbacks.onAccessGranted(mModelAccessQueue.getAccessGranted());
                    callbacks.onPeopleAhead  (mModelAccessQueue.getPeopleAhead());
                    callbacks.onPeopleBehind (mModelAccessQueue.getPeopleBehind());
                }
            }
        });
    }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface ViewModelAccessQueueCallbacks extends ViewModelCallbacks {

        public void onPeopleAhead(int peopleAhead);
        public void onPeopleBehind(int peopleBehind);
        public void onAccessGranted(boolean accessGranted);
    }

    // endregion
}