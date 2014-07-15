package com.blitz.app.utilities.android;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.blitz.app.R;
import com.blitz.app.utilities.background.EnteredBackground;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.reflection.ReflectionHelper;
import com.blitz.app.utilities.string.StringHelper;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Shared base functionality across all activities.
 */
public class BaseActivity extends FragmentActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Set custom transitions.
    private boolean mCustomTransitions = true;

    // Track if activity history is cleared.
    private boolean mHistoryCleared = false;

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

        setupActionBar();

        // Run transitions, we are entering.
        runCustomTransitions(getIntent(), true);

        // Fetch the class name string in the format of resource view.
        String underscoredClassName = StringHelper.camelCaseToLowerCaseUnderscores(getClass().getSimpleName());

        // Use reflection to fetch the associated view resource id and set content view.
        setContentView(ReflectionHelper.getResourceId(underscoredClassName, R.layout.class));
    }

    /**
     * Update the keyboard utility listener.
     */
    @Override
    protected void onStart () {
        super.onStart();

        // Update the layout listener.
        KeyboardUtility.setGlobalLayoutListener(this);
    }

    /**
     * Figure out if application has returned
     * from the background.
     */
    @Override
    protected void onResume () {
        super.onResume();

        // Stop timer to detect entering the background.
        EnteredBackground.stopActivityTransitionTimer();
    }

    /**
     * Run custom transitions if needed, also
     * start timer to detect entering background.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Run transitions, we are exiting.
        runCustomTransitions(null, false);

        // Start timer to detect entering the background.
        EnteredBackground.startActivityTransitionTimer();
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
     * @param customTransitions Custom transitions.
     */
    @SuppressWarnings("unused")
    public void setContentView(int layoutResID, boolean customTransitions) {
        mCustomTransitions = customTransitions;

        setContentView(layoutResID);
    }

    /**
     * Set custom transitions on or off.
     *
     * @param customTransitions Custom transitions.
     */
    public void setCustomTransitions(boolean customTransitions) {
        mCustomTransitions = customTransitions;
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Setup the action bar.  For the time being,
     * we do not want it to display and only use
     * it for the purpose of tabs.
     */
    private void setupActionBar() {

        ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

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

        if (mCustomTransitions) {

            if (isHistoryCleared(intent)) {

                mHistoryCleared = false;

                // Reverse sequence.
                overridePendingTransition(
                        entering ? R.anim.activity_open_scale      : R.anim.activity_open_translate,
                        entering ? R.anim.activity_close_translate : R.anim.activity_close_scale);

            } else {

                // Standard sequence.
                overridePendingTransition(
                        entering ? R.anim.activity_open_translate : R.anim.activity_open_scale,
                        entering ? R.anim.activity_close_scale    : R.anim.activity_close_translate);
            }
        }
    }
}