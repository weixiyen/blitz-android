package com.blitz.app.utilities.authentication;

import android.app.Activity;
import android.content.Intent;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestCallback;
import com.blitz.app.rest_models.RestCallbacks;
import com.blitz.app.rest_models.RestModelDraft;
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

    // region Member Variables
    // ============================================================================================================

    // Instance object.
    private static AuthHelper mInstance;

    // There can be only one current draft.
    private RestModelDraft mCurrentDraft;

    // Preferences object with synced flag.
    private RestModelPreferences mPreferences;
    private boolean mPreferencesSynced;

    // endregion

    // region Public Methods
    // ============================================================================================================

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
     * Update user preferences.
     *
     * @param activity Activity for loading.
     * @param forceSync Force the network sync.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public void getPreferences(Activity activity, boolean forceSync,
                               final RestCallback<RestModelPreferences> callback) {

        // If preferences are already synced for this session.
        if (mPreferencesSynced && !forceSync) {

            // Return cached preferences.
            if (callback != null) {
                callback.onSuccess(mPreferences);
            }
        } else {

            // Sync preferences from the network.
            RestModelPreferences.sync(activity, new RestCallback<RestModelPreferences>() {

                @Override
                public void onSuccess(RestModelPreferences object) {

                    // Update preferences.
                    mPreferences = object;
                    mPreferencesSynced = true;

                    if (callback != null) {
                        callback.onSuccess(object);
                    }
                }

                @Override
                public void onFailure() {
                    super.onFailure();

                    if (callback != null) {
                        callback.onFailure();
                    }
                }
            });
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
     * @param callback By default this method automatically transitions, however
     *                 you can provide a callback that is fired once it is ready
     *                 to enter the main app, so the callee can do it's own
     *                 cleanup before transitioning to the target activity.
     */
    @SuppressWarnings("unused")
    public void tryEnterMainApp(Activity activity, EnterMainAppCallback callback) {

        // First check to see if there is a activity we want
        // to automatically jump to for debugging purposes.
        Class targetActivity = AppConfig.getJumpToActivity();

        if (targetActivity == null) {

            // If user has passed the access queue, and confirmed it.
            if (AppDataObject.hasAccess.get() && AppDataObject.hasAccessConfirmed.get()) {

                // If user is signed into the app.
                if (AppDataObject.userId.get() != null) {

                    // If user has agreed to legal road block.
                    if (AppDataObject.hasAgreedLegal.get()) {

                        // Sync state and enter
                        // appropriate screen.
                        syncAppState(activity, callback);

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

        startActivity(activity, targetActivity, callback);
    }

    /**
     * Tries to enter the main application.  This may fail
     * if the user has not yet agreed to legal policy, is
     * not signed in, or has not passed access queue.
     *
     * @param activity Target activity.
     */
    @SuppressWarnings("unused")
    public void tryEnterMainApp(Activity activity) {

        tryEnterMainApp(activity, null);
    }

    /**
     * Sign in the user.
     *
     * @param userId Id.
     * @param userName Name.
     * @param email Email.
     */
    @SuppressWarnings("unused")
    public void signIn(String userId, String userName, String email) {

        // Set id, and username from result.
        AppDataObject.userId.set(userId);
        AppDataObject.userName.set(userName);

        if (email != null) {

            AppDataObject.userEmail.set(email);
        }

        // Make sure user passed the queue.
        AppDataObject.hasAccess.set(true);
    }

    /**
     * Sign out the user.
     */
    @SuppressWarnings("unused")
    public void signOut() {

        AppData.clear(AppDataObject.userId);
        AppData.clear(AppDataObject.userCookie);
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

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Before we can enter the main app, we must synchronize the
     * application state to find out if we are currently
     * in any active drafts.
     *
     * @param activity Activity context.
     * @param callback Do our own thing when we are ready to
     *                 enter the main application.
     */
    private void syncAppState(final Activity activity, final EnterMainAppCallback callback) {

        if (mCurrentDraft != null) {

            // Drafting screen.
            startActivity(activity, DraftScreen.class, callback);

        } else {

            // Sync preferences.
            getPreferences(null, false, null);

            // Attempt to fetch active drafts for the user.
            RestModelDraft.fetchActiveDraftsForUser(activity, AppDataObject.userId.get(),
                    new RestCallbacks<RestModelDraft>() {

                        @Override
                        public void onSuccess(List<RestModelDraft> drafts) {

                            if (drafts.isEmpty()) {

                                // Main screen.
                                startActivity(activity, MainScreen.class, callback);

                            } else {

                                String draftId = drafts.get(drafts.size() - 1).getId();

                                RestModelDraft.fetchSyncedDraft(activity, draftId,
                                        new RestCallback<RestModelDraft>() {

                                            @Override
                                            public void onSuccess(RestModelDraft draft) {

                                                if (draft != null) {
                                                    setCurrentDraft(draft);

                                                    startActivity(activity, DraftScreen.class, callback);
                                                } else {
                                                    startActivity(activity, MainScreen.class, callback);
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
     * @param callback Do our own thing when we are ready to
     *                 enter the main application.
     */
    private void startActivity(final Activity activity, final Class targetActivity,
                               final EnterMainAppCallback callback) {

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

            if (callback != null) {
                callback.onReady(targetActivity);

            } else {

                Intent intent = new Intent(activity, targetActivity);

                // Base activity supports history clearing.
                if (activity instanceof BaseActivity) {

                    // Start target activity, clear the history.
                    ((BaseActivity) activity).startActivity(intent, true);
                } else {

                    // Just start activity.
                    activity.startActivity(intent);
                }

                // Always destroy the loading
                // activity as we leave.
                activity.finish();
            }
        }
    }

    // endregion

    // region Callbacks
    // ============================================================================================================

    public interface EnterMainAppCallback {

        public abstract void onReady(Class targetActivity);
    }

    // endregion
}