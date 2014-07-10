package com.blitz.app.screens.sign_up;

import android.content.Intent;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.screens.main.MainScreen;

import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
public class SignUpScreen extends BaseActivity {

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.sign_up_screen_sign_up) @SuppressWarnings("unused")
    public void sign_up() {

        startActivity(new Intent(this, MainScreen.class));
    }
}