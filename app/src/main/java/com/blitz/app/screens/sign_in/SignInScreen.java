package com.blitz.app.screens.sign_in;

import android.content.Intent;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.android.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
public class SignInScreen extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Mapped views.
    @InjectView(R.id.sign_in_screen_username_or_email) EditText mEmailOrUsername;
    @InjectView(R.id.sign_in_screen_password)          EditText mPassword;

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.sign_in_screen_sign_in) @SuppressWarnings("unused")
    public void sign_in() {

        startActivity(new Intent(this, MainScreen.class));
    }
}