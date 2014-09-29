package com.blitz.app.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.blitz.app.R;
import com.blitz.app.screens.loading.LoadingScreen;
import com.blitz.app.screens.web.WebScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.carousel.MyPagerAdapter;
import com.blitz.app.utilities.reflection.ReflectionHelper;
import com.blitz.app.utilities.sound.SoundHelper;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelSettings;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenFragmentSettings extends BaseFragment implements ViewModelSettings.ViewModelSettingsCallbacks {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.main_screen_fragment_settings_toggle_music) Switch mSettingsToggleMusic;
    @InjectView(R.id.main_screen_fragment_settings_toggle_sound) Switch mSettingsToggleSound;

    // Reset button (only display for QA).
    @InjectView(R.id.main_settings_reset)        View mSettingsReset;
    @InjectView(R.id.main_settings_reset_border) View mSettingsResetBorder;

    public @InjectView(R.id.main_settings_carousel) ViewPager pager;

    public MyPagerAdapter adapter;

    public final static int PAGES = 5;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
// more than 1000 times just in order to test your "infinite" ViewPager :D
    public final static int LOOPS = 1000;
    public final static int FIRST_PAGE = PAGES * LOOPS / 2;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private ViewModelSettings mViewModel;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup main navigational interface.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        adapter = new MyPagerAdapter(this, getChildFragmentManager());

        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(adapter);
        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);
        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        pager.setOffscreenPageLimit(3);
        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed

        int pixelPadding = ReflectionHelper.densityPixelsToPixels(getActivity(), 90);
        int pagePadding = ReflectionHelper.densityPixelsToPixels(getActivity(), 10);

        pager.setPadding(pixelPadding, 0, pixelPadding, 0);
        pager.setClipToPadding(false);



        // Initialize switch states.
        mSettingsToggleMusic.setChecked(!AppDataObject.settingsMusicDisabled.get());
        mSettingsToggleSound.setChecked(!AppDataObject.settingsSoundDisabled.get());

        if (AppConfig.isProduction()) {

            // Hide if not on production.
            mSettingsReset      .setVisibility(View.GONE);
            mSettingsResetBorder.setVisibility(View.GONE);
        }
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    /**
     * Toggle music setting.
     */
    @OnClick(R.id.main_screen_fragment_settings_toggle_music) @SuppressWarnings("unused")
    public void toggleMusic() {

        // Update the music disabled flag.
        AppDataObject.settingsMusicDisabled.set(!mSettingsToggleMusic.isChecked());

        // Set the disabled state of the music player.
        SoundHelper.instance().setMusicDisabled(AppDataObject.settingsMusicDisabled.get());
    }

    /**
     * Show terms of use.
     */
    @OnClick(R.id.main_settings_terms_of_use) @SuppressWarnings("unused")
    public void termsOfUseClicked() {

        Intent intent = new Intent(this.getActivity(), WebScreen.class);

        // Pass parameters to the web screen.
        intent.putExtra(WebScreen.PARAM_URL, AppConfig.getTermsOfUseUrl());
        intent.putExtra(WebScreen.PARAM_TITLE, "Terms Of Use");
        intent.putExtra(WebScreen.PARAM_TRANSITION_TYPE,
                BaseActivity.CustomTransition.T_SLIDE_VERTICAL);

        startActivity(intent);
    }

    /**
     * Show privacy policy.
     */
    @OnClick(R.id.main_settings_privacy_policy) @SuppressWarnings("unused")
    public void privacyPolicyClicked() {

        Intent intent = new Intent(this.getActivity(), WebScreen.class);

        // Pass parameters to the web screen.
        intent.putExtra(WebScreen.PARAM_URL, AppConfig.getPrivacyPolicyUrl());
        intent.putExtra(WebScreen.PARAM_TITLE, "Privacy Policy");
        intent.putExtra(WebScreen.PARAM_TRANSITION_TYPE,
                BaseActivity.CustomTransition.T_SLIDE_VERTICAL);

        startActivity(intent);
    }

    /**
     * Log out user when clicked.
     */
    @OnClick(R.id.main_settings_logout) @SuppressWarnings("unused")
    public void logoutClicked() {

        // Sign out user.
        AuthHelper.instance().signOut();

        // Bounce user back to the loading screen.
        startActivity(new Intent(this.getActivity(), LoadingScreen.class));
    }

    /**
     * Reset all app data when clicked.
     */
    @OnClick(R.id.main_settings_reset) @SuppressWarnings("unused")
    public void resetClicked() {

        // Clear app data.
        AppData.clear();

        // Bounce user back to the loading screen.
        startActivity(new Intent(this.getActivity(), LoadingScreen.class));
    }

    @Override
    public void onEmail(String email) {

        ((Button)getActivity().findViewById(R.id.email)).setText(email);
    }

    @Override
    public ViewModel onFetchViewModel() {

        if(mViewModel == null) {
            mViewModel = new ViewModelSettings(getActivity(), this);
        }
        return mViewModel;
    }

    // endregion
}