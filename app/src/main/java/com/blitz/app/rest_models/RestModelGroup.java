package com.blitz.app.rest_models;

import android.app.Activity;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.date.DateUtils;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by mrkcsc on 10/27/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class RestModelGroup extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SuppressWarnings("unused") @SerializedName("id")             private String mId;
    @SuppressWarnings("unused") @SerializedName("chat_id")        private String mChatId;
    @SuppressWarnings("unused") @SerializedName("name")           private String mName;
    @SuppressWarnings("unused") @SerializedName("name_lowercase") private String mNameLowercase;

    @SuppressWarnings("unused") @SerializedName("rank")   private Integer mRank;
    @SuppressWarnings("unused") @SerializedName("rating") private Integer mRating;

    @SuppressWarnings("unused") @SerializedName("recruiting") private Boolean mRecruiting;

    @SuppressWarnings("unused") @SerializedName("members") private List<String> mMembers;

    @SuppressWarnings("unused") @SerializedName("role_map")  private Map<String, String> mRoleMap;

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
    @SuppressWarnings("unused")
    public static void getGroupWithId(Activity activity, String groupId,
                                      final RestModelCallback<RestModelGroup> callback) {

        mRestAPI.group_get(groupId, new RestAPICallback<RestAPIResult<RestModelGroup>>(activity) {

            @Override
            public void success(RestAPIResult<RestModelGroup> jsonObject) {

                if (callback != null) {
                    callback.onSuccess(jsonObject.getResult());
                }
            }
        });
    }

    /**
     * Fetch a group with specified name.
     *
     * @param activity Activity for dialogs.
     * @param groupName Group name.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void getGroupWithName(Activity activity, String groupName,
                                        final RestModelCallback<RestModelGroup> callback) {

        if (groupName == null) {

            return;
        }

        // Fetch by user id.
        List<String> keys = Arrays.asList(groupName.toLowerCase());

        // Make api call.
        mRestAPI.groups_get("name_lowercase", keys, null, null, null, null, 1,
                new RestAPICallback<RestAPIResult<RestModelGroup>>(activity) {

                    @Override
                    public void success(RestAPIResult<RestModelGroup> jsonObject) {

                        if (callback != null) {

                            if (jsonObject.getResults() != null &&
                                jsonObject.getResults().size() > 0) {

                                callback.onSuccess(jsonObject.getResults().get(0));
                            } else {
                                callback.onSuccess(null);
                            }
                        }
                    }
                });
    }

    /**
     * Get groups for a given user.
     *
     * @param activity Activity for dialogs.
     * @param userId User id.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void getGroupsForUserId(Activity activity, String userId,
                                          final RestModelCallbacks<RestModelGroup> callback) {

        // Fetch by user id.
        List<String> keys = Arrays.asList(userId);

        // Pluck fields we need.
        List<String> pluck = Arrays.asList("name", "rating", "rank");

        // Order descending.
        String orderBy = "{\"created\": \"ASC\"}";

        // Make api call.
        mRestAPI.groups_get("members", keys, null, null, null, orderBy, 10,
                new RestAPICallback<RestAPIResult<RestModelGroup>>(activity) {

            @Override
            public void success(RestAPIResult<RestModelGroup> jsonObject) {

                if (callback != null) {
                    callback.onSuccess(jsonObject.getResults());
                }
            }
        });
    }

    @SuppressWarnings("unused")
    public static void getGroupsRecruitingWithLimit(Activity activity, Integer limit,
                                                    final RestModelCallbacks<RestModelGroup> callback) {

        Date dateNow = DateUtils.getDateInGMT();
        Date dateNowOneWeekEarlier =  DateUtils.getDateWithOffsetInMilliseconds(dateNow, 604800000L);

        String dateNowJson = DateUtils.getDateAsString(dateNow);
        String dateNowOneWeekEarlierJson = DateUtils.getDateAsString(dateNowOneWeekEarlier);

        // Fetch time range to fetch from.
        List<String> between = Arrays.asList(dateNowOneWeekEarlierJson, dateNowJson);

        // Filter by recruiting.
        String filter = "{\"recruiting\": true}";

        // Order descending.
        String orderBy = "{\"last_updated\": \"DESC\"}";

        // Make api call.
        mRestAPI.groups_get("last_updated", null, null, between, filter, orderBy, limit,
                new RestAPICallback<RestAPIResult<RestModelGroup>>(activity) {

            @Override
            public void success(RestAPIResult<RestModelGroup> jsonObject) {

                if (callback != null) {
                    callback.onSuccess(jsonObject.getResults());
                }
            }
        });
    }

    /**
     * Get the top groups.
     *
     * @param activity Activity for dialogs.
     * @param limit Limit.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void getTopGroupsWithLimit(Activity activity, Integer limit,
                                             final RestModelCallbacks<RestModelGroup> callback) {

        // Pluck fields we need.
        List<String> pluck = Arrays.asList("name", "rating", "rank");

        // Order descending.
        String orderBy = "{\"rating\": \"DESC\"}";

        // Make api call.
        mRestAPI.groups_get(null, null, pluck, null, null, orderBy, limit,
                new RestAPICallback<RestAPIResult<RestModelGroup>>(activity) {

            @Override
            public void success(RestAPIResult<RestModelGroup> jsonObject) {

                if (callback != null) {
                    callback.onSuccess(jsonObject.getResults());
                }
            }
        });
    }

    /**
     * Create a group with specified name.
     *
     * @param activity Activity for dialogs.
     * @param groupName Group name.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void createGroupWithName(Activity activity, String groupName,
                                           final RestModelCallback<RestModelGroup> callback) {

        // Construct POST body.
        JsonObject body = new JsonObject();
        body.addProperty("name", groupName);

        // Make api call.
        mRestAPI.groups_post(body, new RestAPICallback<RestAPIResult<RestModelGroup>>(activity) {

            @Override
            public void success(RestAPIResult<RestModelGroup> jsonObject) {

                if (callback != null) {
                    callback.onSuccess(jsonObject.getResult());
                }
            }
        });
    }

    /**
     * Join a group.
     *
     * @param activity Activity for dialogs.
     * @param groupId Group id.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void joinGroupWithId(Activity activity, String groupId,
                                       final RestModelCallback<RestModelGroup> callback) {

        // Create object holding values to replace.
        JsonObject jsonAppend   = new JsonObject();
        JsonObject jsonMembers  = new JsonObject();

        jsonMembers.addProperty("members", AuthHelper.instance().getUserId());
        jsonAppend.add("append", jsonMembers);

        // Make api call.
        mRestAPI.group_patch(groupId, jsonAppend,
                new RestAPICallback<RestAPIResult<RestModelGroup>>(activity) {

                    @Override
                    public void success(RestAPIResult<RestModelGroup> result) {

                        if (callback != null) {
                            callback.onSuccess(result.getResult());
                        }
                    }
                });
    }

    /**
     * Update recruiting status.
     *
     * @param activity Activity for dialogs.
     * @param groupId Group id.
     * @param recruiting Recruiting enabled.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void updateRecruitingStatusForGroup(Activity activity, String groupId, Boolean recruiting,
                                                      final RestModelCallback<RestModelGroup> callback) {

        // Create object holding values to replace.
        JsonObject jsonReplace     = new JsonObject();
        JsonObject jsonRecruiting  = new JsonObject();

        jsonRecruiting.addProperty("recruiting", recruiting);
        jsonReplace.add("append", jsonRecruiting);

        // Make api call.
        mRestAPI.group_patch(groupId, jsonReplace,
                new RestAPICallback<RestAPIResult<RestModelGroup>>(activity) {

            @Override
            public void success(RestAPIResult<RestModelGroup> result) {

                // Now left queue.
                if (callback != null) {
                    callback.onSuccess(result.getResult());
                }
            }
        });
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Is the current user an officer.
     *
     * @return Is officer.
     */
    @SuppressWarnings("unused")
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

        if (mRoleMap.containsKey(userId)) {

            return mRoleMap.get(userId);
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

        if ("OWNER".equals(role)) {
            return true;
        } else if ("GENERAL_MANAGER".equals(role)) {
            return true;
        } else if ("COACH".equals(role)) {
            return true;
        } else if ("ASSISTANT_COACH".equals(role)) {
            return true;
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
