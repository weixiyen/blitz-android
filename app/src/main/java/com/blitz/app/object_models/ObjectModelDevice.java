package com.blitz.app.object_models;

import android.app.Activity;

/**
 * Created by mrkcsc on 8/10/14.
 */
public class ObjectModelDevice extends ObjectModel {

    public enum DeviceType {
        IOS,
        ANDROID
    }

    private String mDeviceId;
    private DeviceType mDeviceType;
    private String mUserId;
    private Boolean mPushNotificationsEnabled;
    private String mPushNotificationToken;

    /**
     * Get specified device given a device id.  The model
     * will be populated based on the result.
     *
     * @param deviceId Specified device id.
     * @param activity Activity for dialogs.
     * @param callback Completion callback.
     */
    public void get(String deviceId, Activity activity, final Runnable callback) {

        throw new UnsupportedOperationException();
    }

    /**
     * Create a new device model object given
     * a device id.  The model will be populated
     * based on the result.
     *
     * @param deviceId Specified device id.
     * @param activity Activity for dialogs.
     * @param callback Completion callback.
     */
    public void create(String deviceId, Activity activity, final Runnable callback) {

        throw new UnsupportedOperationException();
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

        throw new UnsupportedOperationException();
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
}