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

    // Associated error message view.
    @InjectView(R.id.dialog_error_message) TextView mDialogErrorMessage;

    // Error type.
    public static enum Type {
        Generic,
        Network,
        Unauthorized
    }

    // Specific error message resource.
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
     * Show method for the error dialog.
     *
     * @param fragmentManager Fragment manager.
     * @param activity Activity.
     * @param type Error type.
     */
    @SuppressWarnings("unused")
    public void show(FragmentManager fragmentManager, final Activity activity, Type type) {

        // Fetch the error message.
        mDialogErrorMessageResource = getErrorMessage(type);

        if (type == Type.Unauthorized) {

            // Log out the user.
            signOutUser(activity);
        }

        show(fragmentManager);
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Sign out the user and re-flow the
     * UI when this dialog is dismissed.
     *
     * @param activity Activity.
     */
    private void signOutUser(final Activity activity) {

        // Sign out user.
        AuthHelper.instance().signOut();

        // Set dismiss action.
        addOnDismissAction(new Runnable() {

            @Override
            public void run() {

                // Remove after being run.
                removeOnDismissAction(this);

                if (activity != null) {

                    // Bounce user back to the loading screen.
                    AuthHelper.instance().tryEnterMainApp(activity);
                }
            }
        });
    }

    /**
     * Get error message for associated type.
     *
     * @param type Error type.
     *
     * @return Error message.
     */
    private int getErrorMessage(Type type) {

        switch (type) {
            case Generic:
                return R.string.error_generic;

            case Network:
                return R.string.error_network;

            case Unauthorized:
                return R.string.error_unauthorized;

            default:
                return -1;
        }
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