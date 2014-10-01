package com.blitz.app.object_models;

import android.app.Activity;
import android.widget.EditText;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrkcsc on 7/9/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelUser extends ObjectModel {

    // region Member Variables
    // =============================================================================================

    @SuppressWarnings("unused") @SerializedName("banned") private Boolean mBanned;

    @SuppressWarnings("unused") @SerializedName("losses")         private int mLosses;
    @SuppressWarnings("unused") @SerializedName("wins")           private int mWins;
    @SuppressWarnings("unused") @SerializedName("matches_played") private int mMatchesPlayed;
    @SuppressWarnings("unused") @SerializedName("rating")         private int mRating;
    @SuppressWarnings("unused") @SerializedName("ties")           private int mTies;
    @SuppressWarnings("unused") @SerializedName("cash")           private int mCash;

    @SuppressWarnings("unused") @SerializedName("id")        private String mId;
    @SuppressWarnings("unused") @SerializedName("username")  private String mUsername;
    @SuppressWarnings("unused") @SerializedName("avatar_id") private String mAvatarId;
    @SuppressWarnings("unused") @SerializedName("full_name") private String mFullName;
    @SuppressWarnings("unused") @SerializedName("email")     private String mEmail;

    private String mPassword;

    // endregion

    // region REST Calls
    // =============================================================================================

    /**
     * Update the users avatar.
     *
     * @param activity Activity for dialogs.
     * @param avatarId Avatar id.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void updateAvatar(Activity activity, String avatarId,
                                    final CallbackUser callback) {
        if (avatarId == null) {
            return;
        }

        RestAPICallback<RestAPIResult<ObjectModelUser>> operation =
                new RestAPICallback<RestAPIResult<ObjectModelUser>>(activity) {

                    @Override
                    public void success(RestAPIResult<ObjectModelUser> jsonObject) {

                        if (callback != null) {
                            callback.onSuccess(jsonObject.getResult());
                        }
                    }
                };

        // Create object holding values to replace.
        JsonObject replace = new JsonObject();

        replace.addProperty("avatar_id", avatarId);

        // Create body.
        JsonObject body = new JsonObject();

        // Add replace object.
        body.add("replace", replace);

        // Make rest call for code.
        mRestAPI.user_patch(body, operation);
    }

    /**
     * Get and populate the user model - requires a user
     * id which means the user must be logged in.
     */
    @SuppressWarnings("unused")
    public static void getUser(Activity activity, String userId,
                               final CallbackUser callback, boolean logoutOnFailure) {

        // Rest operation.
        RestAPICallback<RestAPIResult<ObjectModelUser>> operation =
                new RestAPICallback<RestAPIResult<ObjectModelUser>>(activity) {

                    @Override
                    public void success(RestAPIResult<ObjectModelUser> jsonObject) {

                        if (callback != null) {
                            callback.onSuccess(jsonObject.getResult());
                        }
                    }
                };

        operation.setLogoutOnFailure(logoutOnFailure);

        // Make api call to fetch user data.
        mRestAPI.user_get(userId, operation);
    }

    public static void getTopPlayersWithLimit(Activity activity, final int limit,
                                              final CallbackUsers callback) {

        final RestAPICallback<RestAPIResult<ObjectModelUser>> operation =
                new RestAPICallback<RestAPIResult<ObjectModelUser>>(activity) {
                    @Override
                    public void success(RestAPIResult<ObjectModelUser> result) {
                        callback.onSuccess(result.getResults());
                    }
                };

        final String orderBy = "{\"rating\":\"DESC\"}";
        mRestAPI.users_get(limit, orderBy, operation);
    }

    /**
     * Fetch a list of users (by user id).
     *
     * @param activity Activity for dialogs.
     * @param userIds List of user id's.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void getUsers(Activity activity, List<String> userIds,
                                final CallbackUsers callback) {

        RestAPICallback<RestAPIResult<ObjectModelUser>> operation =
                new RestAPICallback<RestAPIResult<ObjectModelUser>>(activity) {

                    @Override
                    public void success(RestAPIResult<ObjectModelUser> jsonObject) {

                        if (callback != null) {
                            callback.onSuccess(jsonObject.getResults());
                        }
                    }
                };

        // Pluck a subset of the fields.
        ArrayList<String> pluck = new ArrayList<String>();

        pluck.add("id");
        pluck.add("username");
        pluck.add("status");
        pluck.add("avatar_id");
        pluck.add("rating");
        pluck.add("wins");
        pluck.add("losses");
        pluck.add("ties");
        pluck.add("cash");

        mRestAPI.users_get(userIds, "id", pluck, null, null, operation);
    }

    /**
     * Sign up (register) a user.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public void signUp(Activity activity, final CallbackSignUp callback) {

        // Rest operation.
        RestAPICallback<JsonObject> operation =
                new RestAPICallback<JsonObject>(activity) {

                    @Override
                    public void success(JsonObject jsonObject) {

                        if (jsonObject != null) {

                            // Fetch user object.
                            JsonObject result = jsonObject.getAsJsonObject("result");

                            // Sign in the user.
                            AuthHelper.instance().signIn(
                                    result.get("id").getAsString(),
                                    result.get("username").getAsString(), mEmail, mPassword);

                            // Now signed up.
                            callback.onSignUp();
                        }
                    }
                };

        operation.setIsAuthentication(true);

        // Construct POST body.
        JsonObject body = new JsonObject();
        body.addProperty("email", mEmail);
        body.addProperty("username", mUsername);
        body.addProperty("password", mPassword);

        // Make rest call for code.
        mRestAPI.users_post(body, operation);
    }

    /**
     * Sign in the user.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public void signIn(Activity activity, final CallbackSignIn callback) {

        // Rest operation.
        RestAPICallback<JsonObject> operation =
                new RestAPICallback<JsonObject>(activity) {

                    @Override
                    public void success(JsonObject jsonObject) {

                        if (jsonObject != null) {

                            // Fetch user object.
                            JsonObject user = jsonObject.getAsJsonObject("user");

                            // Sign in the user.
                            AuthHelper.instance().signIn(
                                    user.get("id").getAsString(),
                                    user.get("username").getAsString(), mEmail, mPassword);

                            // Now signed in.
                            callback.onSignIn();
                        }
                    }
                };

        operation.setIsAuthentication(true);

        // Construct POST body.
        JsonObject body = new JsonObject();
        body.addProperty("username", mUsername);
        body.addProperty("password", mPassword);

        // Make auth rest call.
        mRestAPI.auth_post(body, operation);
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Fetch id of this user.
     *
     * @return User id.
     */
    public String getId() {

        return mId;
    }

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
     * Fetch ties.
     *
     * @return Ties.
     */
    public int getTies() {

        return mTies;
    }

    /**
     * Fetch cash.
     */
    public int getCash() {

        return mCash;
    }

    /**
     * Fetch avatar id.
     */
    public String getAvatarId() {

        return mAvatarId;
    }

    /**
     * Fetch email.
     */
    public String getEmail() {
        return mEmail;
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

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface CallbackSignUp { public void onSignUp(); }
    public interface CallbackSignIn { public void onSignIn(); }

    public interface CallbackUser {

        public void onSuccess(ObjectModelUser user);
    }

    public interface CallbackUsers {

        public void onSuccess(List<ObjectModelUser> users);
    }

    // endregion
}