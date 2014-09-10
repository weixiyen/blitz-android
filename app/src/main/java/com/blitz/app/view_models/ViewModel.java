package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Miguel on 7/26/2014. Copyright 2014 Blitz Studios
 */
public abstract class ViewModel {

    // region Member Variables
    // =============================================================================================

    // Activity instance.
    protected Activity mActivity;

    // Callbacks object.
    private ViewModelCallbacks mCallbacks;

    // endregion

    // region Constructors
    // =============================================================================================

    /**
     * Empty constructor is not allowed.
     */

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModel(Activity activity, ViewModelCallbacks callbacks) {

        mActivity  = activity;
        mCallbacks = callbacks;
    }

    // endregion

    // region Public Methods
    // =============================================================================================

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

        return savedInstanceState;
    }

    /**
     * Initialize the view model.
     */
    @SuppressWarnings("unused")
    public abstract void initialize();

    // endregion

    // region Protected Methods
    // =============================================================================================

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

    // endregion

    // region Callbacks
    // =============================================================================================

    /**
     * Base callback interface.
     */
    public interface ViewModelCallbacks {

        /**
         * This method requests an instance of the view
         * model to operate on for lifecycle callbacks.
         *
         * @return Instantiated instance of the view model
         */
        public abstract ViewModel onFetchViewModel();
    }

    // endregion
}