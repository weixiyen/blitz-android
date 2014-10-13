package com.blitz.app.screens.sign_in;

import android.os.Bundle;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelSignIn;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/1/14. Copyright 2014 Blitz Studios
 */
public class SignInScreen extends BaseActivity implements ViewModelSignIn.ViewModelSignInCallbacks {

    // region Member Variables
    // =============================================================================================

    // Mapped views.
    @InjectView(R.id.sign_in_screen_username_or_email) EditText mUsername;
    @InjectView(R.id.sign_in_screen_password)          EditText mPassword;

    private ViewModelSignIn mViewModel;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Slide up style transition.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use a vertical slide animation.
        setCustomTransitions(CustomTransition.T_SLIDE_VERTICAL);
    }

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelSignIn(this, this);
        }

        return mViewModel;
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    /**
     * Sign the user in when they click the button.  Error
     * handling is taken care of automatically.
     */
    @OnClick(R.id.sign_in_screen_sign_in) @SuppressWarnings("unused")
    public void sign_in() {

        if (mViewModel != null) {
            mViewModel.signIn(mUsername.getText().toString(), mPassword.getText().toString());
        }
    }

    /**
     * If a saved email or username is received, pre
     * populate the UI with that info.
     *
     * @param emailOrUsername Email or username.
     */
    @Override
    public void onSavedEmailOrUsernameReceived(String emailOrUsername) {

        // Populate the username field.
        mUsername.setText(emailOrUsername);

        // Focus password field.
        mPassword.requestFocus();
    }

    // endregion
}