package com.blitz.app.utilities.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blitz.app.R;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import butterknife.ButterKnife;

/**
 * Created by Miguel Gaeta on 6/29/14.
 *
 * TODO: Split animation related code into BaseDialogAnimations
 */
public class BaseDialog {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // State of dialog content.
    private enum DialogContentState {
        HIDDEN, ANIMATING, VISIBLE
    }

    // Constants.
    private static final long ANIMATION_TIME = 250;

    // All dialogs should have a content view.
    private ViewGroup mDialogContent;
    private DialogContentState mDialogContentState;

    // Dialog is represented by a popup window.
    private PopupWindow mPopupWindow;

    // Parent activity.
    private Activity mActivity;

    // Hide related variables.
    private boolean mHidePending;
    private HideListener mHideListener;

    //==============================================================================================
    // Constructors
    //==============================================================================================

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
        mPopupWindow = new PopupWindow(layoutOfPopup,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        // When the popup is dismissed, reset any butter-knife injections.
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                // Probably not needed, but good practice.
                ButterKnife.reset(this);
            }
        });

        // Inject butter knife into the content view.
        ButterKnife.inject(this, mPopupWindow.getContentView());

        setupDialogContent();
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Set a dismiss listener.
     *
     * @param onDismissListener Dismiss listener.
     */
    @SuppressWarnings("unused")
    public void setOnDismissListener(OnDismissListener onDismissListener) {

        // Assign the dismiss listener.
        mPopupWindow.setOnDismissListener(onDismissListener);
    }

    /**
     * Show the popup.
     *
     * @param showContent Should also display it's content?
     * @param delay Time to delay showing.
     */
    @SuppressWarnings("unused")
    public void show(final boolean showContent, int delay) {

        // If window exists and not already showing.
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {

            // Hide the keyboard.
            KeyboardUtility.hideKeyboard(mActivity);

            // Run on specified delay.
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    // Show at top corner of the window.
                    mPopupWindow.showAtLocation(mActivity.getWindow().getDecorView(),
                            Gravity.NO_GRAVITY, 0, 0);

                    // Try to show dialog content.
                    tryShowDialogContent(showContent);
                }
            }, delay);

        } else {

            // Try to show dialog content.
            tryShowDialogContent(showContent);
        }
    }

    /**
     * Show the popup.
     *
     * @param showContent Should also display it's content?
     */
    public void show(boolean showContent) {
        show(showContent, 0);
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

                // Hide immediately.
                mPopupWindow.dismiss();

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

    //==============================================================================================
    // Private Methods
    //==============================================================================================

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

        // Initialize the alpha.
        mDialogContent.setAlpha(visible ? 0f : 1f);

        // Define animation end callback.
        AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

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
            }
        };

        // Perform the animation.
        mDialogContent.animate()
                .alpha(visible ? 1f : 0f)
                .setDuration(ANIMATION_TIME)
                .setListener(adapter);
    }

    /**
     * Setup the dialog content view (find it),
     * hide it, and set click listeners, etc.
     */
    private void setupDialogContent() {

        // Fetch the content window.  This is required of all dialogs.
        mDialogContent = ButterKnife.findById(mPopupWindow.getContentView(), R.id.dialog_content);

        if (mDialogContent == null) {

            // Throw exception if content view not found.
            throw new RuntimeException("Dialog must have a content view.");
        }

        mDialogContent.setVisibility(View.GONE);
        mDialogContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Only hide if dismissible, this is done by
                // providing a background drawable to popup.
                if (mPopupWindow.getBackground() != null) {

                    hide(null);
                }
            }
        });

        // State initially set to hidden.
        mDialogContentState = DialogContentState.HIDDEN;
    }

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * Set whether this dialog can be touched.  Only useable
     * by direct dialog subclasses.
     *
     * @param touchable Is dialog touchable.
     */
    @SuppressWarnings("unused")
    protected void setTouchable(boolean touchable) {

        mPopupWindow.setOutsideTouchable(touchable);
        mPopupWindow.setTouchable(touchable);
        mPopupWindow.setFocusable(touchable);
    }

    /**
     * Popup is only dismissible if you provide
     * a background drawable. http://bit.ly/1oaUSSy
     *
     * @param dismissible Boolean toggle.
     */
    @SuppressWarnings("unused")
    protected void setDismissible(boolean dismissible) {

        // Provide a drawable if dismissible.
        mPopupWindow.setBackgroundDrawable
                (dismissible ? new ColorDrawable(Color.TRANSPARENT) : null);
    }

    //==============================================================================================
    // Interfaces / Inner Classes
    //==============================================================================================

    /**
     * Custom on dismiss listener.
     */
    public abstract class OnDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {

            // Probably not needed, but good practice.
            ButterKnife.reset(this);

            onDismiss(mActivity);
        }

        public abstract void onDismiss(Activity activity);
    }

    /**
     * Listener for hide event.  Sometimes we cannot hide
     * this dialog immediately because of pending animations.
     */
    public interface HideListener {

        // Hide executed.
        void didHide();
    }
}