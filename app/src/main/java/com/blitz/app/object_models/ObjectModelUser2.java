package com.blitz.app.object_models;

import android.app.Activity;
import android.widget.EditText;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.blitz.app.utilities.rest.RestAPIObject;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.google.gson.JsonObject;

/**
 * Observable flavor of User object model.
 *
 * Created by Nate on 7/9/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelUser2 {

    // region Member Variables
    // =============================================================================================

    private String mUsername;
    private String mPassword;
    private String mEmail;
    private String mAvatarId;

    private int mRating;
    private int mWins;
    private int mLosses;
    private int mCash;

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Fetch username.
     */
    public String getUsername() {

        return mUsername;
    }

    /**
     * Fetch rating.
     */
    public int getRating() {

        return mRating;
    }

    /**
     * Fetch wins.
     */
    public int getWins() {

        return mWins;
    }

    /**
     * Fetch losses.
     */
    public int getLosses() {

        return mLosses;
    }

    /**
     * Fetch cash.
     */
    public int getCash() {

        return mCash;
    }

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
    @SuppressWarnings("unused")
    public void getUser(Activity activity, final Runnable success, final Runnable failure) {

        // Rest operation.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success(RestAPIObject restAPIObject) {

                JsonObject jsonObject = restAPIObject.getJsonObject();

                if (jsonObject != null) {

                    // Fetch user object.
                    JsonObject result = jsonObject.getAsJsonObject("result");

                    // Fetch the data.
                    mAvatarId = result.get("avatar_id").getAsString();
                    mCash     = result.get("cash").getAsInt();
                    mLosses   = result.get("losses").getAsInt();
                    mRating   = result.get("rating").getAsInt();
                    mUsername = result.get("username").getAsString();
                    mWins     = result.get("wins").getAsInt();

                    if (success != null) {
                        success.run();
                    }
                }
            }

            /**
             * Triggered when a model operation fails.
             *
             * @param logout Should also log out the user.
             */
            @Override
            public void failure(boolean logout) {

                if (failure != null) {
                    failure.run();
                } else {

                    super.failure(logout);
                }
            }
        };

        // Fetch user id.
        String userId = AuthHelper.getUserId();

        // Make api call to fetch user data.
        RestAPIClient.getAPI().user_get(userId,
                RestAPICallback.create(operation));
    }

    /**
     * Get and populate the user model - requires a user
     * id which means the user must be logged in.
     */
    @SuppressWarnings("unused")
    public void getUser(Activity activity, final Runnable success) {
        getUser(activity, success, null);
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
            public void success(RestAPIObject restAPIObject) {

                // Fetch json result.
                JsonObject jsonObject = restAPIObject.getJsonObject();

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
                RestAPICallback.create(operation, true));
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
            public void success(RestAPIObject restAPIObject) {

                // Fetch json result.
                JsonObject jsonObject = restAPIObject.getJsonObject();

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
                RestAPICallback.create(operation, true));
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface CallbackSignUp { public void onSignUp(); }
    public interface CallbackSignIn { public void onSignIn(); }

    // endregion
}