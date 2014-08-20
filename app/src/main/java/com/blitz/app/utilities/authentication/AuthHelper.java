package com.blitz.app.utilities.authentication;

import android.content.Intent;

import com.blitz.app.object_models.ObjectModelDraft;
import com.blitz.app.screens.access_queue.AccessQueueScreen;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.screens.sign_up.SignUpScreenTerms;
import com.blitz.app.screens.splash.SplashScreen;
import com.blitz.app.utilities.android.BaseActivity;
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
     * Is this device registered with
     * the servers.
     *
     * @return Is the device registered.
     */
    public static boolean isDeviceRegistered() {

        return AppDataObject.gcmRegistrationPersisted.getBoolean();
    }

    /**
     * Grant access to the user.  This is done after
     * they have entered a valid access code or
     * reached the front of the queue.
     *
     * @param activity Target activity.
     */
    public static void grantAccess(BaseActivity activity) {

        // User now has access.
        AppDataObject.hasAccess.set(true);

        // Transition to splash screen, clear history.
        activity.startActivity(new Intent(activity, SplashScreen.class), true);
    }

    /**
     * Tries to enter the main application.  This may fail
     * if the user has not yet agreed to legal policy, is
     * not signed in, or has not passed access queue.
     *
     * @param activity Target activity.
     */
    public static void tryEnterMainApp(BaseActivity activity) {

        // If user has passed the access queue.
        if (AppDataObject.hasAccess.getBoolean()) {

            // If user is signed into the app.
            if (AppDataObject.userId.getString() != null) {

                // If user has agreed to legal road block.
                if (AppDataObject.hasAgreedLegal.getBoolean()) {

                    // Enter main screen of the app.
                    activity.startActivity(new Intent(activity, MainScreen.class), true);

                } else {

                    // User is blocked on legal screen.
                    activity.startActivity(new Intent(activity, SignUpScreenTerms.class), true);
                }

            } else {

                // User must go to splash screen and sign-in/register.
                activity.startActivity(new Intent(activity, SplashScreen.class));
            }

        } else {

            // User is blocked on Queue screen.
            activity.startActivity(new Intent(activity, AccessQueueScreen.class));
        }
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