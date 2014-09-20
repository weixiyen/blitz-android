package com.blitz.app.utilities.app;

import java.util.HashMap;

/**
 * Created by mrkcsc on 7/7/14. Copyright 2014 Blitz Studios
 */
public class AppDataObject {

    //==============================================================================================
    // App Data Objects - Define Here
    //==============================================================================================

    // Does the user have access to the main app (passed queue wall).
    public static final AppData<Boolean> hasAccess = AppData.bool("HAS_ACCESS");

    // Has the user agreed to the legal terms of this app.
    public static final AppData<Boolean> hasAgreedLegal = AppData.bool("HAS_AGREED_LEGAL");

    // Set of various keyboard heights.
    public static final AppData<HashMap<String, String>> keyboardHeights = AppData.dictionary("KEYBOARD_HEIGHTS");

    // Set of user information.
    public static final AppData<String> userCookie   = AppData.string("USER_COOKIES");
    public static final AppData<String> userId       = AppData.string("USER_ID");
    public static final AppData<String> userEmail    = AppData.string("USER_EMAIL");
    public static final AppData<String> userName     = AppData.string("USER_NAME");
    public static final AppData<String> userPassword = AppData.string("USER_PASSWORD");

    // General settings.
    public static final AppData<Boolean> settingsMusicDisabled = AppData.bool("SETTINGS_MUSIC");
    public static final AppData<Boolean> settingsSoundDisabled = AppData.bool("SETTINGS_SOUND");

    // GCM related information (specifically, the application version and device registration id).
    public static final AppData<Integer> gcmAppVersion             = AppData.integer("GCM_APP_VERSION");
    public static final AppData<String>  gcmRegistrationId         = AppData.string("GCM_REGISTRATION_ID");
    public static final AppData<Boolean> gcmRegistrationPersisted  = AppData.bool("GCM_REGISTRATION_PERSISTED");
    public static final AppData<String>  gcmDeviceId               = AppData.string("GCM_DEVICE_ID");

    // Cached content used for a cleaner looking UI.
    public static final AppData<HashMap<String, String>> cachedImageUrls = AppData.dictionary("CACHED_IMAGE_URLS");
    public static final AppData<HashMap<String, String>> cachedText = AppData.dictionary("CACHED_TEXT");
}