package com.blitz.app.models.operation;

import android.app.Activity;

import com.blitz.app.dialogs.DialogLoading;
import com.blitz.app.dialogs.DialogError;


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
    private DialogError mDialogError;

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
        getDialogLoading().delayedShow();
    }

    /**
     * Finish helper method that handles
     * finishing the operation in sync with
     * any dialogs/running UI events, etc.
     *
     * @param success Operation status.
     */
    @Override
    public void finish(final boolean success) {

        // Hide loading dialog.
        getDialogLoading().hide(new DialogLoading.HideListener() {

            @Override
            public void didHide() {

                if (success) {
                    success();
                } else {
                    failure();
                }
            }
        });
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Triggered when a model operation is successful.
     */
    public abstract void success();

    /**
     * Triggered when a model operation fails.
     */
    public void failure() {

        // Show the error dialog.
        getDialogError().show();
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Lazy load the error dialog.
     *
     * @return Error dialog.
     */
    private DialogError getDialogError() {
        if (mDialogError == null) {
            mDialogError = new DialogError(mActivity);
        }

        return mDialogError;
    }

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