package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallbackCombined;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 8/10/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelDevice extends ObjectModel{

    // region Member Variables
    // =============================================================================================

    private String mId;

    private String mDeviceId;
    private String mUserId;

    private Boolean mPushNotificationsEnabled = null;
    private String mPushNotificationToken = null;

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Get specified device given a device id.  The model
     * will be populated based on the result.
     *
     * @param activity Activity for dialogs.
     * @param callback Completion callback.
     */
    public void get(Activity activity, final Runnable callback) {

        RestAPICallbackCombined<JsonObject> operation =
                new RestAPICallbackCombined<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

                // Populate model.
                populateModel(jsonObject);

                // Device model created.
                callback.run();
            }
        };

        // Make rest call for code.
        mRestAPI.device_get(mDeviceId, operation);
    }

    /**
     * Create a new device model object given
     * a device id.  The model will be populated
     * based on the result.
     *
     * @param activity Activity for dialogs.
     * @param callback Completion callback.
     */
    public void create(Activity activity, final Runnable callback) {

        RestAPICallbackCombined<JsonObject> operation =
                new RestAPICallbackCombined<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

                // Populate model.
                populateModel(jsonObject);

                // Device model created.
                callback.run();
            }
        };

        // Create post body.
        JsonObject body = new JsonObject();

        body.addProperty("device_id", mDeviceId);
        body.addProperty("device_type", "ANDROID");
        body.addProperty("push_notification_enabled", false);

        // Make rest call for code.
        mRestAPI.devices_post(body, operation);
    }

    /**
     * Update device model with the currently
     * set values.  Assumes the device has either
     * been created or fetched.
     *
     * @param activity Activity for dialogs.
     * @param callback Completion callback.
     */
    public void update(Activity activity, final Runnable callback) {

        RestAPICallbackCombined<JsonObject> operation =
                new RestAPICallbackCombined<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

                // Device model updated.
                callback.run();
            }
        };

        // Create object holding values to replace.
        JsonObject replace = new JsonObject();

        if (mPushNotificationsEnabled != null) {

            // Update push notifications if needed.
            replace.addProperty("push_notification_enabled", mPushNotificationsEnabled);
        }

        if (mPushNotificationToken != null) {

            // Update token if needed.
            replace.addProperty("push_notification_token", mPushNotificationToken);
        }

        if (mUserId != null) {

            // Update user id if needed.
            replace.addProperty("user_id", mUserId);
        }

        // Create body.
        JsonObject body = new JsonObject();

        // Add replace object.
        body.add("replace", replace);

        // Make rest call for code.
        mRestAPI.device_patch(mId, body, operation);
    }

    /**
     * Set the push notification token. In android
     * this is referred to as the registration id.
     *
     * @param pushNotificationToken Token.
     */
    @SuppressWarnings("unused")
    public void setPushNotificationToken(String pushNotificationToken) {
        mPushNotificationToken = pushNotificationToken;
    }

    /**
     * Set whether this device has push notifications enabled.
     *
     * @param pushNotificationsEnabled Are push notifications enabled.
     */
    @SuppressWarnings("unused")
    public void setPushNotificationsEnabled(boolean pushNotificationsEnabled) {
        mPushNotificationsEnabled = pushNotificationsEnabled;
    }

    /**
     * Set the device id, this is needed for any
     * of the REST calls.
     *
     * @param deviceId Target device id.
     */
    @SuppressWarnings("unused")
    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    /**
     * Set the user id.
     *
     * @param userId User id.
     */
    @SuppressWarnings("unused")
    public void setUserId(String userId) {
        mUserId = userId;
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Populate the model.  Should be called with
     * a json result object that is populated
     * with device model results.
     */
    private void populateModel(JsonObject jsonObject) {

        JsonElement element;

        // Fetch id and device id.
        mId = jsonObject.get("id").getAsString();
        mDeviceId = jsonObject.get("device_id").getAsString();

        // Fetch push notification info.
        mPushNotificationsEnabled = jsonObject.get("push_notification_enabled").getAsBoolean();

        element = jsonObject.get("push_notification_token");

        if (!element.isJsonNull()) {

            // Fetch notification token.
            mPushNotificationToken = element.getAsString();
        }

        element = jsonObject.get("user_id");

        if (!element.isJsonNull()) {

            // Fetch user id.
            mUserId = element.getAsString();
        }
    }

    // endregion
}