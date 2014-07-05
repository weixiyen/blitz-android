package com.blitz.app.dialogs;

import android.app.Activity;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class DialogLoading extends BlitzDialog {

    public DialogLoading(Activity activity) {
        super(activity);

        setTouchable(true);
    }
}