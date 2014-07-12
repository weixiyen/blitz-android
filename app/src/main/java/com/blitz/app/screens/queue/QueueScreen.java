package com.blitz.app.screens.queue;

import android.content.Intent;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.screens.access_code.AccessCodeScreen;
import com.blitz.app.screens.sign_in.SignInScreen;

import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class QueueScreen extends BaseActivity {

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Transition to access code screen.
     */
    @OnClick(R.id.queue_screen_have_code) @SuppressWarnings("unused")
    public void haveCode() {

        // Transition to access code screen.
        startActivity(new Intent(this, AccessCodeScreen.class));
    }

    /**
     * Transition to sign in screen.
     */
    @OnClick(R.id.queue_screen_have_account) @SuppressWarnings("unused")
    public void haveAccount() {

        // Transition to sign in screen.
        startActivity(new Intent(this, SignInScreen.class));
    }
}