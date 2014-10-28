package com.blitz.app.dialogs.loading;

import android.app.Activity;
import android.os.Handler;

import com.blitz.app.utilities.android.BaseDialog;
import com.blitz.app.utilities.blitz.BlitzDelay;

/**
 * Created by Miguel Gaeta on 6/29/14. Copyright 2014 Blitz Studios
 */
public class DialogLoading extends BaseDialog {

    // region Member Variables
    // ============================================================================================================

    // Constants.
    private static final long MINIMUM_LOADING_TIME = 1000;

    // Callback object.
    private Handler mHandler;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Setup state when constructed.
     *
     * @param activity Target activity.
     */
    public DialogLoading(Activity activity) {
        super(activity);

        setTouchable(true);
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Perform some additional hide
     * functionality for this dialog.
     */
    @Override
    public void hide(HideListener hideListener) {
        super.hide(hideListener);

        // Remove any pending callbacks.
        BlitzDelay.remove(mHandler);
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * When this dialog is shown, at first it just blocks
     * the UI.  After a period of time, it then shows a
     * loading view UI.
     */
    public void delayedShow() {
        show(false);

        // Toggle loading content if displayed for long enough.
        mHandler = BlitzDelay.postDelayed(new Runnable() {

            @Override
            public void run() {

                show(true);
            }
        }, MINIMUM_LOADING_TIME);
    }

    // endregion
}