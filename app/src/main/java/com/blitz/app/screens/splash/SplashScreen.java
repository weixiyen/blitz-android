package com.blitz.app.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.blitz.app.R;
import com.blitz.app.screens.sign_in.SignInScreen;
import com.blitz.app.screens.sign_up.SignUpScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.viewpager.ViewPagerTransformerDepth;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.InjectView;
import butterknife.OnClick;

public class SplashScreen extends BaseActivity {

    // region Member Variables
    // ============================================================================================================

    @InjectView(R.id.splash_screen_pager)               ViewPager mPager;
    @InjectView(R.id.splash_screen_indicator) CirclePageIndicator mPagerIndicator;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Setup the activity.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup view pager.
        setupViewPager();
    }

    /**
     * If the user is currently looking at the first step,
     * allow the system to handle the back button.
     *
     * This calls finish() on this activity and pops the back stack.
     */
    @Override
    public void onBackPressed() {

        if (mPager.getCurrentItem() > 0) {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        } else {

            super.onBackPressed();
        }
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Start next activity in the flow.
     */
    private void transitionNextScreen(Class targetScreen) {

        // Create intent for main screen activity.
        Intent intent = new Intent(this, targetScreen);

        startActivity(intent);
    }

    /**
     * Create and setup the viewpager and associated
     * elements.
     */
    private void setupViewPager() {

        // Create adapter for the view pager.
        mPager.setAdapter(new SplashScreenPagerAdapter(getSupportFragmentManager()));

        // Create dots for the view pager.
        mPagerIndicator.setViewPager(mPager);

        // Add a custom page transition effect.
        mPager.setPageTransformer(true, new ViewPagerTransformerDepth());
    }

    // endregion

    // region Click Methods
    // ============================================================================================================

    @OnClick(R.id.splash_screen_sign_in) @SuppressWarnings("unused")
    public void sign_in() {

        transitionNextScreen(SignInScreen.class);
    }

    @OnClick(R.id.splash_screen_sign_up) @SuppressWarnings("unused")
    public void sign_up() {

        transitionNextScreen(SignUpScreen.class);
    }

    // endregion
}