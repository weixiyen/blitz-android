package com.blitz.app.screens.sign_in;

import android.os.Bundle;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/1/14. Copyright 2014 Blitz Studios
 */
public class SignInScreen extends BaseActivity {

    // region Member Variables
    // =============================================================================================

    // Mapped views.
    @InjectView(R.id.sign_in_screen_username_or_email) EditText mUsername;
    @InjectView(R.id.sign_in_screen_password)          EditText mPassword;

    // Model object.
    private RestModelUser mObjectModelUser;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use a vertical slide animation.
        setCustomTransitions(CustomTransition.T_SLIDE_VERTICAL);
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    @OnClick(R.id.sign_in_screen_sign_in) @SuppressWarnings("unused")
    public void sign_in() {

        if (RestAPICallback.shouldThrottle()) {
            return;
        }

        if (mObjectModelUser == null) {
            mObjectModelUser = new RestModelUser();
        }

        // Set desired registration fields.
        mObjectModelUser.setUsername(mUsername);
        mObjectModelUser.setPassword(mPassword);
        mObjectModelUser.signIn(this, new RestModelUser.CallbackSignIn() {

            @Override
            public void onSignIn() {

                // Enter main app.
                AuthHelper.instance().tryEnterMainApp(SignInScreen.this);
            }
        });
    }

    // endregion
}