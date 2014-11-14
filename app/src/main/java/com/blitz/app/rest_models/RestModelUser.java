package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mrkcsc on 7/9/14. Copyright 2014 Blitz Studios
 */
public class RestModelUser extends RestModel {

    // region Member Variables
    // ============================================================================================================

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

    // endregion

    // region REST Calls
    // ============================================================================================================

    /**
     * Update the users avatar.
     *
     * @param activity Activity for dialogs.
     * @param avatarId Avatar id.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void updateAvatar(Activity activity, String avatarId,
                                    @NonNull RestCallback<RestModelUser> callback) {
        if (avatarId == null) {
            return;
        }

        // Create object holding values to replace.
        JsonObject replace = new JsonObject();

        replace.addProperty("avatar_id", avatarId);

        // Create body.
        JsonObject body = new JsonObject();

        // Add replace object.
        body.add("replace", replace);

        // Make rest call for code.
        mRestAPI.user_patch(body, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Get and populate the user model - requires a user
     * id which means the user must be logged in.
     */
    @SuppressWarnings("unused")
    public static void getUser(Activity activity, String userId,
                               @NonNull RestCallback<RestModelUser> callback,
                               boolean logoutOnFailure) {

        // Rest operation.
        RestAPICallback<RestAPIResult<RestModelUser>> operation = new RestAPICallback<>(activity,
                        result -> callback.onSuccess(result.getResult()), null);

        operation.setLogoutOnFailure(logoutOnFailure);

        // Make api call to fetch user data.
        mRestAPI.user_get(userId, operation);
    }

    /**
     * Fetch a list of the top ELO users.
     *
     * @param activity Activity for dialogs.
     * @param limit Limit of players.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void getTopUsersWithLimit(Activity activity, final int limit,
                                            @NonNull RestCallbacks<RestModelUser> callback) {

        // Only fetch relevant fields from the users table.
        List<String> pluck = Arrays.asList("id", "username", "wins",
                "losses", "rating", "avatar_id");

        // Sort it by rating.
        String orderBy = "{\"rating\":\"DESC\"}";

        mRestAPI.users_get(null, null, pluck, orderBy, limit, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null));
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
                                @NonNull RestCallbacks<RestModelUser> callback) {

        // Pluck a subset of the fields.
        List<String> pluck = Arrays.asList("id", "username", "status", "avatar_id",
                "rating", "wins", "losses", "ties", "cash");

        mRestAPI.users_get(userIds, "id", pluck, null, null, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null));
    }

    /**
     * Sign up (register) a user.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void signUp(Activity activity, String email, String username, String password,
                              @NonNull RestCallback<RestModelUser> callback) {

        // Rest operation.
        RestAPICallback<RestAPIResult<RestModelUser>> operation = new RestAPICallback<>(activity, result -> {

            // Fetch user object.
            RestModelUser user = result.getResult();

            // Sign in the user.
            AuthHelper.instance().signIn(user.mId, user.mUsername, user.mEmail);

            // Now signed up.
            callback.onSuccess(user);

        }, null);

        // Authentication operation.
        operation.setIsAuthentication(true);

        // Construct POST body.
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("username", username);
        body.addProperty("password", password);

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
    public static void signIn(Activity activity, String username, String password,
                              @NonNull RestCallback<RestModelUser> callback) {

        // Rest operation.
        RestAPICallback<RestAPIResult<RestModelUser>> operation = new RestAPICallback<>(activity, result -> {

            // Fetch user object.
            RestModelUser user = result.getResult();

            // Sign in the user.
            AuthHelper.instance().signIn(user.mId, user.mUsername, user.mEmail);

            // Now signed in.
            callback.onSuccess(user);

        }, null);

        // Authentication operation.
        operation.setIsAuthentication(true);

        // Construct POST body.
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);

        // Make auth rest call.
        mRestAPI.auth_post(body, operation);
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

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

    // endregion
}