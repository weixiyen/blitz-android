package com.blitz.app.screens.access_code;

import android.content.Intent;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.screens.sign_up.SignUpScreen;

import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class AccessCodeScreen extends BaseActivity {

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Transition to sign up screen.
     */
    @OnClick(R.id.access_code_screen_continue_with_code) @SuppressWarnings("unused")
    public void haveCode() {

        // Transition to access code screen.
        startActivity(new Intent(this, SignUpScreen.class));
    }
}