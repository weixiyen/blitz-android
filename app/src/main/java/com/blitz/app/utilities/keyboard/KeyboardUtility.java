package com.blitz.app.utilities.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.blitz.app.utilities.app.AppDataObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Keyboard related functionality.
 */
public class KeyboardUtility {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Minimum height of the keyboard.
    private static final int MINIMUM_KEYBOARD_HEIGHT = 300;

    // Current root activity view.
    private static View mRootView;

    // Used to track keyboard state internally.
    private static int mKeyboardHeight;
    private static int mKeyboardHeightCached;
    private static int mWindowHeight;

    // Is the keyboard open.
    private static boolean mKeyboardOpen;

    // Handler and runnable to control keyboard
    // open event (posted on a delay).
    private static Handler mKeyboardHandler;
    private static Runnable mKeyboardRunnable;

    // Listener to watch for layout changes in the root view.
    private static ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;

    // Listener set from outside to run callbacks on keyboard events.
    private static OnKeyboardChangedListener mOnKeyboardChangedListener;

    // Callbacks for when the keyboard shows and hides.
    private static ArrayList<Runnable> mOnKeyboardShowCallbacks = new ArrayList<Runnable>();
    private static ArrayList<Runnable> mOnKeyboardHideCallbacks = new ArrayList<Runnable>();

    //==============================================================================================
    // Public Methods
    //==============================================================================================

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
    static public void setGlobalLayoutListener(final Activity activity) {

        if (mRootView != null) {
            mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }

        // Every time we make a new global listener,
        // we want to re-initialize keyboard to closed.
        mKeyboardOpen = false;

        // Define the layout listener for window height changes.
        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            private int mPreviousKeyboardHeight;
            private int mCurrentKeyboardHeight;

            public void onGlobalLayout() {

                // Swap around previous and current heights,
                // every time we detect a change in the layout.
                mPreviousKeyboardHeight = mCurrentKeyboardHeight;
                mCurrentKeyboardHeight = getCurrentKeyboardHeight();

                if (mKeyboardRunnable == null) {
                    mKeyboardRunnable = new Runnable() {
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
                    };
                }

                if (mKeyboardHandler == null) {
                    mKeyboardHandler = new Handler();
                }

                // If the height has changed in some way.
                if (mPreviousKeyboardHeight != mCurrentKeyboardHeight) {

                    mKeyboardHandler.removeCallbacks(mKeyboardRunnable);

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
                        mKeyboardHandler.postDelayed(mKeyboardRunnable, 300);
                    }
                }
            }
        };

        // Update the root view and set a global layout listener.
        mRootView = activity.findViewById(android.R.id.content);
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

    //==============================================================================================
    // Show Keyboard
    //==============================================================================================

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
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
        }

        mOnKeyboardShowCallbacks.add(onShowCallback);
    }

    //==============================================================================================
    // Hide Keyboard
    //==============================================================================================

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

    //==============================================================================================
    // Private Methods
    //==============================================================================================

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
        int rootViewHeight = mRootView.getHeight();

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
                AppDataObject.keyboardHeights.getDictionary();

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
                AppDataObject.keyboardHeights.getDictionary();

        // Place new height it into dictionary.
        keyboardHeights.put(softKeyboardIdentifier, Integer.toString(mKeyboardHeight));

        // Persist the updated dictionary.
        AppDataObject.keyboardHeights.set(keyboardHeights);

        // Update the cached keyboard height.
        mKeyboardHeightCached = mKeyboardHeight;
    }

    //==============================================================================================
    // Interfaces
    //==============================================================================================

    /**
     * Expose an interface for keyboard events.
     */
    public interface OnKeyboardChangedListener {
        public abstract void keyboardOpened(int keyboardHeight);
        public abstract void keyboardClosed();
    }
}