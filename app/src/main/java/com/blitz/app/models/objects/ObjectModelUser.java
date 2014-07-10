package com.blitz.app.models.objects;

import android.widget.EditText;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectUsers;

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
     * Set information needed for registration.
     *
     * @param email Desired email.
     * @param username Desired username.
     * @param password Desired password.
     */
    public void signUpSetInfo(EditText email, EditText username, EditText password) {

        mUsername = username.getText().toString();
        mPassword = password.getText().toString();

        mEmail = email.getText().toString();
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
        RestAPIClient.getAPI().users(body, new RestAPICallback<JsonObjectUsers>(mRestApiObject, operation));
    }
}