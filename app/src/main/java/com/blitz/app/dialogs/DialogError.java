package com.blitz.app.dialogs;

import android.app.Activity;

import com.blitz.app.base.dialog.BaseDialog;

/**
 * Created by mrkcsc on 7/6/14.
 */
public class DialogError extends BaseDialog {

    /**
     * Default constructor.  Sets up a dialog
     * using project defaults.
     *
     * @param activity Associated activity.
     */
    public DialogError(Activity activity) {
        super(activity);

        setTouchable(true);
    }
}