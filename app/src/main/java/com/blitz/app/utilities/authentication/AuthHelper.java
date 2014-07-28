package com.blitz.app.utilities.authentication;

import com.blitz.app.models.objects.ObjectModelDraft;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.app.AppDataObject;

/**
 * Created by mrkcsc on 7/20/14.
 */
public class AuthHelper {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // There can be only one current draft.
    private static ObjectModelDraft mCurrentDraft;

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

    /**
     * Sign in the user.
     *
     * @param userId Id.
     * @param userName Name.
     * @param email Email.
     * @param password Password.
     */
    public static void signIn(String userId, String userName, String email, String password) {

        // Set id, and username from result.
        AppDataObject.userId.set(userId);
        AppDataObject.userName.set(userName);

        // Rest is user provided.
        AppDataObject.userEmail.set(email);
        AppDataObject.userPassword.set(password);

        // Make sure user passed the queue.
        AppDataObject.hasAccess.set(true);
    }

    /**
     * Sign out the user.
     */
    public static void signOut() {

        AppData.clear(AppDataObject.userId);
        AppData.clear(AppDataObject.userName);

        AppData.clear(AppDataObject.userEmail);
        AppData.clear(AppDataObject.userPassword);
    }

    /**
     * Fetch the current draft model object.
     *
     * @return Current draft object.
     */
    public static ObjectModelDraft getCurrentDraft() {
        if (mCurrentDraft == null) {
            mCurrentDraft = new ObjectModelDraft();
        }

        return mCurrentDraft;
    }
}