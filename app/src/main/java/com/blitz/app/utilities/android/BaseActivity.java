package com.blitz.app.utilities.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.blitz.app.R;
import com.blitz.app.models.comet.CometAPIManager;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.background.EnteredBackground;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.reflection.ReflectionHelper;
import com.blitz.app.utilities.string.StringHelper;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Shared base functionality across all activities.
 */
public class BaseActivity extends FragmentActivity implements ViewModel.ViewModelCallbacks {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Custom transition type.
    public enum CustomTransition {
        T_STANDARD,
        T_SLIDE_HORIZONTAL,
        T_SLIDE_VERTICAL
    }

    // Custom transitions flag.
    private CustomTransition mCustomTransitions = CustomTransition.T_STANDARD;

    // Track if activity history is cleared.
    private static boolean mHistoryClearedFlag;
    private static boolean mHistoryCleared;

    // View model for each activity.
    private ViewModel mViewModel = null;

    // Are we going back an activity.
    private static boolean mGoingBack = false;

    // By default the keyboard should adjust
    // and resize activity window.
    private boolean mAdjustResize;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Run custom transitions if needed.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppConfig.PORTRAIT_ONLY) {

            // Portrait mode only.
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // Fetch the class name string in the format of resource view.
        String underscoredClassName = StringHelper.camelCaseToLowerCaseUnderscores(getClass().getSimpleName());

        // Use reflection to fetch the associated view resource id and set content view.
        setContentView(ReflectionHelper.getResourceId(underscoredClassName, R.layout.class));
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

        if (mGoingBack) {
            mGoingBack = false;

        } else if (!EnteredBackground.isInBackground()) {

            // Run transitions, we are entering.
            runCustomTransitions(true);
        }

        // Stop timer to detect entering the background.
        EnteredBackground.stopActivityTransitionTimer();

        // Add current activity.
        CometAPIManager.configAddActivity(this);

        // Initialize the view model.
        if (mViewModel != null) {
            mViewModel.initialize(this, this);
        }
    }

    /**
     * Save this screens state.
     *
     * @param outState State values.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save state.
        if (mViewModel != null) {
            mViewModel.saveInstanceState(outState);
        }
    }

    /**
     * Run custom transitions if needed, also
     * start timer to detect entering background.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (mGoingBack) {

            if (mHistoryCleared) {
                mHistoryCleared = false;
            } else {

                // Run transitions, we going back.
                runCustomTransitions(false);
            }
        }

        // Start timer to detect entering the background.
        EnteredBackground.startActivityTransitionTimer();

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
     * Intercept start activity in order to make sure that
     * if the history stack is cleared, we still run
     * animations in the proper direction.
     *
     * @param intent Parameters.
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);

        // If clear history flag was set, but we now are
        // starting a new activity, then we can unset the
        // flag as a new history stack is being built.
        if (mHistoryCleared) {
            mHistoryCleared = false;
        }

        // If however the clear history flag has been set
        // we do want the history to remain cleared.
        if (mHistoryClearedFlag) {
            mHistoryClearedFlag = false;

            mHistoryCleared = true;
        }
    }

    /**
     * Track if we are going back
     * with a static flag.
     */
    @Override
    public void onBackPressed() {

        if (mHistoryCleared) {

            // Move this activity to the back of
            // the history stack which causes the
            // back button to essentially exit
            // the app, which is the behavior a
            // user expects on clear history.
            moveTaskToBack(true);
        }

        // Now going back.
        mGoingBack = true;

        super.onBackPressed();
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Convenience method for starting an activity
     * and also clearing the history.
     *
     * @param intent Intent.
     * @param clearHistory Clear history flag.
     */
    public void startActivity(Intent intent, boolean clearHistory) {

        // Set clear history flag.
        mHistoryClearedFlag = clearHistory;

        // Call native method.
        startActivity(intent);
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
     * Fetch the view model, assumes user
     * knows the type.
     *
     * @param type View model type.
     * @param <T> Type.
     *
     * @return Casted view model.
     */
    public <T extends ViewModel> T getViewModel(Class<T> type) {

        return type.cast(mViewModel);
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

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * Set the view model (initializes it).
     *
     * @param viewModel View model instance.
     * @param savedInstanceState Saved state.
     */
    protected void setViewModel(ViewModel viewModel, Bundle savedInstanceState) {

        // Set the model.
        mViewModel = viewModel;

        // Restore state.
        if (mViewModel != null) {
            mViewModel.restoreInstanceState(savedInstanceState);
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

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
}