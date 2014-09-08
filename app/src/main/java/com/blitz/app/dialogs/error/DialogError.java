package com.blitz.app.dialogs.error;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.loading.LoadingScreen;
import com.blitz.app.utilities.android.BaseDialog;
import com.blitz.app.utilities.authentication.AuthHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/6/14. Copyright 2014 Blitz Studios
 */
public class DialogError extends BaseDialog {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.dialog_error_message) TextView mDialogErrorMessage;

    // endregion

    // region Constructors
    // =============================================================================================

    /**
     * Default constructor.  Sets up a dialog
     * using project defaults.
     *
     * @param activity Associated activity.
     */
    public DialogError(Activity activity) {
        super(activity);

        setTouchable(true);
        setDismissible(true);
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Add to default dialog show and also provide
     * a flag for if a logout should occur.
     *
     * @param showContent Show dialog content.
     * @param logout Should log out user.
     */
    public void show(boolean showContent, boolean logout) {
        super.show(showContent);

        if (logout) {

            // Provide unauthorized message.
            mDialogErrorMessage.setText(R.string.error_unauthorized);

            // Sign out user.
            AuthHelper.instance().signOut();

            // Set a dismiss listener.
            setOnDismissListener(new Runnable() {

                @Override
                public void run() {

                    // Bounce user back to the loading screen.
                    mActivity.startActivity(new Intent(mActivity, LoadingScreen.class));
                }
            });
        }
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    /**
     * Dismiss error dialog on user press.
     */
    @OnClick(R.id.dialog_error_ok) @SuppressWarnings("unused")
    public void dismissDialog() {

        hide(null);
    }

    // endregion
}