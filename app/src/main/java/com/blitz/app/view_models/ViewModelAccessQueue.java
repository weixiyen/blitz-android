package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelAccessQueue;
import com.blitz.app.rest_models.RestResult;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.gcm.GcmRegistrationHelper;

/**
 * Created by mrkcsc on 8/9/14. Copyright 2014 Blitz Studios
 */
public class ViewModelAccessQueue extends ViewModel {

    // region Constructor
    // ============================================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelAccessQueue(BaseActivity activity, ViewModel.Callbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Initialize and sync the access queue numbers.
     */
    @Override
    public void initialize() {

        // Sync access queue state.
        RestModelAccessQueue.sync(mActivity, GcmRegistrationHelper.getDeviceId(mActivity),
                new RestResult<RestModelAccessQueue>() {

            @Override
            public void onSuccess(RestModelAccessQueue object) {

                Callbacks callbacks = getCallbacks(Callbacks.class);

                if (callbacks != null) {

                    // Run callbacks.
                    callbacks.onAccessGranted(object.isAccessGranted());
                    callbacks.onPeopleAhead  (object.getPeopleAhead());
                    callbacks.onPeopleBehind (object.getPeopleBehind());
                }
            }
        });
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onPeopleAhead(int peopleAhead);
        public void onPeopleBehind(int peopleBehind);
        public void onAccessGranted(boolean accessGranted);
    }

    // endregion
}