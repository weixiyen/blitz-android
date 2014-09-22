package com.blitz.app.screens.sign_up;

import android.os.Bundle;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.object_models.ObjectModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallbackCombined;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/1/14. Copyright 2014 Blitz Studios
 */
public class SignUpScreen extends BaseActivity {

    // region Member Variables
    // =============================================================================================

    // Mapped views.
    @InjectView(R.id.sign_up_screen_email)    EditText mEmail;
    @InjectView(R.id.sign_up_screen_username) EditText mUsername;
    @InjectView(R.id.sign_up_screen_password) EditText mPassword;

    // Model object.
    private ObjectModelUser mObjectModelUser;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

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
    // =============================================================================================

    /**
     * Register the user.
     */
    @OnClick(R.id.sign_up_screen_sign_up) @SuppressWarnings("unused")
    public void sign_up() {

        if (RestAPICallbackCombined.shouldThrottle()) {
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

                // Enter main app.
                AuthHelper.instance().tryEnterMainApp(SignUpScreen.this);
            }
        });
    }

    // endregion
}