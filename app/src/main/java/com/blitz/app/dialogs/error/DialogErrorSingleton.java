package com.blitz.app.dialogs.error;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.blitz.app.utilities.animations.AnimHelper;
import com.blitz.app.utilities.blitz.BlitzDelay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 11/2/2014. Copyright 2014 Blitz Studios
 */
public class DialogErrorSingleton {

    // region Member Variables
    // ============================================================================================================

    // Instance object.
    private static DialogErrorSingleton mInstance;

    // Single error dialog.
    private DialogError mDialogError;
    private boolean mDialogErrorShowing;

    // List of dialog errors queued up.
    private List<DialogError.Type> mQueuedDialogErrors;

    // Activity reference.
    private Activity mActivity;
    private String mActivityClass;

    // Handler for delayed show.
    private Handler mShowDialogHandler;

    // endregion

    // region Config Methods
    // ============================================================================================================

    /**
     * Add current activity, and check the queue.
     *
     * @param activity Target activity.
     */
    public static void configAddActivity(Activity activity) {

        // If this is new activity.
        if (instance().mActivityClass != null &&
           !instance().mActivityClass.equals(activity.getLocalClassName())) {

            // Safe to clear out the showing flag.
            instance().mDialogErrorShowing = false;
        }

        // Set the activity.
        instance().mActivity = activity;
        instance().mActivityClass = activity.getLocalClassName();

        // Re-initialize the singleton.
        instance().initializeDialogSingleton();

        // Try showing dialog.
        instance().tryShowDialog();
    }

    /**
     * Remove current activity.
     */
    public static void configRemoveActivity() {

        // Clear the activity.
        instance().mActivity = null;

        // Remove any pending show handler.
        BlitzDelay.remove(instance().mShowDialogHandler);
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Show generic error dialog.
     */
    @SuppressWarnings("unused")
    public static void showGeneric() {

        // Add generic type to queue.
        instance().addDialogToQueue(DialogError.Type.Generic);
    }

    /**
     * Show network error dialog.
     */
    @SuppressWarnings("unused")
    public static void showNetwork() {

        // Add network dialog to queue.
        instance().addDialogToQueue(DialogError.Type.Network);
    }

    /**
     * Show unauthorized error dialog.
     */
    @SuppressWarnings("unused")
    public static void showUnauthorized() {

        // Add unauthorized dialog to queue.
        instance().addDialogToQueue(DialogError.Type.Unauthorized);
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Add a dialog to the queue.
     *
     * @param type Error type.
     */
    private void addDialogToQueue(DialogError.Type type) {

        // Add to queue.
        mQueuedDialogErrors.add(0, type);

        // Show if needed.
        tryShowDialog();
    }

    /**
     * Try showing the next error dialog that has been
     * requested by the user.
     */
    private void tryShowDialog() {

        // Remove pending handler.
        BlitzDelay.remove(mShowDialogHandler);

        // Try to show on a short delay (cleaner UI).
        mShowDialogHandler = BlitzDelay.postDelayed(() -> {

            if (mQueuedDialogErrors.size() > 0 && !mDialogErrorShowing) {

                int lastQueueIndex = mQueuedDialogErrors.size() - 1;

                // Fetch the last dialog type in the queue.
                DialogError.Type type = mQueuedDialogErrors.get(lastQueueIndex);

                if (mActivity != null) {

                    // Show the actual dialog.
                    mDialogError.show(((FragmentActivity)mActivity).getSupportFragmentManager(), mActivity, type);
                    mDialogErrorShowing = true;

                    if (type == DialogError.Type.Unauthorized) {

                        // Remove all.
                        mQueuedDialogErrors.clear();

                    } else {

                        // Pop from end of the queue.
                        mQueuedDialogErrors.remove(lastQueueIndex);
                    }
                }
            }

        }, AnimHelper.getConfigAnimTimeStandard());
    }

    /**
     * Initialize this singleton.  Internally it contains
     * a single error dialog that is powered by a data queue.
     */
    private void initializeDialogSingleton() {

        // Initialize list of error dialogs to show.
        if (mQueuedDialogErrors == null) {
            mQueuedDialogErrors = new ArrayList<>();
        }

        // Initialize the internal error dialog.
        mDialogError = new DialogError();
        mDialogError.addOnDismissAction(() -> {

            // No longer showing a dialog.
            mDialogErrorShowing = false;

            // Show next in line.
            tryShowDialog();
        });
    }

    /**
     * Standard singleton with initialization.
     *
     * @return Singleton instance.
     */
    private static DialogErrorSingleton instance() {

        if (mInstance == null) {
            synchronized (DialogErrorSingleton.class) {

                if (mInstance == null) {
                    mInstance = new DialogErrorSingleton();
                    mInstance.initializeDialogSingleton();
                }
            }
        }

        return mInstance;
    }

    // endregion
}