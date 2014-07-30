package com.blitz.app.utilities.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.blitz.app.R;
import com.blitz.app.models.comet.CometAPIManager;
import com.blitz.app.models.views.ViewModel;
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
    private boolean mHistoryCleared = false;

    // View model for each activity.
    private ViewModel mViewModel = null;

    // Are we going back an activity.
    private static boolean mGoingBack = false;

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
            runCustomTransitions(getIntent(), true);
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

            // Run transitions, we are exiting.
            runCustomTransitions(null, false);
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
     * @param options Options.
     */
    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);

        // If history is cleared.
        if (isHistoryCleared(intent)) {

            // Flip the flag.
            mHistoryCleared = true;
        }
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

        if (clearHistory) {

            // Set flags to clear the history.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        super.startActivity(intent);
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
     * Given an optional intent, find out if the
     * history has been cleared - either via intent
     * or the cleared flag.
     *
     * @param intent Intent.
     *
     * @return True if cleared.
     */
    private boolean isHistoryCleared(Intent intent) {

        return intent != null &&
               intent.getFlags() == (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                || mHistoryCleared;

    }

    /**
     * Run custom transitions between switching activities.
     *
     * @param intent Intent - used to check for cleared history.
     * @param entering Is activity entering or existing.
     */
    private void runCustomTransitions(Intent intent, boolean entering) {

        if (mCustomTransitions != null) {

            if (isHistoryCleared(intent)) {

                // Unset history flag.
                mHistoryCleared = false;

                switch (mCustomTransitions) {
                    case T_STANDARD:

                        overridePendingTransition(
                                entering ? R.anim.activity_open_scale      : R.anim.activity_open_translate,
                                entering ? R.anim.activity_close_translate : R.anim.activity_close_scale);
                        break;

                    case T_SLIDE_HORIZONTAL:

                        overridePendingTransition(
                                entering ? R.anim.activity_slide_close_in  : R.anim.activity_slide_open_in,
                                entering ? R.anim.activity_slide_close_out : R.anim.activity_slide_open_out);
                        break;
                }
            } else {

                switch (mCustomTransitions) {
                    case T_STANDARD:

                        overridePendingTransition(
                                entering ? R.anim.activity_open_translate : R.anim.activity_open_scale,
                                entering ? R.anim.activity_close_scale    : R.anim.activity_close_translate);
                        break;

                    case T_SLIDE_HORIZONTAL:

                        overridePendingTransition(
                                entering ? R.anim.activity_slide_open_in  : R.anim.activity_slide_close_in,
                                entering ? R.anim.activity_slide_open_out : R.anim.activity_slide_close_out);
                        break;
                }
            }
        }
    }
}