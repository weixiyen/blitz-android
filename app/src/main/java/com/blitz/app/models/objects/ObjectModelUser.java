package com.blitz.app.models.objects;

import android.app.Activity;
import android.widget.EditText;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectAuth;
import com.blitz.app.models.rest_objects.JsonObjectUsers;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.app.AppDataObject;

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

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Un-persist user information.
     */
    public void removeUserInfo() {

        AppData.clear(AppDataObject.userId);
        AppData.clear(AppDataObject.userName);

        AppData.clear(AppDataObject.userEmail);
        AppData.clear(AppDataObject.userPassword);
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
                JsonObjectUsers jsonObject = getJsonObject(JsonObjectUsers.class);

                // Set id, and username from result.
                AppDataObject.userId.set(jsonObject.result.id);
                AppDataObject.userName.set(jsonObject.result.username);

                // Rest is user provided.
                AppDataObject.userEmail.set(mEmail);
                AppDataObject.userPassword.set(mPassword);

                // Now signed up.
                callback.onSignUp();
            }
        };

        // Construct POST body.
        JsonObjectUsers.Body body = new JsonObjectUsers.Body(mEmail, mUsername, mPassword);

        // Make rest call for code.
        RestAPIClient.getAPI().users(body,
                new RestAPICallback<JsonObjectUsers>(mRestApiObject, operation, true));
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
                JsonObjectAuth jsonObjectAuth = getJsonObject(JsonObjectAuth.class);

                // Set id, and username from result.
                AppDataObject.userId.set(jsonObjectAuth.user.id);
                AppDataObject.userName.set(jsonObjectAuth.user.username);

                // Rest is user provided.
                AppDataObject.userEmail.set(mEmail);
                AppDataObject.userPassword.set(mPassword);

                // Now signed in.
                callback.onSignIn();
            }
        };

        // Construct POST body.
        JsonObjectAuth.Body body = new JsonObjectAuth.Body(mUsername, mPassword);

        // Make auth rest call.
        RestAPIClient.getAPI().auth(body,
                new RestAPICallback<JsonObjectAuth>(mRestApiObject, operation, true));
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    public interface CallbackSignUp { public void onSignUp(); }
    public interface CallbackSignIn { public void onSignIn(); }
}