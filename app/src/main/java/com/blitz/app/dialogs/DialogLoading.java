package com.blitz.app.dialogs;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.utilities.android.BaseDialog;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class DialogLoading extends BaseDialog {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Constants.
    private static final long MINIMUM_LOADING_TIME = 1000;

    // Callback objects.
    private Runnable mRunnable;
    private Handler mHandler;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Setup state when constructed.
     *
     * @param activity Target activity.
     */
    public DialogLoading(Activity activity) {
        super(activity);

        setTouchable(true);
    }

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Perform some additional hide
     * functionality for this dialog.
     */
    @Override
    public void hide(HideListener hideListener) {
        super.hide(hideListener);

        // Remove any pending callbacks.
        mHandler.removeCallbacks(mRunnable);
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * When this dialog is shown, at first it just blocks
     * the UI.  After a period of time, it then shows a
     * loading view UI.
     */
    public void delayedShow() {
        show(false);

        mRunnable = new Runnable() {
            @Override
            public void run() {

                show(true);
            }
        };

        mHandler = new Handler();

        // Toggle loading view if displayed for long enough.
        mHandler.postDelayed(mRunnable, MINIMUM_LOADING_TIME);
    }
}