package com.blitz.app.screens.sign_up;

import android.os.Bundle;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestCallback;
import com.blitz.app.rest_models.RestModel;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/1/14. Copyright 2014 Blitz Studios
 */
public class SignUpScreen extends BaseActivity {

    // region Member Variables
    // ============================================================================================================

    // Mapped views.
    @InjectView(R.id.sign_up_screen_email)    EditText mEmail;
    @InjectView(R.id.sign_up_screen_username) EditText mUsername;
    @InjectView(R.id.sign_up_screen_password) EditText mPassword;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Setup view on creation.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use a vertical slide animation.
        setCustomTransitions(CustomTransition.T_SLIDE_VERTICAL);
    }

    // endregion

    // region Click Methods
    // ============================================================================================================

    /**
     * Register the user.
     */
    @OnClick(R.id.sign_up_screen_sign_up) @SuppressWarnings("unused")
    public void sign_up() {

        if (RestModel.shouldThrottle()) {
            return;
        }

        RestModelUser.signUp(this,
                mEmail.getText().toString(),
                mUsername.getText().toString(),
                mPassword.getText().toString(), new RestCallback<RestModelUser>() {

                    @Override
                    public void onSuccess(RestModelUser object) {

                        // Enter main app.
                        AuthHelper.instance().tryEnterMainApp(SignUpScreen.this);
                    }
                });
    }

    // endregion
}