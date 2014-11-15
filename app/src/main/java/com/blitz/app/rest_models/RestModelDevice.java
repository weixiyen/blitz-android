package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by mrkcsc on 8/10/14. Copyright 2014 Blitz Studios
 */
public class RestModelDevice extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SuppressWarnings("unused") @SerializedName("id")
    private String mId;
    @SuppressWarnings("unused") @SerializedName("device_id")
    private String mDeviceId;
    @SuppressWarnings("unused") @SerializedName("device_type")
    private String mDeviceType;
    @SuppressWarnings("unused") @SerializedName("user_id")
    private String mUserId;
    @SuppressWarnings("unused") @SerializedName("push_notification_token")
    private String mPushNotificationToken;

    @SuppressWarnings("unused") @SerializedName("push_notification_enabled")
    private boolean mPushNotificationsEnabled;

    @SuppressWarnings("unused") @SerializedName("created")
    private Date mCreated;
    @SuppressWarnings("unused") @SerializedName("last_updated")
    private Date mLastUpdated;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Fetch the id of the device model.  This is NOT
     * the same as the device id associated to a device.
     *
     * @return Id.
     */
    public String getId() {

        return mId;
    }

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
    public static void get(Activity activity, String deviceId, @NonNull CallbackDevice callback) {

        if (deviceId == null) {
            return;
        }

        // Make rest call for code.
        mRestAPI.device_get(deviceId, new RestAPICallback<>(activity,
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
    public static void create(Activity activity, String deviceId, @NonNull CallbackDevice callback) {

        if (deviceId == null) {
            return;
        }

        // Create post body.
        JsonObject body = new JsonObject();

        body.addProperty("device_id", deviceId);
        body.addProperty("device_type", "ANDROID");
        body.addProperty("push_notification_enabled", false);

        // Make rest call for code.
        mRestAPI.devices_post(body, new RestAPICallback<>(activity,
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
                              String id,
                              String userId,
                              Boolean pushNotificationsEnabled,
                              String pushNotificationToken, @NonNull CallbackDevice callback) {

        if (id == null) {
            return;
        }

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
        mRestAPI.device_patch(id, body, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    // endregion

    // region Callbacks
    // ============================================================================================================

    /**
     * Single device returned.
     */
    public interface CallbackDevice {

        public void onSuccess(RestModelDevice device);
    }

    // endregion
}