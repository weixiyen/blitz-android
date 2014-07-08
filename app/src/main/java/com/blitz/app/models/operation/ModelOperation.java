package com.blitz.app.models.operation;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.dialogs.DialogError;
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
    private DialogError mDialogError;

    private static boolean mOperationThrottle;
    private static Handler mOperationThrottleHandler;
    private static Runnable mOperatonThrottleRunnable;

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

        // Setup operation throttling.
        setOperationThrottle();

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

    public static boolean shouldThrottle() {

        // In progress if loading or error dialog is on screen.
        return mOperationThrottle;
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Set a throttle that can be checked externally
     * and prevent operation spam.
     */
    private void setOperationThrottle() {

        // Enable the throttle.
        mOperationThrottle = true;

        // Clear any existing callbacks, and init.
        if (mOperationThrottleHandler != null) {
            mOperationThrottleHandler.removeCallbacks(mOperatonThrottleRunnable);
        } else {
            mOperationThrottleHandler = new Handler();
        }

        // Initialize the runnable callback.
        if (mOperatonThrottleRunnable == null) {
            mOperatonThrottleRunnable = new Runnable() {

                @Override
                public void run() {

                    // No longer throttling.
                    mOperationThrottle = false;
                }
            };
        }

        // Set the de-throttle callback.
        mOperationThrottleHandler.postDelayed(mOperatonThrottleRunnable, 250);
    }

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