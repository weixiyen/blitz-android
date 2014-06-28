package com.blitz.app.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.base.config.BaseConfig;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.facebook.FacebookHelper;
import com.blitz.app.utilities.viewpager.ViewPagerDepthTransformer;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.InjectView;
import butterknife.OnClick;

public class SplashScreen extends BaseActivity {

    @InjectView(R.id.splash_screen_pager) ViewPager mPager;
    @InjectView(R.id.splash_screen_indicator)

    CirclePageIndicator mPagerIndicator;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

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
     * When activity returns from a facebook authentication
     * activity, pass results into session object.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass results into facebook session object.
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
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

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Start next activity in the flow.
     */
    private void transitionNextScreen() {

        // Create intent for main screen activity.
        Intent intent = new Intent(this, MainScreen.class);

        startActivity(intent);
    }

    /**
     * Get facebook session token then launch
     * next activity.
     */
    private void authenticateWithFacebook() {

        // Create a new facebook helper object.
        final FacebookHelper facebookHelper = new FacebookHelper(this);

        // Authorize the user and launch main screen.
        facebookHelper.authorizeUser(new Request.GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser graphUser, Response response) {

                String accessToken = facebookHelper.getAccessToken(graphUser);

                if (accessToken != null) {
                    transitionNextScreen();
                }
            }
        });
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
        mPager.setPageTransformer(true, new ViewPagerDepthTransformer());
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Register user and take them to main screen.
     */
    @OnClick(R.id.splash_screen_sign_up) @SuppressWarnings("unused")
    public void register() {

        // Authenticate if needed.
        if (BaseConfig.AUTH_WITH_FACEBOOK) {

            authenticateWithFacebook();
        } else {

            // Otherwise just go.
            transitionNextScreen();
        }
    }
}