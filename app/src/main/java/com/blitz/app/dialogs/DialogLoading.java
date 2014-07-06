package com.blitz.app.dialogs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.blitz.app.R;
import com.blitz.app.base.dialog.BaseDialog;

import butterknife.InjectView;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class DialogLoading extends BaseDialog {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.dialog_loading_content) RelativeLayout mLoadingView;

    private static final long ANIMATION_TIME = 250;
    private static final long MINIMUM_LOADING_TIME = 1000;

    private enum LoadingContentState {
        HIDDEN, ANIMATING, VISIBLE
    }

    // Callback objects.
    private Runnable mRunnable;
    private Handler mHandler;

    // State of the loading view.
    private LoadingContentState mLoadingViewState;

    // Signals hide event pending.
    private boolean mHidePending;

    // Hide event callback.
    private HideListener mHideListener;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Setup state when constructed.
     *
     * @param activity Target activity.
     */
    public DialogLoading(Activity activity) {
        super(activity);

        setTouchable(true);

        // State initially set to hidden.
        mLoadingViewState = LoadingContentState.HIDDEN;

        // View initially set to gone.
        mLoadingView.setVisibility(View.GONE);
    }

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * When this dialog is shown, at first it just blocks
     * the UI.  After a period of time, it then shows a
     * loading view UI.
     */
    @Override
    public void show() {
        super.show();

        mRunnable = new Runnable() {
            @Override
            public void run() {

                toggleLoadingUI(true);
            }
        };

        mHandler = new Handler();

        // Toggle loading view if displayed for long enough.
        mHandler.postDelayed(mRunnable, MINIMUM_LOADING_TIME);
    }

    /**
     * Perform some additional hide
     * functionality for this dialog.
     */
    @Override
    public void hide() {
        super.hide();

        // Remove any pending callbacks.
        mHandler.removeCallbacks(mRunnable);

        if (mHideListener != null) {
            mHideListener.didHide();
        }
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Additional hide method that provides a
     * callback function.
     *
     * @param hideListener Callback function.
     */
    public void hide(HideListener hideListener) {
        mHideListener = hideListener;

        switch (mLoadingViewState) {
            case HIDDEN:

                // Hide immediately.
                hide();

                break;
            case ANIMATING:

                // Set pending hide.
                mHidePending = true;

                break;
            case VISIBLE:

                // Set pending hide.
                mHidePending = true;

                // Hide loading view.
                toggleLoadingUI(false);

                break;
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Toggle the loading dialog UI. In the process of hiding it,
     * can also potentially perform a hide operation if pending.
     *
     * @param visible Show or hide.
     */
    private void toggleLoadingUI(final boolean visible) {

        // Set the state to be animating.
        mLoadingViewState = LoadingContentState.ANIMATING;

        // Make sure the loading view is visible.
        mLoadingView.setVisibility(View.VISIBLE);

        // Initialize the alpha.
        mLoadingView.setAlpha(visible ? 0f : 1f);

        // Define animation end callback.
        AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                // Update the state flag.
                mLoadingViewState = visible ?
                        LoadingContentState.VISIBLE :
                        LoadingContentState.HIDDEN;

                // If hide pending.
                if (mHidePending) {

                    if (visible) {

                        // Hide it, and leave pending flag on.
                        toggleLoadingUI(false);

                    } else {

                        hide();
                    }
                }
            }
        };

        // Perform the animation.
        mLoadingView.animate()
                .alpha(visible ? 1f : 0f)
                .setDuration(ANIMATION_TIME)
                .setListener(adapter);
    }

    //==============================================================================================
    // Interfaces
    //==============================================================================================

    /**
     * Listener for hide event.  Sometimes we cannot hide
     * this dialog immediately because of pending animations.
     */
    public interface HideListener {

        // Hide executed.
        void didHide();
    }
}