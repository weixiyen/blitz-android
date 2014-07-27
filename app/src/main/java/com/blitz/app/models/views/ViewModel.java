package com.blitz.app.models.views;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Miguel on 7/26/2014.
 */
public abstract class ViewModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Activity instance.
    protected Activity mActivity;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Sets activity.
     *
     * @param activity Activity,
     */
    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    //==============================================================================================
    // Abstract Methods
    //==============================================================================================

    /**
     * Restores state of the view model.
     *
     * @param savedInstanceState State bundle.
     */
    @SuppressWarnings("unused")
    public abstract void restoreInstanceState(Bundle savedInstanceState);

    /**
     * Saves state of the view model.
     *
     * @param savedInstanceState State bundle.
     *
     * @return State bundle.
     */
    @SuppressWarnings("unused")
    public abstract Bundle  saveInstanceState(Bundle savedInstanceState);
}