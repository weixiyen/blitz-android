package com.blitz.app.base.activity;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.blitz.app.R;
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

        if (mCustomTransitions) {

            // Opening transition animations.
            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        }

        // Fetch the class name string in the format of resource view.
        String underscoredClassName = StringHelper.camelCaseToLowerCaseUnderscores(getClass().getSimpleName());

        // Use reflection to fetch the associated view resource id and set content view.
        setContentView(ReflectionHelper.getResourceId(underscoredClassName, R.layout.class));
    }

    /**
     * Run custom transitions if needed.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (mCustomTransitions) {

            // Closing transition animations.
            overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
        }
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

    //==============================================================================================
    // Public Methods
    //==============================================================================================

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
}