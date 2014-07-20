package com.blitz.app.utilities.authentication;

import com.blitz.app.utilities.app.AppDataObject;

/**
 * Created by mrkcsc on 7/20/14.
 */
public class AuthHelper {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Instance object.
    private static AuthHelper mInstance;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Is user signed in.
     *
     * @return Yes/No.
     */
    public static boolean isSignedIn() {
        return AppDataObject.userId.getString() != null;
    }

    /**
     * Is user past queue screen.
     *
     * @return Yes/No.
     */
    public static boolean isPassedQueue() {
        return AppDataObject.hasAccess.getBoolean();
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Fetch singleton.
     *
     * @return Singleton object.
     */
    private static AuthHelper instance() {

        if (mInstance == null) {
            synchronized (AuthHelper.class) {
                if (mInstance == null) {
                    mInstance = new AuthHelper();
                }
            }
        }

        return mInstance;
    }
}