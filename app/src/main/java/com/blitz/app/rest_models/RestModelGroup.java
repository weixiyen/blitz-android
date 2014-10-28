package com.blitz.app.rest_models;

import android.app.Activity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by mrkcsc on 10/27/14. Copyright 2014 Blitz Studios
 */
public class RestModelGroup extends RestModel {

    // region Member Variables
    // =============================================================================================

    @SuppressWarnings("unused") @SerializedName("id")             private String mId;
    @SuppressWarnings("unused") @SerializedName("chat_id")        private String mChatId;
    @SuppressWarnings("unused") @SerializedName("name")           private String mName;
    @SuppressWarnings("unused") @SerializedName("name_lowercase") private String mNameLowercase;

    @SuppressWarnings("unused") @SerializedName("rank")   private Integer mRank;
    @SuppressWarnings("unused") @SerializedName("rating") private Integer mRating;

    @SuppressWarnings("unused") @SerializedName("recruiting") private Boolean mRecruiting;

    @SuppressWarnings("unused") @SerializedName("members") private List<Object> mMembers;

    @SuppressWarnings("unused") @SerializedName("user_info") private Map<String, Object> mUserInfo;
    @SuppressWarnings("unused") @SerializedName("role_map")  private Map<String, Object> mRoleMap;

    // endregion

    // region REST Methods
    // =============================================================================================

    @SuppressWarnings("unused")
    public static void createGroupWithName(Activity activity, String groupName,
                                           final RestModelCallback<RestModelGroup> callback) {

    }

    @SuppressWarnings("unused")
    public static void getGroupWithId(Activity activity, String groupId,
                                      final RestModelCallback<RestModelGroup> callback) {

    }

    @SuppressWarnings("unused")
    public static void getGroupWithName(Activity activity, String groupName,
                                        final RestModelCallback<RestModelGroup> callback) {

    }

    @SuppressWarnings("unused")
    public static void getGroupsForUserId(Activity activity, String userId,
                                          final RestModelCallbacks<RestModelGroup> callback) {

    }

    @SuppressWarnings("unused")
    public static void getGroupsRecruitingWithLimit(Activity activity, Integer limit,
                                                    final RestModelCallbacks<RestModelGroup> callback) {

    }

    @SuppressWarnings("unused")
    public static void getTopGroupsWithLimit(Activity activity, Integer limit,
                                             final RestModelCallbacks<RestModelGroup> callback) {

    }

    @SuppressWarnings("unused")
    public static void joinGroupWithName(Activity activity, String groupName,
                                         final RestModelCallback<RestModelGroup> callback) {

    }

    @SuppressWarnings("unused")
    public static void updateRecruitingStatusForGroup(Activity activity, boolean recruiting,
                                                      final RestModelCallback<RestModelGroup> callback) {

    }

    // endregion
}
