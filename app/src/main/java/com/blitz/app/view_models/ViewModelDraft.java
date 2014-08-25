package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Handler;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Dummy call to drafting started.
                getCallbacks(ViewModelDraftCallbacks.class).onDraftingStarted();
            }
        }, 2000);
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