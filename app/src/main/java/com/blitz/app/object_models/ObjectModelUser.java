package com.blitz.app.object_models;

import android.app.Activity;
import android.widget.EditText;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 7/9/14.
 */
public class ObjectModelUser extends ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    private String mUsername;
    private String mPassword;
    private String mEmail;
    private String mAvatarId;

    private int mRating;
    private int mWins;
    private int mLosses;
    private int mTies;
    private int mCash;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Set email.
     *
     * @param email Email.
     */
    public void setEmail(EditText email) {

        mEmail = email.getText().toString();
    }

    /**
     * Set username.
     *
     * @param username Username.
     */
    public void setUsername(EditText username) {

        mUsername = username.getText().toString();
    }

    /**
     * Set password.
     *
     * @param password Passowrd
     */
    public void setPassword(EditText password) {

        mPassword = password.getText().toString();
    }

    /**
     * Get and populate the user model - requires a user
     * id which means the user must be logged in.
     */
    public void getUser(Activity activity, final Runnable callback) {

        // Rest operation.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success() {

                JsonObject jsonObject = getJsonObject();

                if (jsonObject != null) {

                    // Fetch user object.
                    JsonObject result = jsonObject.getAsJsonObject("result");

                    // Fetch the data.
                    mAvatarId = result.get("avatar_id").getAsString();
                    mCash     = result.get("cash").getAsInt();
                    mLosses   = result.get("losses").getAsInt();
                    mRating   = result.get("rating").getAsInt();
                    mTies     = result.get("ties").getAsInt();
                    mUsername = result.get("username").getAsString();
                    mWins     = result.get("wins").getAsInt();

                    callback.run();
                }
            }
        };

        // Fetch user id.
        String userId = AuthHelper.getUserId();

        // Make api call to fetch user data.
        RestAPIClient.getAPI().user_get(userId,
                RestAPICallback.create(mRestApiObject, operation));
    }

    /**
     * Sign up (register) a user.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void signUp(Activity activity, final CallbackSignUp callback) {

        // Rest operation.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success() {

                // Fetch json result.
                JsonObject jsonObject = getJsonObject();

                if (jsonObject != null) {

                    // Fetch user object.
                    JsonObject result = jsonObject.getAsJsonObject("result");

                    // Sign in the user.
                    AuthHelper.signIn(
                            result.get("id").getAsString(),
                            result.get("username").getAsString(), mEmail, mPassword);

                    // Now signed up.
                    callback.onSignUp();
                }
            }
        };

        // Construct POST body.
        JsonObject body = new JsonObject();
                   body.addProperty("email", mEmail);
                   body.addProperty("username", mUsername);
                   body.addProperty("password", mPassword);

        // Make rest call for code.
        RestAPIClient.getAPI().users_post(body,
                RestAPICallback.create(mRestApiObject, operation, true));
    }

    /**
     * Sign in the user.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void signIn(Activity activity, final CallbackSignIn callback) {

        // Rest operation.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success() {

                // Fetch json result.
                JsonObject jsonObject = getJsonObject();

                if (jsonObject != null) {

                    // Fetch user object.
                    JsonObject user = jsonObject.getAsJsonObject("user");

                    // Sign in the user.
                    AuthHelper.signIn(
                            user.get("id").getAsString(),
                            user.get("username").getAsString(), mEmail, mPassword);

                    // Now signed in.
                    callback.onSignIn();
                }
            }
        };

        // Construct POST body.
        JsonObject body = new JsonObject();
                   body.addProperty("username", mUsername);
                   body.addProperty("password", mPassword);

        // Make auth rest call.
        RestAPIClient.getAPI().auth_post(body,
                RestAPICallback.create(mRestApiObject, operation, true));
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    public interface CallbackSignUp { public void onSignUp(); }
    public interface CallbackSignIn { public void onSignIn(); }
}