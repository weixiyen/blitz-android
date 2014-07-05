package com.blitz.app.dialogs;

import android.os.Bundle;
import android.util.Log;

import com.blitz.app.R;
import com.blitz.app.base.dialog.BaseDialog;

import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/4/14.
 */
public class DialogInfo extends BaseDialog {

    public void onCreateView(Bundle savedInstanceState) {
        setCancelable(true);


    }

    @OnClick(R.id.info_dialog_password)
    public void dfdsfds() {
        Log.e("Test", "Test 2");
    }
}