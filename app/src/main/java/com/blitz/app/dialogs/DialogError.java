package com.blitz.app.dialogs;

import android.app.Activity;

import com.blitz.app.R;
import com.blitz.app.base.dialog.BaseDialog;

import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/6/14.
 */
public class DialogError extends BaseDialog {

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Default constructor.  Sets up a dialog
     * using project defaults.
     *
     * @param activity Associated activity.
     */
    public DialogError(Activity activity) {
        super(activity);

        setTouchable(true);
        setDismissible(true);
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Dismiss error dialog on user press.
     */
    @OnClick(R.id.dialog_error_ok) @SuppressWarnings("unused")
    public void dismissDialog() {

        hide(null);
    }
}