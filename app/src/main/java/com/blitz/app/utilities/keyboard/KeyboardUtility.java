package com.blitz.app.utilities.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.blitz.BlitzDelay;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Keyboard related functionality.
 */
public class KeyboardUtility {

    // region Member Variables
    // =============================================================================================

    // Minimum height of the keyboard.
    private static final int MINIMUM_KEYBOARD_HEIGHT = 300;

    // Current root activity view.
    private static View mRootView;
    private static FrameLayout.LayoutParams mRootViewLayoutParams;

    // Used to track keyboard state internally.
    private static int mKeyboardHeight;
    private static int mKeyboardHeightCached;
    private static int mWindowHeight;

    // Are we running in fullscreen/
    private static boolean mIsFullscreen;

    // Is the keyboard open.
    private static boolean mKeyboardOpen;

    // Handler and runnable to control keyboard
    // open event (posted on a delay).
    private static Handler mKeyboardHandler;

    // Listener to watch for layout changes in the root view.
    private static ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;

    // Listener set from outside to run callbacks on keyboard events.
    private static OnKeyboardChangedListener mOnKeyboardChangedListener;

    // Callbacks for when the keyboard shows and hides.
    private static ArrayList<Runnable> mOnKeyboardShowCallbacks = new ArrayList<Runnable>();
    private static ArrayList<Runnable> mOnKeyboardHideCallbacks = new ArrayList<Runnable>();

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Initialize the keyboard utility.
     *
     * @param context Application context.
     */
    @SuppressWarnings("unused")
    static public void init(Context context) {

        mKeyboardHeight = detectKeyboardHeight(context);
        mKeyboardHeightCached = detectKeyboardHeight(context);
    }

    /**
     * Set keyboard changed listener to expose keyboard event's to
     * the callee.  Currently only supports a single listener.
     */
    @SuppressWarnings("unused")
    static public void setKeyboardChangedListener(OnKeyboardChangedListener onKeyboardChangedListener) {
        mOnKeyboardChangedListener = onKeyboardChangedListener;
    }

