package com.blitz.app.utilities.android;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blitz.app.R;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import java.util.concurrent.ConcurrentHashMap;

import butterknife.ButterKnife;

/**
 * Created by Miguel Gaeta on 6/29/14.
 *
 * TODO: Split animation related code into BaseDialogAnimations
 * TODO: Power this with an implementation for accessing the underlying popup (PopWindow/DialogFragment)
 */
public class BaseDialog {

    // region Member Variables
    // ============================================================================================================

    // State of dialog content.
    private enum DialogContentState {
        HIDDEN, ANIMATING, VISIBLE
    }

    // Constants.
    private static final int ANIMATION_TIME = 250;

    // All dialogs should have a content view.
    private ViewGroup mDialogContent;
    private DialogContentState mDialogContentState;

    // Parent activity.
    protected Activity mActivity;

    // Hide related variables.
    private boolean mHidePending;
    private HideListener mHideListener;

    // Popup windows are very volatile in nature and cannot exist
    // outside of the context of the activity they are created in.
    // As such we do not hold a direct reference to the associated
    // popup window.  Instead it is stored inside a concurrent hash
    // map keyed by a system identifier (this variable).  The base
    // activity controls the lifecycle of the popups, ensuring they
    // cannot exist outside of their valid lifecycle.  This class
    // therefore ensures to not hold onto references of the popup
    // window and ensures it exists via null checks.
    private int mPopupWindowKey;

    // List of all active popup windows.
    private static final ConcurrentHashMap<Integer, PopupWindow> mPopupWindows
            = new ConcurrentHashMap<>();

    // endregion

    // region Constructors
    // ============================================================================================================

    /**
     * Do not allow empty constructor.
     */
    @SuppressWarnings("unused")
    private BaseDialog() {

    }

    /**
     * Default constructor.  Sets up a dialog
     * using project defaults.
     *
     * @param activity Associated activity.
     */
    public BaseDialog(Activity activity) {

        // Use reflection to fetch the associated view resource id and set content view.
        int layoutResourceId = ReflectionHelper.getResourceId(this.getClass(), R.layout.class);

        // Now inflate the associated view.
        View layoutOfPopup = activity.getLayoutInflater().inflate(layoutResourceId, null);

        // Save the activity.
        mActivity = activity;

        // Create full size popup window.
        PopupWindow popupWindow = new PopupWindow(layoutOfPopup,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        // Set the key for this window.
        mPopupWindowKey = System.identityHashCode(popupWindow);

        // Insert into static list of popup windows.
        mPopupWindows.put(mPopupWindowKey, popupWindow);

        // Inject butter knife into the content view.
        ButterKnife.inject(this, popupWindow.getContentView());

        // Default dismiss listener, with no custom
        // callback provided by default.
        setOnDismissListener(null);

        setupDialogContent();
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Force dismiss all popup windows.  This ensures
     * that there can be no leaks or crashes when
     * activities are paused.
     */
    @SuppressWarnings("unused")
    public static void dismissAllPopups() {

        // Iterate over all popup windows.
        for (PopupWindow popupWindow : mPopupWindows.values()) {

            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        }

        // Remove all windows.
        mPopupWindows.clear();
    }

    /**
     * Set a dismiss listener.
     *
     * @param onDismissListener Dismiss listener.
     */
    @SuppressWarnings("unused")
    public void setOnDismissListener(final Runnable onDismissListener) {

        // Assign the dismiss listener.
        if (getPopupWindow() != null) {
            getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {

                    if (getPopupWindow() != null) {

                        // Remove from the global list of popups.
                        mPopupWindows.remove(System.identityHashCode(getPopupWindow()));

                        // Reset injections.
                        ButterKnife.reset(this);

                        if (onDismissListener != null) {
                            onDismissListener.run();
                        }
                    }
                }
            });
        }
    }

