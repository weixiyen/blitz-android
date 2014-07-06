package com.blitz.app.models.operation;

import android.app.Activity;
import android.util.Log;

import com.blitz.app.dialogs.DialogLoading;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public abstract class ModelOperation implements ModelOperationInterface {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Parent activity.
    private Activity mActivity;

    // Loading dialog.
    private DialogLoading mDialogLoading;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Empty constructor not allowed (for now).
     */
    @SuppressWarnings("unused")
    private ModelOperation() {

    }

    /**
     * Provide an activity so we can create
     * shared loading/error dialogs.
     *
     * @param activity Target activity.
     */
    public ModelOperation(Activity activity) {
        mActivity = activity;
    }

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Triggered when a model operation begins.
     */
    @Override
    public void start() {

        // Show loading dialog.
        getDialogLoading().show();
    }

    /**
     * Triggered when a model operation finishes.
     */
    @Override
    public void finish() {

        // Hide loading dialog.
        getDialogLoading().hide();
    }

    /**
     * Triggered when a model operation is successful.
     */
    @Override
    public abstract void success();

    /**
     * Triggered when a model operation fails.
     */
    @Override
    public void failure() {

        // TODO: On failure, show error dialog.
        Log.e("ModelOperation", "Operation failure.");
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Lazy load the loading dialog.
     *
     * @return Loading dialog.
     */
    private DialogLoading getDialogLoading() {
        if (mDialogLoading == null) {
            mDialogLoading = new DialogLoading(mActivity);
        }

        return mDialogLoading;
    }
}