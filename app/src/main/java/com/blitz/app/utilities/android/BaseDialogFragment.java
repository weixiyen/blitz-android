package com.blitz.app.utilities.android;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.blitz.app.R;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by mrkcsc on 8/17/14. Copyright 2014 Blitz Studios
 */
public abstract class BaseDialogFragment extends DialogFragment {

    // region Member Variables
    // ============================================================================================================

    // Initialize list of dismiss actions.
    private List<Runnable> mOnDismissActions = new ArrayList<>();

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Set a transparent theme.
     *
     * @param savedInstanceState Saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the style to force a transparent dialog.
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent);
    }

    /**
     * When view is created inflate layout and setup the window.
     *
     * @param inflater Inflater object.
     * @param container Parent container.
     * @param savedInstanceState Saved state.
     *
     * @return Instantiated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Configure fragment.
        configureDialogFragment();

        // Configure and return the view.
        return configureDialogView(inflater, container);
    }

    /**
     * Setup keyboard events.
     */
    @Override
    public void onResume() {
        super.onResume();

        // Respond to keyboard changes.
        configureKeyboardChanges(getView(), false);
    }

    /**
     * Remove keyboard events.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Remove keyboard changes.
        configureKeyboardChanges(getView(), true);
    }

    /**
     * Destroy butter-knife injections.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Reset butter-knife.
        ButterKnife.reset(this);
    }

    /**
     * Perform custom action on dismiss.
     */
    @Override
    public void dismiss() {
        super.dismiss();

        for (Runnable onDismissAction : new ArrayList<>(mOnDismissActions)) {

            // Run custom dismiss action.
            if (onDismissAction != null) {
                onDismissAction.run();
            }
        }
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Convenience method for show that does not need a tag.
     *
     * @param fragmentManager Fragment manager.
     */
    @SuppressWarnings("unused")
    public void show(FragmentManager fragmentManager) {

        // Call using unique hash code based on class instance.
        show(fragmentManager, Integer.toString(System.identityHashCode(this)));
    }

    /**
     * Set a dismiss action.
     *
     * @param onDismissAction Dismiss action.
     */
    @SuppressWarnings("unused")
    public void addOnDismissAction(Runnable onDismissAction) {

        // Add on dismiss action.
        mOnDismissActions.add(onDismissAction);
    }

    /**
     * Remove a selected dismiss action.
     *
     * @param onDismissAction Dismiss action.
     */
    @SuppressWarnings("unused")
    public void removeOnDismissAction(Runnable onDismissAction) {

        // Remove associated action.
        mOnDismissActions.remove(onDismissAction);
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Configure the native dialog fragment to
     * be full screen with no background.
     */
    private void configureDialogFragment() {

        // Fetch dialog window.
        Window window = getDialog().getWindow();

        // Make the background transparent.
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Make the dialog full screen.
        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // No title mode (or status bar).
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the animations.
        window.getAttributes().windowAnimations = R.style.App_DialogAnimation;
    }

    /**
     * Inflate, and configure the view.
     *
     * @param inflater Inflater object.
     * @param container Parent container.
     *
     * @return Configured view.
     */
    private View configureDialogView(LayoutInflater inflater, ViewGroup container) {

        // Use reflection to fetch the associated view resource id and set content view.
        int layoutResourceId = ReflectionHelper.getResourceId(this.getClass(), R.layout.class);

        // Now inflate the associated view.
        View view = inflater.inflate(layoutResourceId, container);

        // Initialize butter-knife.
        ButterKnife.inject(this, view);

        // Call on view created.
        onViewCreated(view);

        return view;
    }

    /**
     * Configure to respond to keyboard changes.
     *
     * @param view Dialog root view.
     */
    private void configureKeyboardChanges(final View view, boolean remove) {

        if (remove) {

            // Detach the change listener.
            KeyboardUtility.setKeyboardChangedListener(null);

        } else {

            // Fetch layout parameters of the dialog view.
            final ViewGroup.LayoutParams lp = view.getLayoutParams();

            // Track keyboard changes.
            KeyboardUtility.setKeyboardChangedListener(new KeyboardUtility.OnKeyboardChangedListener() {

                @Override
                public void keyboardOpened(int keyboardHeight) {

                    // Set the height to not include keyboard height.
                    lp.height = KeyboardUtility.getWindowHeight() - KeyboardUtility.getKeyboardHeight();

                    // Re-render layout.
                    view.requestLayout();
                }

                @Override
                public void keyboardClosed() {

                    // Set full window height.
                    lp.height = KeyboardUtility.getWindowHeight();

                    // Re-render layout.
                    view.requestLayout();
                }
            });
        }
    }

    // endregion

    // region Abstract Methods
    // ============================================================================================================

    protected abstract void onViewCreated(View view);

    // endregion
}