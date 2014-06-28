package com.blitz.app.screens.main;

import android.content.Intent;

import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.screens.sign_up.SignUpScreen;

public class MainScreen extends BaseActivity {

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Start next activity in the flow.
     */
    public void transitionNextScreen() {

        // Create intent for main screen activity.
        Intent intent = new Intent(this, SignUpScreen.class);

        startActivity(intent);
    }
}