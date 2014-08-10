package com.blitz.app.models.views;

import android.app.Activity;

import com.blitz.app.models.objects.ObjectModelAccessQueue;
import com.blitz.app.utilities.gcm.GcmRegistrationHelper;

/**
 * Created by mrkcsc on 8/9/14.
 */
public class ViewModelAccessQueue extends ViewModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    private ObjectModelAccessQueue mModelAccessQueue;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

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

                // Run callbacks.
                callbacks.onAccessGranted(mModelAccessQueue.getAccessGranted());
                callbacks.onPeopleAhead  (mModelAccessQueue.getPeopleAhead());
                callbacks.onPeopleBehind (mModelAccessQueue.getPeopleBehind());
            }
        });
    }

    //==============================================================================================
    // Callbacks Interface
    //==============================================================================================

    public interface ViewModelAccessQueueCallbacks extends ViewModelCallbacks {

        public void onPeopleAhead(int peopleAhead);
        public void onPeopleBehind(int peopleBehind);
        public void onAccessGranted(boolean accessGranted);
    }
}