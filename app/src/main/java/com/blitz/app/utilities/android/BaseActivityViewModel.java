package com.blitz.app.utilities.android;

import android.os.Bundle;

import com.blitz.app.view_models.ViewModel;

/**
 * Created by mrkcsc on 8/24/14. Copyright 2014 Blitz Studios
 */
public abstract class BaseActivityViewModel extends BaseActivity {

    // region Overwritten Methods
    //==============================================================================================

    /**
     * Restore the instance state of the model.
     *
     * @param savedInstanceState Saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore view model state.
        if (onFetchViewModel() != null) {
            onFetchViewModel().restoreInstanceState(savedInstanceState);
        }
    }

    /**
     * Initialize the view model when activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Initialize view model.
        if (onFetchViewModel() != null) {
            onFetchViewModel().initialize(this, this);
        }
    }

    /**
     * Save the instance state of the model.
     *
     * @param outState State values.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save view model state.
        if (onFetchViewModel() != null) {
            onFetchViewModel().saveInstanceState(outState);
        }
    }

    // endregion

    // region Abstract Methods
    //==============================================================================================

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    public abstract ViewModel onFetchViewModel();

    // endregion
}