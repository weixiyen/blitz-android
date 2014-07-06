package com.blitz.app.dialogs;

import android.app.Activity;
import android.util.Log;

import com.blitz.app.R;
import com.blitz.app.base.dialog.BaseDialog;

import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class DialogLoading extends BaseDialog {

    public DialogLoading(Activity activity) {
        super(activity);

        setTouchable(true);

        // TODO: After some period of time, show a UI instead
        // of just blocking the screen.
    }

    @OnClick(R.id.popupbutton) @SuppressWarnings("unused")
    public void popupbutton() {
        Log.e("Parrot", "CLICKED");
    }
}