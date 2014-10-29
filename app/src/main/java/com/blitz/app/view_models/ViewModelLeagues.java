package com.blitz.app.view_models;

import com.blitz.app.utilities.android.BaseActivity;

/**
 * Created by mrkcsc on 10/28/14. Copyright 2014 Blitz Studios
 */
public class ViewModelLeagues extends ViewModel {

    // region Constructor
    // =============================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelLeagues(BaseActivity activity, Callbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Initialize the view model.
     */
    @Override
    public void initialize() {

    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

    }

    // endregion
}