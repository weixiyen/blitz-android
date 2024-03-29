package com.blitz.app.utilities.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.dialogs.error.DialogErrorSingleton;
import com.blitz.app.utilities.animations.AnimHelper;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.background.EnteredBackground;
import com.blitz.app.utilities.blitz.BlitzDelay;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.reflection.ReflectionHelper;
import com.blitz.app.utilities.string.StringHelper;
import com.blitz.app.view_models.ViewModel;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Shared base functionality across all activities.
 */
public class BaseActivity extends FragmentActivity {

    // region Member Variables
    // ============================================================================================================

    // Custom transition type.
    public enum CustomTransition {
        T_STANDARD,
        T_SLIDE_HORIZONTAL,
        T_SLIDE_VERTICAL
    }

    // Custom transitions flag.
    private CustomTransition mCustomTransitions = CustomTransition.T_STANDARD;

    // Are we going back an activity.
    private static boolean mGoingBack = false;

    // By default the keyboard should adjust
    // and resize activity window.
    private boolean mAdjustResize;

    // Use to delay initialization of our view models which
    // alleviates perceived lag during transition animations.
    private Handler mViewModelInitializeHandler;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Run custom transitions if needed.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AppConfig.isLandscapeEnabled()) {

            // Portrait mode only.
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // Fetch the class name string in the format of resource view.
        String underscoredClassName = StringHelper.camelCaseToLowerCaseUnderscores(getClass().getSimpleName());

        // Use reflection to fetch the associated view resource id and set content view.
        setContentView(ReflectionHelper.getResourceId(underscoredClassName, R.layout.class));

        // Restore view model state.
        if (getViewModel() != null) {
            getViewModel().restoreInstanceState(savedInstanceState);
        }
    }

    /**
     * Update the keyboard utility listener.
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Update the layout listener.
        KeyboardUtility.setGlobalLayoutListener(this);
    }

    /**
     * Runs when activity has been
     * presented to the user.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Initialize the view model on the screen transition delay.
        mViewModelInitializeHandler = BlitzDelay.postDelayed(() -> {

            // Initialize view model.
            if (getViewModel() != null) {
                getViewModel().initialize();
            }

        }, AnimHelper.getConfigAnimTimeStandard());

        if (mGoingBack) {
            mGoingBack = false;

        } else if (!EnteredBackground.isInBackground()) {

            // Run transitions, we are entering.
            runCustomTransitions(true);
        }

        // Stop timer to detect entering the background.
        EnteredBackground.stopActivityTransitionTimer();

        // Add current activity.
        DialogErrorSingleton.configAddActivity(this);

        // Add current activity.
        CometAPIManager.configAddActivity(this);
    }

    /**
     * Run custom transitions if needed, also
     * start timer to detect entering background.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Remove pending initialize calls.
        BlitzDelay.remove(mViewModelInitializeHandler);

        // Stop view model.
        if (getViewModel() != null) {
            getViewModel().stop();
        }

        if (mGoingBack) {

            // Run transitions, we going back.
            runCustomTransitions(false);
        }

        // Start timer to detect entering the background.
        EnteredBackground.startActivityTransitionTimer();

        // Remove any popups that may exist.
        DialogErrorSingleton.configRemoveActivity();

        // TODO: Deprecate.
        BaseDialog.dismissAllPopups();

        // Clear our current activity.
        CometAPIManager.configRemoveActivity(this);
    }

    /**
     * Inject butter-knife views when the content
     * view is set by the user.
     *
     * @param layoutResID Layout xml file.
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // Inject butter-knife.
        ButterKnife.inject(this);
    }

    /**
     * Intercept base context and pass it through
     * calligraphy library for custom fonts.
     *
     * @param baseContext Base context.
     */
    @Override
    protected void attachBaseContext(Context baseContext) {
        super.attachBaseContext(new CalligraphyContextWrapper(baseContext));
    }

    /**
     * Track if we are going back
     * with a static flag.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Now going back.
        mGoingBack = true;
    }

    /**
     * Save the instance state of the model.
     *
     * @param outState State values.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save view model state.
        if (getViewModel() != null) {
            getViewModel().saveInstanceState(outState);
        }
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Convenience method for starting an activity
     * and also clearing the history.
     *
     * @param intent Intent.
     * @param clearHistory Clear history flag.
     */
    public void startActivity(Intent intent, boolean clearHistory) {

        // Call native method.
        startActivity(intent);

        if (clearHistory) {

            // If jelly bean or above.
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                // Finish all activities on
                // the current stack.
                finishAffinity();
            }
        }
    }

    /**
     * Expose an overloaded set content view that also allows
     * for toggling of custom transitions.
     *
     * @param layoutResID Layout resource id.
     * @param customTransition Custom transition.
     */
    @SuppressWarnings("unused")
    public void setContentView(int layoutResID, CustomTransition customTransition) {

        // First set the custom transitions.
        mCustomTransitions = customTransition;

        setContentView(layoutResID);
    }

    /**
     * Set custom transitions on or off.
     *
     * @param customTransition Custom transition.
     */
    public void setCustomTransitions(CustomTransition customTransition) {

        mCustomTransitions = customTransition;
    }

    /**
     * Set if activity should adjust and resize
     * window when keyboard is open.
     *
     * @param adjustResize Should adjust resize.
     */
    @SuppressWarnings("unused")
    public void setAdjustResize(boolean adjustResize) {

        mAdjustResize = adjustResize;
    }

    /**
     * Get if activity should adjust and resize
     * window when keyboard is opened.
     *
     * @return Flag.
     */
    @SuppressWarnings("unused")
    public boolean getAdjustResize() {

        return mAdjustResize;
    }

    /**
     * Get root view of activity.
     */
    @SuppressWarnings("unused")
    public View getView() {

        return getWindow().getDecorView().getRootView();
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Run custom transitions between switching activities.
     *
     * @param entering Is activity entering or existing.
     */
    private void runCustomTransitions(boolean entering) {
        if (mCustomTransitions == null) {
            return;
        }

        switch (mCustomTransitions) {
            case T_STANDARD:

                overridePendingTransition(
                        entering ? R.anim.activity_standard_open_in  : R.anim.activity_standard_close_in,
                        entering ? R.anim.activity_standard_open_out : R.anim.activity_standard_close_out);
                break;
            case T_SLIDE_HORIZONTAL:

                overridePendingTransition(
                        entering ? R.anim.activity_slide_open_in  : R.anim.activity_slide_close_in,
                        entering ? R.anim.activity_slide_open_out : R.anim.activity_slide_close_out);
                break;
            case T_SLIDE_VERTICAL:
                overridePendingTransition(
                        entering ? R.anim.activity_slide_up_open_in  : R.anim.activity_slide_up_close_in,
                        entering ? R.anim.activity_slide_up_open_out : R.anim.activity_slide_up_close_out);
                break;
        }
    }

    /**
     * Attempts to fetch the associated instance
     * of the view model for lifecycle callbacks.
     *
     * @return View model or null if not found.
     */
    private ViewModel getViewModel() {

        // If we implement view model callbacks.
        if (this instanceof ViewModel.Callbacks) {

            // That means we can fetch the view model.
            return ((ViewModel.Callbacks)this).onFetchViewModel();
        }

        return null;
    }

    // endregion
}