    /**
     * Show the popup.
     *
     * @param showContent Should also display it's content?
     */
    @SuppressWarnings("unused")
    public void show(final boolean showContent) {

        // If window exists and not already showing.
        if (getPopupWindow() != null && !getPopupWindow().isShowing()) {

            // Hide the keyboard.
            KeyboardUtility.hideKeyboard(mActivity);

            try {

                mActivity.findViewById(android.R.id.content).post(() -> {

                    // If we are jellybean or higher, check if activity is destroyed.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                        if (mActivity.isDestroyed()) {

                            return;
                        }
                    }

                    if (getPopupWindow() != null && mActivity != null && !mActivity.isFinishing()) {

                        // Show at top corner of the window.
                        getPopupWindow().showAtLocation(mActivity.getWindow()
                                .getDecorView(), Gravity.NO_GRAVITY, 0, 0);

                        // Try to show dialog content.
                        tryShowDialogContent(showContent);
                    }
                });

            } catch (Exception ignored) {  }

        } else {

            // Try to show dialog content.
            tryShowDialogContent(showContent);
        }
    }

    /**
     * Additional hide method that provides a
     * callback function.
     *
     * @param hideListener Callback function.
     */
    @SuppressWarnings("unused")
    public void hide(HideListener hideListener) {

        // Assign callback, if changed.
        if (mHideListener != hideListener) {
            mHideListener = hideListener;
        }

        switch (mDialogContentState) {
            case HIDDEN:

                // No longer pending.
                mHidePending = false;

                try {

                    // Hide immediately.
                    if (getPopupWindow() != null && getPopupWindow().isShowing()) {
                        getPopupWindow().dismiss();
                    }

                } catch (IllegalArgumentException | IllegalStateException ignored) { }

                // Execute callback.
                if (mHideListener != null) {
                    mHideListener.didHide();
                }

                break;
            case ANIMATING:

                // Set pending hide.
                mHidePending = true;

                break;
            case VISIBLE:

                // Set pending hide.
                mHidePending = true;

                // Hide dialog content.
                toggleDialogContent(false);

                break;
        }
    }

    /**
     * Set whether this dialog can be touched.  Only useable
     * by direct dialog subclasses.
     *
     * @param touchable Is dialog touchable.
     */
    @SuppressWarnings("unused")
    public void setTouchable(boolean touchable) {

        if (getPopupWindow() != null) {
            getPopupWindow().setOutsideTouchable(touchable);
            getPopupWindow().setTouchable(touchable);
            getPopupWindow().setFocusable(touchable);
        }
    }

    /**
     * Popup is only dismissible if you provide
     * a background drawable. http://bit.ly/1oaUSSy
     *
     * @param dismissible Boolean toggle.
     */
    @SuppressWarnings("unused")
    public void setDismissible(boolean dismissible) {

        // Provide a drawable if dismissible.
        if (getPopupWindow() != null) {
            getPopupWindow().setBackgroundDrawable
                    (dismissible ? new ColorDrawable(Color.TRANSPARENT) : null);
        }
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Try to show the dialog content.
     *
     * @param showContent Show flag.
     */
    private void tryShowDialogContent(boolean showContent) {

        if (showContent && mDialogContentState == DialogContentState.HIDDEN) {

            // Show the content.
            toggleDialogContent(true);
        }
    }

    /**
     * Toggle the dialog content view.
     *
     * @param visible Show or hide flag.
     */
    private void toggleDialogContent(final boolean visible) {

        // Set the state to be animating.
        mDialogContentState = DialogContentState.ANIMATING;

        // Make sure the loading view is visible.
        mDialogContent.setVisibility(View.VISIBLE);

        // Perform the animation.
        AnimHelperFade.setAlpha(mDialogContent,
                visible ? 0f : 1f,
                visible ? 1f : 0f, ANIMATION_TIME, () -> {

                    // Update the state flag.
                    mDialogContentState = visible ?
                            DialogContentState.VISIBLE :
                            DialogContentState.HIDDEN;

                    // If hide pending.
                    if (mHidePending) {

                        if (visible) {

                            // Hide it, and leave pending flag on.
                            toggleDialogContent(false);

                        } else {

                            hide(mHideListener);
                        }
                    }
                });
    }

    /**
     * Setup the dialog content view (find it),
     * hide it, and set click listeners, etc.
     */
    private void setupDialogContent() {

        if (getPopupWindow() == null) {

            return;
        }

        // Fetch the content window.  This is required of all dialogs.
        mDialogContent = ButterKnife.findById(getPopupWindow().getContentView(),
                R.id.dialog_content);

        if (mDialogContent == null) {

            // Throw exception if content view not found.
            throw new RuntimeException("Dialog must have a content view.");
        }

        mDialogContent.setVisibility(View.GONE);
        mDialogContent.setOnClickListener(view -> {

            // Only hide if dismissible, this is done by
            // providing a background drawable to popup.
            if (getPopupWindow() != null &&
                getPopupWindow().getBackground() != null) {

                hide(null);
            }
        });

        // State initially set to hidden.
        mDialogContentState = DialogContentState.HIDDEN;
    }

    /**
     * Fetch instance of popup window.
     *
     * @return Popup window or null if it does not exist.
     */
    private PopupWindow getPopupWindow() {

        if (mPopupWindows.containsKey(mPopupWindowKey)) {

            return mPopupWindows.get(mPopupWindowKey);
        }

        return null;
    }

    // endregion

    // region Interfaces
    // ============================================================================================================

    /**
     * Listener for hide event.  Sometimes we cannot hide
     * this dialog immediately because of pending animations.
     */
    public interface HideListener {

        // Hide executed.
        void didHide();
    }

    // endregion
}