package com.blitz.app.utilities.authentication;

import android.content.Intent;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelCallbacks;
import com.blitz.app.rest_models.RestModelPreferences;
import com.blitz.app.screens.access_queue.AccessQueueScreen;
import com.blitz.app.screens.draft.DraftScreen;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.screens.sign_up.SignUpScreenLegal;
import com.blitz.app.screens.splash.SplashScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.sound.SoundHelper;

import java.util.List;

/**
 * Created by mrkcsc on 7/20/14. Copyright 2014 Blitz Studios
 */
public class AuthHelper {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Instance object.
    private static AuthHelper mInstance;

    // There can be only one current draft.
    private RestModelDraft mCurrentDraft;

    private RestModelPreferences mPreferences;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Fetch singleton.
     *
     * @return Singleton object.
     */
    public static AuthHelper instance() {

        if (mInstance == null) {
            synchronized (SoundHelper.class) {

                if (mInstance == null) {
                    mInstance = new AuthHelper();
                }
            }
        }

        return mInstance;
    }

    /**
     * Fetch preferences
     */
    public RestModelPreferences getPreferences() {
        if(mPreferences == null) {
            return RestModelPreferences.defaultPreferences();
        } else {
            return mPreferences;
        }
    }

    /**
     * Fetch associated user id.
     */
    @SuppressWarnings("unused")
    public String getUserId() {

        return AppDataObject.userId.get();
    }

    /**
     * Is this device registered with
     * the servers.
     *
     * @return Is the device registered.
     */
    @SuppressWarnings("unused")
    public boolean isDeviceRegistered() {

        return AppDataObject.gcmRegistrationPersisted.get();
    }

    /**
     * Grant access to the user.  This is done after
     * they have entered a valid access code or
     * reached the front of the queue.
     *
     * @param activity Target activity.
     */
    @SuppressWarnings("unused")
    public void grantAccess(BaseActivity activity) {

        // User now has access.
        AppDataObject.hasAccess.set(true);

        // Transition to splash screen, clear history.
        activity.startActivity(new Intent(activity, SplashScreen.class), true);
    }

    /**
     * User accepted legal agreements.
     */
    @SuppressWarnings("unused")
    public void setLegalAccepted() {

        // User has accepted legal terms.
        AppDataObject.hasAgreedLegal.set(true);
    }

    /**
     * Tries to enter the main application.  This may fail
     * if the user has not yet agreed to legal policy, is
     * not signed in, or has not passed access queue.
     *
     * @param activity Target activity.
     */
    @SuppressWarnings("unused")
    public void tryEnterMainApp(BaseActivity activity) {

        // First check to see if there is a activity we want
        // to automatically jump to for debugging purposes.
        Class targetActivity = AppConfig.getJumpToActivity();

        // Stop all music.
        SoundHelper.instance().stopMusic();

        if (targetActivity == null) {

            // If user has passed the access queue.
            if (AppDataObject.hasAccess.get()) {

                // If user is signed into the app.
                if (AppDataObject.userId.get() != null) {

                    // If user has agreed to legal road block.
                    if (AppDataObject.hasAgreedLegal.get()) {

                        // Sync state and enter
                        // appropriate screen.
                        syncAppState(activity);

                    } else {

                        // User is blocked on legal screen.
                        targetActivity = SignUpScreenLegal.class;
                    }

                } else {

                    // User must go to splash screen and sign-in/register.
                    targetActivity = SplashScreen.class;
                }

            } else {

                // User is blocked on Queue screen.
                targetActivity = AccessQueueScreen.class;
            }
        }

        startActivity(activity, targetActivity);
    }

    /**
     * Sign in the user.
     *
     * @param userId Id.
     * @param userName Name.
     * @param email Email.
     * @param password Password.
     */
    @SuppressWarnings("unused")
    public void signIn(String userId, String userName, String email, String password) {

        // Set id, and username from result.
        AppDataObject.userId.set(userId);
        AppDataObject.userName.set(userName);

        if (email != null) {

            AppDataObject.userEmail.set(email);
        }

        AppDataObject.userPassword.set(password);

        // Make sure user passed the queue.
        AppDataObject.hasAccess.set(true);
    }

    /**
     * Sign out the user.
     */
    @SuppressWarnings("unused")
    public void signOut() {

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
    @SuppressWarnings("unused")
    public RestModelDraft getCurrentDraft() {

        return mCurrentDraft;
    }

    /**
     * Sets the current draft model object.
     *
     * @param objectModelDraft Draft model object.
     */
    @SuppressWarnings("unused")
    public void setCurrentDraft(RestModelDraft objectModelDraft) {

        mCurrentDraft = objectModelDraft;
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Before we can enter the main app, we must synchronize the
     * application state to find out if we are currently
     * in any active drafts.
     *
     * @param activity Activity context.
     */
    private void syncAppState(final BaseActivity activity) {

        if (mCurrentDraft != null) {

            // Drafting screen.
            startActivity(activity, DraftScreen.class);

        } else {

            // Attempt to fetch active drafts for the user.
            RestModelDraft.fetchActiveDraftsForUser(activity, AppDataObject.userId.get(),
                    new RestModelCallbacks<RestModelDraft>() {

                        @Override
                        public void onSuccess(List<RestModelDraft> drafts) {

                            if (drafts.isEmpty()) {

                                // Main screen.
                                startActivity(activity, MainScreen.class);

                            } else {

                                String draftId = drafts.get(drafts.size() - 1).getId();

                                RestModelDraft.fetchSyncedDraft(activity, draftId,
                                        new RestModelCallback<RestModelDraft>() {

                                            @Override
                                            public void onSuccess(RestModelDraft draft) {

                                                if (draft != null) {
                                                    setCurrentDraft(draft);

                                                    startActivity(activity, DraftScreen.class);
                                                } else {
                                                    startActivity(activity, MainScreen.class);
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    /**
     * Start an activity, clear history.
     *
     * @param activity Activity from.
     * @param targetActivity Activity to.
     */
    private void startActivity(BaseActivity activity, Class targetActivity) {

        if (targetActivity != null) {

            if (targetActivity.equals(DraftScreen.class)) {

                // Start the fast music.
                SoundHelper.instance().startMusic(R.raw.music_fast_loop_0,
                        R.raw.music_fast_loop_n);
            } else {

                // Play the lobby music after loading.
                SoundHelper.instance().startMusic(R.raw.music_lobby_loop0,
                        R.raw.music_lobby_loopn);
            }

            // Disable music if needed.
            SoundHelper.instance().setMusicDisabled
                    (AppDataObject.settingsMusicDisabled.get());

            // Start target activity, clear the history.
            activity.startActivity(new Intent(activity, targetActivity), true);

            // Always destroy the loading
            // activity as we leave.
            activity.finish();
        }
    }
}