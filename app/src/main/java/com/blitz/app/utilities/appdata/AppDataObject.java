package com.blitz.app.utilities.appdata;

/**
 * Created by mrkcsc on 7/7/14.
 */
public class AppDataObject extends AppData {

    //==============================================================================================
    // App Data Objects - Define Here
    //==============================================================================================

    // Does the user have access to the main app (passed queue wall).
    public static final AppData hasAccess = new AppData(Boolean.class, "HAS_ACCESS");

    public static final AppData userId       = new AppDataObject(String.class, "USER_ID");
    public static final AppData userEmail    = new AppDataObject(String.class, "USER_EMAIL");
    public static final AppData userName     = new AppDataObject(String.class, "USER_NAME");
    public static final AppData userPassword = new AppDataObject(String.class, "USER_PASSWORD");

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