package com.blitz.app.view_models;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by mrkcsc on 8/24/14. Copyright 2014 Blitz Studios
 */
public class ViewModelDraft extends ViewModel {

    // region Constructor
    // =============================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelDraft(Activity activity, ViewModelCallbacks callbacks) {
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