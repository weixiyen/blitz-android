package com.blitz.app.utilities.app;

import java.util.HashMap;

/**
 * Created by mrkcsc on 7/7/14.
 */
public class AppDataObject extends AppData {

    //==============================================================================================
    // App Data Objects - Define Here
    //==============================================================================================

    // Does the user have access to the main app (passed queue wall).
    public static final AppData hasAccess = new AppData(Boolean.class, "HAS_ACCESS");

    // Set of various keyboard heights.
    public static final AppData keyboardHeights = new AppData(HashMap.class, "KEYBOARD_HEIGHTS");

    // Set of user information.
    public static final AppData userCookie   = new AppDataObject(String.class, "USER_COOKIES");
    public static final AppData userId       = new AppDataObject(String.class, "USER_ID");
    public static final AppData userEmail    = new AppDataObject(String.class, "USER_EMAIL");
    public static final AppData userName     = new AppDataObject(String.class, "USER_NAME");
    public static final AppData userPassword = new AppDataObject(String.class, "USER_PASSWORD");

    // General settings.
    public static final AppData settingsMusicDisabled = new AppDataObject(Boolean.class, "SETTINGS_MUSIC");
    public static final AppData settingsSoundDisabled = new AppDataObject(Boolean.class, "SETTINGS_SOUND");

    // GCM related information (specifically, the application version and device registration id).
    public static final AppData gcmAppVersion     = new AppDataObject(Integer.class, "GCM_APP_VERSION");
    public static final AppData gcmRegistrationId = new AppDataObject(String.class,  "GCM_REGISTRATION_ID");

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Cannot publicly create this class.
     */
    @SuppressWarnings("unused")
    private AppDataObject() {

    }

    /**
     * Only acceptable object type.
     *
     * @param type Define object type (primitive wrapper).
     * @param key Define key.
     */
    @SuppressWarnings("unused")
    private AppDataObject(Class type, String key) {

        super(type, key);
    }
}