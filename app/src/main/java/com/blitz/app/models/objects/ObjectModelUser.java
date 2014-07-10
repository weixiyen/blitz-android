package com.blitz.app.models.objects;

import android.widget.EditText;

import com.blitz.app.models.rest.RestAPIOperation;

/**
 * Created by mrkcsc on 7/9/14.
 */
public class ObjectModelUser {

    private String mUsername;
    private String mPassword;
    private String mEmail;

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

    public void signUp(RestAPIOperation operation) {

    }
}