package com.blitz.app.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.blitz.app.R;
import com.blitz.app.screens.loading.LoadingScreen;
import com.blitz.app.screens.web.WebScreen;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.sound.SoundHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreenFragmentSettings extends BaseFragment {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.main_screen_fragment_settings_toggle_music) Switch mSettingsToggleMusic;
    @InjectView(R.id.main_screen_fragment_settings_toggle_sound) Switch mSettingsToggleSound;

    // Reset button (only display for QA).
    @InjectView(R.id.main_settings_reset) View mSettingsReset;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Setup main navigational interface.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize switch states.
        mSettingsToggleMusic.setChecked(!AppDataObject.settingsMusicDisabled.getBoolean());
        mSettingsToggleSound.setChecked(!AppDataObject.settingsSoundDisabled.getBoolean());

        if (!AppConfig.isProduction()) {

            // Hide if not on production.
            mSettingsReset.setVisibility(View.GONE);
        }
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Toggle music setting.
     */
    @OnClick(R.id.main_screen_fragment_settings_toggle_music) @SuppressWarnings("unused")
    public void toggleMusic() {

        // Update the music disabled flag.
        AppDataObject.settingsMusicDisabled.set(!mSettingsToggleMusic.isChecked());

        // Set the disabled state of the music player.
        SoundHelper.instance().setMusicDisabled(AppDataObject.settingsMusicDisabled.getBoolean());
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

        startActivity(intent);
    }

    /**
     * Show privacy policy.
     */
    @OnClick(R.id.main_settings_privacy_policy) @SuppressWarnings("unused")
    public void privacyPolicyClicked() {

        Intent i = new Intent(this.getActivity(), WebScreen.class);

        // Pass parameters to the web screen.
        i.putExtra(WebScreen.PARAM_URL, AppConfig.getPrivacyPolicyUrl());
        i.putExtra(WebScreen.PARAM_TITLE, "Privacy Policy");

        startActivity(i);
    }

    /**
     * Sign user out when reset clicked.
     */
    @OnClick(R.id.main_settings_reset) @SuppressWarnings("unused")
    public void resetClicked() {

        // Sign out user.
        AuthHelper.signOut();

        // Bounce user back to the loading screen.
        startActivity(new Intent(this.getActivity(), LoadingScreen.class));
    }
}