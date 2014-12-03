package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

/**
 * Created by mrkcsc on 7/9/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelUser extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("banned")         @Getter private Boolean banned;

    @SerializedName("losses")         @Getter private int losses;
    @SerializedName("wins")           @Getter private int wins;
    @SerializedName("matches_played") @Getter private int matchesPlayed;
    @SerializedName("rating")         @Getter private int rating;
    @SerializedName("ties")           @Getter private int ties;
    @SerializedName("cash")           @Getter private int cash;

    @SerializedName("id")        @Getter private String id;
    @SerializedName("username")  @Getter private String username;
    @SerializedName("avatar_id") @Getter private String avatarId;
    @SerializedName("full_name") @Getter private String fullName;
    @SerializedName("email")     @Getter private String email;

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
                                    @NonNull RestResult<RestModelUser> callback) {
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
        restAPI.user_patch(body, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Get and populate the user model - requires a user
     * id which means the user must be logged in.
     */
    @SuppressWarnings("unused")
    public static void getUser(Activity activity, String userId,
                               @NonNull RestResult<RestModelUser> callback,
                               boolean logoutOnFailure) {

        // Rest operation.
        RestAPICallback<RestAPIResult<RestModelUser>> operation = new RestAPICallback<>(activity,
                        result -> callback.onSuccess(result.getResult()), null);

        operation.setLogoutOnFailure(logoutOnFailure);

        // Make api call to fetch user data.
        restAPI.user_get(userId, operation);
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
                                            @NonNull RestResults<RestModelUser> callback) {

        // Only fetch relevant fields from the users table.
        List<String> pluck = Arrays.asList("id", "username", "wins",
                "losses", "rating", "avatar_id");

        // Sort it by rating.
        String orderBy = "{\"rating\":\"DESC\"}";

        restAPI.users_get(null, null, pluck, orderBy, limit, new RestAPICallback<>(activity,
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
                                @NonNull RestResults<RestModelUser> callback) {

        // Pluck a subset of the fields.
        List<String> pluck = Arrays.asList("id", "username", "status", "avatar_id",
                "rating", "wins", "losses", "ties", "cash");

        restAPI.users_get(userIds, "id", pluck, null, null, new RestAPICallback<>(activity,
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
                              @NonNull RestResult<RestModelUser> callback) {

        // Rest operation.
        RestAPICallback<RestAPIResult<RestModelUser>> operation = new RestAPICallback<>(activity, result -> {

            // Fetch user object.
            RestModelUser user = result.getResult();

            // Sign in the user.
            AuthHelper.instance().signIn(user.id, user.username, user.email);

            // Now signed up.
            callback.onSuccess(user);

        }, null);

        // Authentication operation.
        operation.setAuthenticating(true);

        // Construct POST body.
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("username", username);
        body.addProperty("password", password);

        // Make rest call for code.
        restAPI.users_post(body, operation);
    }

    /**
     * Sign in the user.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void signIn(Activity activity, String username, String password,
                              @NonNull RestResult<RestModelUser> callback) {

        // Rest operation.
        RestAPICallback<RestAPIResult<RestModelUser>> operation = new RestAPICallback<>(activity, result -> {

            // Fetch user object.
            RestModelUser user = result.getResult();

            // Sign in the user.
            AuthHelper.instance().signIn(user.id, user.username, user.email);

            // Now signed in.
            callback.onSuccess(user);

        }, null);

        // Authentication operation.
        operation.setAuthenticating(true);

        // Construct POST body.
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);

        // Make auth rest call.
        restAPI.auth_post(body, operation);
    }

    // endregion
}