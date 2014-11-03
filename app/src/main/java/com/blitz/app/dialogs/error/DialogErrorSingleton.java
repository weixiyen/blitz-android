package com.blitz.app.dialogs.error;

import android.app.Activity;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 11/2/2014. Copyright 2014 Blitz Studios
 *
 * TODO: Pair FM/Activity with the queue.
 * TODO: Make FM/Activity weak.
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

    // Most recent instance of fragment manager.
    private FragmentManager mFragmentManager;

    // Most recent instance of activity.
    private Activity mActivity;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Show generic error dialog.
     */
    @SuppressWarnings("unused")
    public static void showGeneric(FragmentManager fragmentManager) {

        // Add generic type to queue.
        instance().addDialogToQueue(fragmentManager, null, DialogError.Type.Generic);
    }

    /**
     * Show network error dialog.
     */
    @SuppressWarnings("unused")
    public static void showNetwork(FragmentManager fragmentManager) {

        // Add network dialog to queue.
        instance().addDialogToQueue(fragmentManager, null, DialogError.Type.Network);
    }

    /**
     * Show unauthorized error dialog.
     */
    @SuppressWarnings("unused")
    public static void showUnauthorized(FragmentManager fragmentManager, Activity activity) {

        // Add unauthorized dialog to queue.
        instance().addDialogToQueue(fragmentManager, activity, DialogError.Type.Unauthorized);
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Add a dialog to the queue.
     *
     * @param fragmentManager Associated fragment manager.
     * @param activity Associated activity.
     * @param type Error type.
     */
    private void addDialogToQueue(FragmentManager fragmentManager, Activity activity, DialogError.Type type) {

        // Set fragment manager.
        mFragmentManager = fragmentManager;

        // Set activity.
        mActivity = activity;

        // Add to queue.
        mQueuedDialogErrors.add(type);

        // Show if needed.
        tryShowDialog();
    }

    /**
     * Try showing the next error dialog that has been
     * requested by the user.
     */
    private void tryShowDialog() {

        if (mQueuedDialogErrors.size() > 0 && !mDialogErrorShowing) {

            // Fetch the last dialog type in the queue.
            DialogError.Type type = mQueuedDialogErrors.get(mQueuedDialogErrors.size() - 1);

            // Show the actual dialog.
            mDialogError.show(mFragmentManager, mActivity, type);
            mDialogErrorShowing = true;

            if (type == DialogError.Type.Unauthorized) {

                // Remove all.
                mQueuedDialogErrors.clear();

            } else {

                // Pop from end of the queue.
                mQueuedDialogErrors.remove(mQueuedDialogErrors.size() - 1);
            }
        }
    }

    /**
     * Initialize this singleton.  Internally it contains
     * a single error dialog that is powered by a data queue.
     */
    private void initializeDialogSingleton() {

        // Initialize list of error dialogs to show.
        mQueuedDialogErrors = new ArrayList<DialogError.Type>();

        // Initialize the internal error dialog.
        mDialogError = new DialogError();
        mDialogError.addOnDismissAction(new Runnable() {

            @Override
            public void run() {

                // No longer showing a dialog.
                mDialogErrorShowing = false;

                // Show next in line.
                tryShowDialog();
            }
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