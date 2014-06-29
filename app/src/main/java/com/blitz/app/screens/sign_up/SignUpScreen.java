package com.blitz.app.screens.sign_up;

import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.base.config.BaseConfig;
import com.blitz.app.utilities.facebook.FacebookHelper;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
public class SignUpScreen extends BaseActivity {

    private void test() {
        // Authenticate if needed.
        if (BaseConfig.AUTH_WITH_FACEBOOK) {

            authenticateWithFacebook();
        } else {

            // Otherwise just go.
        }
    }

    /**
     * Get facebook session token then launch
     * next activity.
     */
    private void authenticateWithFacebook() {

        // Create a new facebook helper object.
        final FacebookHelper facebookHelper = new FacebookHelper(this);

        // Authorize the user and launch main screen.
        facebookHelper.authorizeUser(new Request.GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser graphUser, Response response) {

                String accessToken = facebookHelper.getAccessToken(graphUser);

                if (accessToken != null) {
                    // Transition next screen.
                }
            }
        });
    }
}