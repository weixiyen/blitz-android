package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.date.DateUtils;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * Created by mrkcsc on 10/27/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration, MismatchedQueryAndUpdateOfCollection")
public class RestModelGroup extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("id")             @Getter private String id;
    @SerializedName("chat_id")        @Getter private String chatId;
    @SerializedName("name")           @Getter private String name;
    @SerializedName("name_lowercase") @Getter private String nameLowercase;
    @SerializedName("rank")           @Getter private Integer rank;
    @SerializedName("rating")         @Getter private Integer rating;
    @SerializedName("recruiting")     @Getter private boolean recruiting;
    @SerializedName("members")        @Getter private List<String> memberIds;
    @SerializedName("user_info")      @Getter private Map<String, RestModelUser> members;
    @SerializedName("role_map")       @Getter private Map<String, String> roleMap;

    // endregion

    // region REST Methods
    // ============================================================================================================

    /**
     * Fetch group with specified id.
     *
     * @param activity Activity for dialogs.
     * @param groupId Group id.
     * @param callback Callback.
     */
    public static void getGroupWithId(Activity activity, String groupId,
                                      @NonNull RestResult<RestModelGroup> callback) {

        restAPI.group_get(groupId, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Fetch a group with specified name.
     *
     * @param activity Activity for dialogs.
     * @param groupName Group name.
     * @param callback Callback.
     */
    public static void getGroupWithName(Activity activity,
                                        @NonNull String groupName,
                                        @NonNull RestResult<RestModelGroup> callback) {

        // Fetch by user id.
        List<String> keys = Arrays.asList(groupName.toLowerCase());

        // Make api call.
        restAPI.groups_get("name_lowercase", keys, null, null, null, null, 1,
                new RestAPICallback<>(activity, result -> {

                    if (result.getResults() != null &&
                            result.getResults().size() > 0) {

                        callback.onSuccess(result.getResults().get(0));
                    } else {
                        callback.onSuccess(null);
                    }
                }, null));
    }

    /**
     * Get groups for a given user.
     *
     * @param activity Activity for dialogs.
     * @param userId User id.
     * @param callback Callback.
     */
    public static void getGroupsForUserId(Activity activity, String userId,
                                          @NonNull RestResults<RestModelGroup> callback) {

        // Fetch by user id.
        List<String> keys = Arrays.asList(userId);

        // Order descending.
        String orderBy = "{\"created\": \"ASC\"}";

        // Make api call.
        restAPI.groups_get("members", keys, null, null, null, orderBy, 10,
                new RestAPICallback<>(activity, result -> callback.onSuccess(result.getResults()), null));
    }

    /**
     * Get groups that are recruiting.
     */
    public static void getGroupsRecruitingWithLimit(Activity activity, Integer limit,
                                                    @NonNull RestResults<RestModelGroup> callback) {

        Date dateNow = DateUtils.getDateInGMT();
        Date dateNowOneWeekEarlier =  DateUtils.getDateWithOffsetInMilliseconds(dateNow, 604800000L * -2);

        String dateNowJson = DateUtils.getDateAsString(dateNow);
        String dateNowOneWeekEarlierJson = DateUtils.getDateAsString(dateNowOneWeekEarlier);

        // Fetch time range to fetch from.
        List<String> between = Arrays.asList(dateNowOneWeekEarlierJson, dateNowJson);

        // Filter by recruiting.
        String filter = "{\"recruiting\": true}";

        // Order descending.
        String orderBy = "{\"last_updated\": \"DESC\"}";

        // Make api call.
        restAPI.groups_get("last_updated", null, null, between, filter, orderBy, limit,
                new RestAPICallback<>(activity, result -> callback.onSuccess(result.getResults()), null));
    }

    /**
     * Get the top groups.
     *
     * @param activity Activity for dialogs.
     * @param limit Limit.
     * @param callback Callback.
     */
    public static void getTopGroupsWithLimit(Activity activity, Integer limit,
                                             @NonNull RestResults<RestModelGroup> callback) {

        // Pluck fields we need.
        List<String> pluck = Arrays.asList("name", "rating", "rank");

        // Order descending.
        String orderBy = "{\"rating\": \"DESC\"}";

        // Make api call.
        restAPI.groups_get(null, null, pluck, null, null, orderBy, limit,
                new RestAPICallback<>(activity, result -> callback.onSuccess(result.getResults()), null));
    }

    /**
     * Create a group with specified name.
     *
     * @param activity Activity for dialogs.
     * @param groupName Group name.
     * @param callback Callback.
     */
    public static void createGroupWithName(Activity activity, String groupName,
                                           @NonNull RestResult<RestModelGroup> callback) {

        // Construct POST body.
        JsonObject body = new JsonObject();
        body.addProperty("name", groupName);

        // Make api call.
        restAPI.groups_post(body, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Join a group.
     *
     * @param activity Activity for dialogs.
     * @param groupId Group id.
     * @param callback Callback.
     */
    public static void joinGroupWithId(Activity activity, String groupId,
                                       @NonNull RestResult<RestModelGroup> callback) {

        // Create object holding values to replace.
        JsonObject jsonAppend   = new JsonObject();
        JsonObject jsonMembers  = new JsonObject();

        jsonMembers.addProperty("members", AuthHelper.instance().getUserId());
        jsonAppend.add("append", jsonMembers);

        // Make api call.
        restAPI.group_patch(groupId, jsonAppend, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Update recruiting status.
     *
     * @param activity Activity for dialogs.
     * @param groupId Group id.
     * @param recruiting Recruiting enabled.
     * @param callback Callback.
     */
    public static void updateRecruitingStatusForGroup(Activity activity, String groupId, Boolean recruiting,
                                                      @NonNull RestResult<RestModelGroup> callback) {

        // Create object holding values to replace.
        JsonObject jsonReplace     = new JsonObject();
        JsonObject jsonRecruiting  = new JsonObject();

        jsonRecruiting.addProperty("recruiting", recruiting);
        jsonReplace.add("replace", jsonRecruiting);

        // Make api call.
        restAPI.group_patch(groupId, jsonReplace, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Is the current user an officer.
     *
     * @return Is officer.
     */
    public boolean isCurrentUserOfficer() {

        return isUserOfficer(AuthHelper.instance().getUserId());
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Fetch group role for a given user id.
     *
     * @param userId User id.
     *
     * @return Associated role.
     */
    private String getRoleForUserId(String userId) {

        if (roleMap.containsKey(userId)) {

            return roleMap.get(userId);
        }

        return null;
    }

    /**
     * Is role an officer position.
     *
     * @param role Role.
     *
     * @return Is officer.
     */
    private boolean isRoleOfficer(String role) {

        if (role != null) {

            switch (role) {
                case "OWNER":
                    return true;
                case "GENERAL_MANAGER":
                    return true;
                case "COACH":
                    return true;
                case "ASSISTANT_COACH":
                    return true;
            }
        }

        return false;
    }

    /**
     * Is the user an officer of the group.
     *
     * @param userId User id.
     *
     * @return Is officer.
     */
    private boolean isUserOfficer(String userId) {

        return isRoleOfficer(getRoleForUserId(userId));
    }

    // endregion
}