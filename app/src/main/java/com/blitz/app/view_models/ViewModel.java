package com.blitz.app.view_models;

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

    private ViewModelCallbacks mCallbacks;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Restores state of the view model.
     *
     * @param savedInstanceState State bundle.
     */
    @SuppressWarnings("unused")
    public void restoreInstanceState(Bundle savedInstanceState) {

    }

    /**
     * Saves state of the view model.
     *
     * @param savedInstanceState State bundle.
     *
     * @return State bundle.
     */
    @SuppressWarnings("unused")
    public Bundle saveInstanceState(Bundle savedInstanceState) {

        mActivity  = null;
        mCallbacks = null;

        return savedInstanceState;
    }

    /**
     * Initialize the view model.  Setup all UI
     * here (non expensive calls please).
     */
    @SuppressWarnings("unused")
    public void initialize(Activity activity, ViewModelCallbacks callbacks) {

        mActivity  = activity;
        mCallbacks = callbacks;
    }

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * Fetch callbacks for this view model.
     *
     * @param type Callback type class.
     * @param <T> Callback type.
     *
     * @return Casted callbacks.
     */
    protected  <T extends ViewModelCallbacks> T getCallbacks(Class<T> type) {

        return type.cast(mCallbacks);
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    /**
     * Base callback interface.
     */
    public interface ViewModelCallbacks {

    }
}