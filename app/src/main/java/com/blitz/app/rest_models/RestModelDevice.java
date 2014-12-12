package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.blitz.app.utilities.json.JsonHelperObject;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;

import lombok.Getter;

/**
 * Created by mrkcsc on 8/10/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelDevice extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("id")                        @Getter private String id;
    @SerializedName("device_id")                 @Getter private String deviceId;
    @SerializedName("device_type")               @Getter private String deviceType;
    @SerializedName("user_id")                   @Getter private String userId;
    @SerializedName("push_notification_token")   @Getter private String pushNotificationToken;
    @SerializedName("push_notification_enabled") @Getter private boolean pushNotificationsEnabled;
    @SerializedName("created")                   @Getter private Date created;
    @SerializedName("last_updated")              @Getter private Date lastUpdated;

    // endregion

    // region REST Methods
    // ============================================================================================================

    /**
     * Get specified device given a device id.  The model
     * will be populated based on the result.
     *
     * @param activity Activity for dialogs.
     * @param deviceId Target device id.
     * @param callback Completion callback.
     */
    @SuppressWarnings("unused")
    public static void get(Activity activity,
                           @NonNull String deviceId,
                           @NonNull RestResult<RestModelDevice> callback) {

        // Make rest call for code.
        restAPI.device_get(deviceId, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Create a new device model object given
     * a device id.  The model will be populated
     * based on the result.
     *
     * @param activity Activity for dialogs.
     * @param deviceId Target device id.
     * @param callback Completion callback.
     */
    @SuppressWarnings("unused")
    public static void create(Activity activity,
                              @NonNull String deviceId,
                              @NonNull RestResult<RestModelDevice> callback) {

        JsonObject body = JsonHelperObject.addProperties(
                Arrays.asList("device_id",  "device_type", "push_notification_enabled"),
                Arrays.asList(deviceId, "ANDROID", Boolean.toString(false)));

        // Make rest call for code.
        restAPI.devices_post(body, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Update device model with the provided
     * values.  Assumes the device has either
     * been created or fetched.
     *
     * @param activity Activity for dialogs.
     * @param id Target id.
     * @param userId Associated user id.
     * @param pushNotificationsEnabled Push notifications on or off.
     * @param pushNotificationToken Push notification token.
     * @param callback Completion callback.
     */
    @SuppressWarnings("unused")
    public static void update(Activity activity,
                              @NonNull String id,
                              String userId,
                              Boolean pushNotificationsEnabled,
                              String pushNotificationToken,
                              @NonNull RestResult<RestModelDevice> callback) {

        // Create object holding values to replace.
        JsonObject replace = new JsonObject();

        if (pushNotificationsEnabled != null) {

            // Update push notifications if needed.
            replace.addProperty("push_notification_enabled", pushNotificationsEnabled);
        }

        if (pushNotificationToken != null) {

            // Update token if needed.
            replace.addProperty("push_notification_token", pushNotificationToken);
        }

        if (userId != null) {

            // Update user id if needed.
            replace.addProperty("user_id", userId);
        }

        // Create body.
        JsonObject body = new JsonObject();

        // Add replace object.
        body.add("replace", replace);

        // Make rest call for code.
        restAPI.device_patch(id, body, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    // endregion
}