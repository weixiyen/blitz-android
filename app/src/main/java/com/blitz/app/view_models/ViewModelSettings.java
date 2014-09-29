package com.blitz.app.view_models;

import android.app.Activity;

/**
 * Created by Nate on 9/28/14.
 */
public class ViewModelSettings extends ViewModel {

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelSettings(Activity activity, ViewModelCallbacks callbacks) {
        super(activity, callbacks);
    }

    @Override
    public void initialize() {

        ViewModelSettingsCallbacks callbacks = getCallbacks(ViewModelSettingsCallbacks.class);
        callbacks.onEmail("spiffy1911@gmail.com");

    }

    public interface ViewModelSettingsCallbacks extends ViewModelCallbacks {
        public void onEmail(String email);
    }
}
