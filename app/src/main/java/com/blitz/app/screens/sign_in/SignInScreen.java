package com.blitz.app.screens.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.object_models.ObjectModelUser;
import com.blitz.app.utilities.rest.RestAPIOperation;
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
    @InjectView(R.id.sign_in_screen_username_or_email) EditText mUsername;
    @InjectView(R.id.sign_in_screen_password)          EditText mPassword;

    // Model object.
    private ObjectModelUser mObjectModelUser;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use a vertical slide animation.
        setCustomTransitions(CustomTransition.T_SLIDE_VERTICAL);
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.sign_in_screen_sign_in) @SuppressWarnings("unused")
    public void sign_in() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        if (mObjectModelUser == null) {
            mObjectModelUser = new ObjectModelUser();
        }

        // Set desired registration fields.
        mObjectModelUser.setUsername(mUsername);
        mObjectModelUser.setPassword(mPassword);
        mObjectModelUser.signIn(this, new ObjectModelUser.CallbackSignIn() {

            @Override
            public void onSignIn() {

                // Enter main screen.
                startActivity(new Intent(SignInScreen.this, MainScreen.class), true);
            }
        });
    }
}