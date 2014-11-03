package com.blitz.app.dialogs.error;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseDialogFragment;
import com.blitz.app.utilities.authentication.AuthHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/6/14. Copyright 2014 Blitz Studios
 */
public class DialogError extends BaseDialogFragment {

    // region Member Variables
    // ============================================================================================================

    @InjectView(R.id.dialog_error_message) TextView mDialogErrorMessage;

    private Integer mDialogErrorMessageResource;

    // endregion

    @Override
    protected void onViewCreated(View view) {

        // Set the error message.
        if (mDialogErrorMessage != null && mDialogErrorMessageResource != null) {
            mDialogErrorMessage.setText(mDialogErrorMessageResource);
        }
    }

    // region Public Methods
    // ============================================================================================================

    /**
     * Show an error dialog for a user
     * who cannot connect to the network.
     */
    public void showNetworkError(FragmentManager fragmentManager) {

        // Set network error message.
        mDialogErrorMessageResource = R.string.error_network;

        show(fragmentManager);
    }

    /**
     * Show an error dialog for a user
     * who is not authorized.  Also log
     * that user out.
     */
    public void showUnauthorized(FragmentManager fragmentManager, final Activity activity) {

        // Provide unauthorized message.
        mDialogErrorMessageResource = R.string.error_unauthorized;

        // Sign out user.
        AuthHelper.instance().signOut();

        // Set dismiss action.
        addOnDismissAction(new Runnable() {

            @Override
            public void run() {

                // Remove after being run.
                removeOnDismissAction(this);

                // Bounce user back to the loading screen.
                AuthHelper.instance().tryEnterMainApp(activity);
            }
        });

        show(fragmentManager);
    }

    // endregion

    // region Click Methods
    // ============================================================================================================

    /**
     * Dismiss error dialog on user press.
     */
    @OnClick(R.id.dialog_error_ok) @SuppressWarnings("unused")
    public void dismissDialog() {

        // Dismiss the dialog.
        dismiss();
    }

    // endregion
}