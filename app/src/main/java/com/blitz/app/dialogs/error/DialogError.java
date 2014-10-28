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
    // ============================================================================================================

    @InjectView(R.id.dialog_error_message) TextView mDialogErrorMessage;

    // endregion

    // region Constructors
    // ============================================================================================================

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
    // ============================================================================================================

    /**
     * Show an error dialog for a user
     * who cannot connect to the network.
     */
    public void showNetworkError() {
        super.show(true);

        // Set error text.
        setText(R.string.error_network);
    }

    /**
     * Show an error dialog for a user
     * who is not authorized.  Also log
     * that user out.
     */
    public void showUnauthorized() {
        super.show(true);

        // Provide unauthorized message.
        setText(R.string.error_unauthorized);

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

    /**
     * Set text of the error dialog.
     *
     * @param textResId Resource id for text.
     */
    public void setText(int textResId) {

        // Set the text.
        mDialogErrorMessage.setText(textResId);
    }

    // endregion

    // region Click Methods
    // ============================================================================================================

    /**
     * Dismiss error dialog on user press.
     */
    @OnClick(R.id.dialog_error_ok) @SuppressWarnings("unused")
    public void dismissDialog() {

        hide(null);
    }

    // endregion
}