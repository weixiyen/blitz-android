package com.blitz.app.screens.sign_up;

import android.content.Intent;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.models.objects.ObjectModelUser;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.screens.main.MainScreen;

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
        mObjectModelUser.signUpSetInfo(mEmail, mUsername, mPassword);
        mObjectModelUser.signUp(new RestAPIOperation(this) {

            @Override
            public void success() {

                mObjectModelUser.persistUserInfo();

                startActivity(new Intent(SignUpScreen.this, MainScreen.class));
            }
        });
    }
}