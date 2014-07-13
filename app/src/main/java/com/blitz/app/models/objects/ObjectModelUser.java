package com.blitz.app.models.objects;

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
     * Persist user information and sign up.
     */
    public void persistAfterSignUp() {
        JsonObjectUsers jsonObject = getJsonObject(JsonObjectUsers.class);

        AppDataObject.userId.set(jsonObject.result.id);
        AppDataObject.userName.set(jsonObject.result.username);

        AppDataObject.userEmail.set(mEmail);
        AppDataObject.userPassword.set(mPassword);
    }

    /**
     * Persist user information after sign in.
     */
    public void persistAfterSignIn() {
        JsonObjectAuth jsonObjectAuth = getJsonObject(JsonObjectAuth.class);

        AppDataObject.userId.set(jsonObjectAuth.user.id);
        AppDataObject.userName.set(jsonObjectAuth.user.username);

        AppDataObject.userEmail.set(mEmail);
        AppDataObject.userPassword.set(mPassword);
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
     * @param operation Rest operation.
     */
    public void signUp(RestAPIOperation operation) {

        // Construct POST body.
        JsonObjectUsers.Body body = new JsonObjectUsers.Body(mEmail, mUsername, mPassword);

        // Make rest call for code.
        RestAPIClient.getAPI().users(body,
                new RestAPICallback<JsonObjectUsers>(mRestApiObject, operation, true));
    }

    /**
     * Sign in the user.
     *
     * @param operation Rest operation.
     */
    public void signIn(RestAPIOperation operation) {

        // Construct POST body.
        JsonObjectAuth.Body body = new JsonObjectAuth.Body(mUsername, mPassword);

        // Make auth rest call.
        RestAPIClient.getAPI().auth(body,
                new RestAPICallback<JsonObjectAuth>(mRestApiObject, operation, true));
    }
}