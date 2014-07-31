package com.blitz.app.screens.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.models.objects.ObjectModelUser;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.android.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
public class SignUpScreen extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Mapped views.
    @InjectView(R.id.sign_up_screen_email)    EditText mEmail;
    @InjectView(R.id.sign_up_screen_username) EditText mUsername;
    @InjectView(R.id.sign_up_screen_password) EditText mPassword;

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

    /**
     * Register the user.
     */
    @OnClick(R.id.sign_up_screen_sign_up) @SuppressWarnings("unused")
    public void sign_up() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        if (mObjectModelUser == null) {
            mObjectModelUser = new ObjectModelUser();
        }

        // Set desired registration fields.
        mObjectModelUser.setEmail(mEmail);
        mObjectModelUser.setUsername(mUsername);
        mObjectModelUser.setPassword(mPassword);
        mObjectModelUser.signUp(this, new ObjectModelUser.CallbackSignUp() {

            @Override
            public void onSignUp() {

                // Enter main screen.
                startActivity(new Intent(SignUpScreen.this, MainScreen.class), true);
            }
        });
    }
}