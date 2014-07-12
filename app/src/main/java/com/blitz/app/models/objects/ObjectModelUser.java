package com.blitz.app.models.objects;

import android.widget.EditText;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectUsers;
import com.blitz.app.utilities.appdata.AppData;
import com.blitz.app.utilities.appdata.AppDataObject;

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
     * Persist user information.
     */
    public void persistUserInfo() {
        JsonObjectUsers jsonObject = getJsonObject(JsonObjectUsers.class);

        AppDataObject.userId.set(jsonObject.result.id);
        AppDataObject.userName.set(jsonObject.result.username);

        AppDataObject.userEmail.set(mEmail);
        AppDataObject.userPassword.set(mPassword);
    }

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
        RestAPIClient.getAPI().users(body,
                new RestAPICallback<JsonObjectUsers>(mRestApiObject, operation, true));
    }
}