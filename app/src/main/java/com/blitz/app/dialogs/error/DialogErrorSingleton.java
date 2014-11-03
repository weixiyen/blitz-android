package com.blitz.app.dialogs.error;

import android.support.v4.app.FragmentManager;

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

    // Most recent instance of fragment manager.
    private FragmentManager mFragmentManager;

    private List<Integer> mErrorMessageList;

    // endregion

    public static DialogErrorSingleton instance() {

        if (mInstance == null) {
            synchronized (DialogErrorSingleton.class) {

                if (mInstance == null) {
                    mInstance = new DialogErrorSingleton();

                    // Initialize list of error dialogs to show.
                    mInstance.mErrorMessageList = new ArrayList<Integer>();

                    // Initialize the internal error dialog.
                    mInstance.mDialogError = new DialogError();
                    mInstance.mDialogError.addOnDismissAction(new Runnable() {

                        @Override
                        public void run() {

                            if (mInstance.mErrorMessageList.size() > 0) {
                                mInstance.mErrorMessageList.remove(mInstance.mErrorMessageList.size() - 1);
                            }
                        }
                    });
                }
            }
        }

        return mInstance;
    }

    public void show(FragmentManager fragmentManager) {

        // Set fragment manager.
        mFragmentManager = fragmentManager;
    }
}
