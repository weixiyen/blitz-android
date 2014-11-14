package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestCallback;
import com.blitz.app.rest_models.RestModel;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;

/**
 * Created by mrkcsc on 10/12/14. Copyright 2014 Blitz Studios
 */
public class ViewModelSignIn extends ViewModel {

    // region Constructor
    // ============================================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelSignIn(BaseActivity activity, ViewModel.Callbacks callbacks) {
        super(activity, callbacks);

        // Lookup existing info.
        checkForExistingUsernameOrEmail();
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Initialize the view model.
     */
    @Override
    public void initialize() {

    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Sign in the user.
     *
     * @param usernameOrEmail Username or email.
     * @param password Password.
     */
    @SuppressWarnings("unused")
    public void signIn(String usernameOrEmail, String password) {

        if (RestModel.shouldThrottle()) {
            return;
        }

        RestModelUser.signIn(mActivity, usernameOrEmail, password,
                new RestCallback<RestModelUser>() {

                    @Override
                    public void onSuccess(RestModelUser object) {

                        // Enter main app.
                        AuthHelper.instance().tryEnterMainApp(mActivity);
                    }
                });
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Emit existing email or username saved in app data
     * to make the login process easier for the user.
     */
    private void checkForExistingUsernameOrEmail() {

        Callbacks callbacks = getCallbacks(Callbacks.class);

        if (callbacks != null) {

            // Try to fetch existing login info.
            String userEmail = AppDataObject.userEmail.get();
            String userName = AppDataObject.userName.get();

            if (userEmail != null) {

                callbacks.onSavedEmailOrUsernameReceived(userEmail);

            } else if (userName != null) {

                callbacks.onSavedEmailOrUsernameReceived(userName);
            }

        }
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onSavedEmailOrUsernameReceived(String emailOrUsername);
    }

    // endregion
}
