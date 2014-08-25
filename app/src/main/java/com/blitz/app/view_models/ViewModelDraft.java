package com.blitz.app.view_models;

import android.app.Activity;

/**
 * Created by mrkcsc on 8/24/14. Copyright 2014 Blitz Studios
 */
public class ViewModelDraft extends ViewModel {

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Initialize the view model.
     */
    @Override
    public void initialize(Activity activity, ViewModelCallbacks callbacks) {
        super.initialize(activity, callbacks);
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    public void startDrafting() {

        // Dummy call to drafting started.
        getCallbacks(ViewModelDraftCallbacks.class).onDraftingStarted();
    }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    /**
     * Drafting related callbacks.
     */
    public interface ViewModelDraftCallbacks extends ViewModelCallbacks {

        public void onDraftingStarted();
    }

    // endregion
}