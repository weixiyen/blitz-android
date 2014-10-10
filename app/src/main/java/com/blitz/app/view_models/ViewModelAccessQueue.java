package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.rest_models.RestModelAccessQueue;
import com.blitz.app.utilities.gcm.GcmRegistrationHelper;

/**
 * Created by mrkcsc on 8/9/14. Copyright 2014 Blitz Studios
 */
public class ViewModelAccessQueue extends ViewModel {

    // region Member Variables
    // =============================================================================================

    private RestModelAccessQueue mModelAccessQueue;

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelAccessQueue(Activity activity, ViewModelCallbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Initialize and sync the access queue numbers.
     */
    @Override
    public void initialize() {

        // Initialize access queue model.
        mModelAccessQueue = new RestModelAccessQueue();

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