    /**
     * Should only be called by the class that
     * control the activity life cycles.  This class will
     * given the current activity, track layout changes
     * to infer keyboard state and height.
     *
     * @param activity If null, removes tracking.
     */
    @SuppressWarnings("unused, deprecation")
    static public void setGlobalLayoutListener(final BaseActivity activity) {

        if (mRootView != null) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            } else {
                mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            }
        }

        // Every time we make a new global listener,
        // we want to re-initialize keyboard to closed.
        mKeyboardOpen = false;

        // Figure out if running in fullscreen.
        mIsFullscreen = (activity.getWindow().getAttributes().flags
                & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;

        // Define the layout listener for window height changes.
        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            private int mPreviousKeyboardHeight;
            private int mCurrentKeyboardHeight;

            public void onGlobalLayout() {

                // Swap around previous and current heights,
                // every time we detect a change in the layout.
                mPreviousKeyboardHeight = mCurrentKeyboardHeight;
                mCurrentKeyboardHeight = getCurrentKeyboardHeight();

                // If the height has changed in some way.
                if (mPreviousKeyboardHeight != mCurrentKeyboardHeight) {

                    BlitzDelay.remove(mKeyboardHandler);

                    if (mCurrentKeyboardHeight == 0 && mKeyboardOpen) {
                        mKeyboardOpen = false;

                        // Execute on hide listener.
                        if (mOnKeyboardChangedListener != null) {
                            mOnKeyboardChangedListener.keyboardClosed();
                        }

                        // Run hide callbacks.
                        runCallbacks(mOnKeyboardHideCallbacks);

                    } else if (mCurrentKeyboardHeight > MINIMUM_KEYBOARD_HEIGHT) {
                        mKeyboardHeight = mCurrentKeyboardHeight;

                        // Run show on a callback.
                        mKeyboardHandler = BlitzDelay.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                // If actual height differs from the cached height.
                                if (mKeyboardHeight != mKeyboardHeightCached) {
                                    setKeyboardHeight(activity);
                                }

                                if (!mKeyboardOpen) {
                                     mKeyboardOpen = true;

                                    // Execute on show listener.
                                    if (mOnKeyboardChangedListener != null) {
                                        mOnKeyboardChangedListener.keyboardOpened(mKeyboardHeight);
                                    }

                                    // Run show callbacks.
                                    runCallbacks(mOnKeyboardShowCallbacks);
                                }
                            }
                        }, 300);
                    }

                    if (mIsFullscreen && activity.getAdjustResize()) {

                        // Update and resize the view if in fullscreen mode.
                        mRootViewLayoutParams.height = mWindowHeight - mCurrentKeyboardHeight;
                        mRootView.requestLayout();
                    }
                }
            }
        };

        // Update the root view and set a global layout listener.
        mRootView = ((FrameLayout)activity.findViewById(android.R.id.content)).getChildAt(0);
        mRootViewLayoutParams = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    /**
     * Get keyboard height.
     */
    @SuppressWarnings("unused")
    static public int getKeyboardHeight() {
        return mKeyboardHeight;
    }

    /**
     * Check if keyboard is open.
     */
    @SuppressWarnings("unused")
    static public boolean keyboardOpen() {
        return mKeyboardOpen;
    }

    // endregion

    // region Show Keyboard
    // =============================================================================================

    @SuppressWarnings("unused")
    static public void showKeyboard(Activity activity) {
        showKeyboard(activity, null);
    }

    @SuppressWarnings("unused")
    static public void showKeyboard(Activity activity, View targetFocus) {
        showKeyboard(activity, targetFocus, null);
    }

    /**
     * Show the keyboard.  Optionally provide a target
     * view for focus and an on hide callback.
     */
    @SuppressWarnings("unused")
    static public void showKeyboard(final Activity activity, View targetFocus, Runnable onShowCallback) {
        if (targetFocus != null) {
            targetFocus.requestFocus();
        }

        final InputMethodManager imm = (InputMethodManager)activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {

            // Small delay to make sure keyboard opens.
            BlitzDelay.postDelayed(new Runnable() {

                @Override
                public void run() {
                    imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
        }

        mOnKeyboardShowCallbacks.add(onShowCallback);
    }

    // endregion

    // region Hide Keyboard
    // =============================================================================================

    @SuppressWarnings("unused")
    static public void hideKeyboard(Activity activity) {
        hideKeyboard(activity, null);
    }

    @SuppressWarnings("unused")
    static public void hideKeyboard(Activity activity, View targetFocus) {
        hideKeyboard(activity, targetFocus, null);
    }

    /**
     * Hide the keyboard.  Optionally provide a target
     * view for focus and an on hide callback.
     */
    @SuppressWarnings("unused")
    static public void hideKeyboard(final Activity activity, View targetFocus, Runnable onHideCallback) {
        if (mKeyboardOpen) {

            // If target focus is provided,
            // then just clear it out.
            if (targetFocus != null) {
                targetFocus.clearFocus();
            }

            if (activity != null) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

                if (activity.getCurrentFocus() != null) {

                    inputMethodManager.hideSoftInputFromWindow
                            (activity.getCurrentFocus().getWindowToken(), 0);
                }
            }

            mOnKeyboardHideCallbacks.add(onHideCallback);

            // If keyboard is not open, then
            // run the callback right away.
        } else if (onHideCallback != null) {
            onHideCallback.run();
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Run callbacks and clear list.
     */
    private static void runCallbacks(ArrayList<Runnable> callbacks) {
        for (Runnable callback : callbacks) {
            if (callback != null) {
                callback.run();
            }
        }

        callbacks.clear();
    }

    /**
     * Convert DP to pixels.
     *
     * @param context Context object.
     * @param densityPixels Target DP.
     *
     * @return Converted pixel value.
     */
    private static int densityPixelsToPixels(Context context, int densityPixels) {

        // Grab the resources object.
        Resources resources = context.getResources();

        // Return pixel value.
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                densityPixels, resources.getDisplayMetrics()));
    }

    /**
     * Get current keyboard height by guessing it
     * using the current root view and window height.
     */
    private static int getCurrentKeyboardHeight() {

        // Rect holder.
        Rect rect = new Rect();

        // Fetch current height.
        mRootView.getWindowVisibleDisplayFrame(rect);

        // Compute the height from rect holder.
        int rootViewHeight = (rect.bottom - rect.top);

        // Track the window height.
        if (mWindowHeight < rootViewHeight) {
            mWindowHeight = rootViewHeight;
        }

        // Fetch current height of the keyboard.
        return mWindowHeight - rootViewHeight;
    }

    /**
     * Attempt to detect keyboard height
     * from the user defaults.
     */
    private static int detectKeyboardHeight(Context context) {

        // Fetch soft keyboard identifier using a cool trick: http://bit.ly/18w0yTJ
        String softKeyboardIdentifier = Settings.Secure.getString
                (context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);

        // Fetch cached keyboard heights.
        HashMap<String, String> keyboardHeights =
                AppDataObject.keyboardHeights.get();

        // If cached height exists, return it.
        if (keyboardHeights.get(softKeyboardIdentifier) != null) {

            return Integer.parseInt(keyboardHeights.get(softKeyboardIdentifier));
        }

        // Otherwise return default height.
        return densityPixelsToPixels(context, 350);
    }

    /**
     * Attempt to set set keyboard
     * height for given soft keyboard into
     * the user defaults.
     */
    private static void setKeyboardHeight(Context context) {

        // Fetch soft keyboard identifier using a cool trick: http://bit.ly/18w0yTJ
        String softKeyboardIdentifier =  Settings.Secure.getString
                (context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);

        // Fetch cached keyboard heights.
        HashMap<String, String> keyboardHeights =
                AppDataObject.keyboardHeights.get();

        // Place new height it into dictionary.
        keyboardHeights.put(softKeyboardIdentifier, Integer.toString(mKeyboardHeight));

        // Persist the updated dictionary.
        AppDataObject.keyboardHeights.set(keyboardHeights);

        // Update the cached keyboard height.
        mKeyboardHeightCached = mKeyboardHeight;
    }

    // endregion

    // region Interface
    // =============================================================================================

    /**
     * Expose an interface for keyboard events.
     */
    public interface OnKeyboardChangedListener {

        public abstract void keyboardOpened(int keyboardHeight);
        public abstract void keyboardClosed();
    }

    // endregion
}