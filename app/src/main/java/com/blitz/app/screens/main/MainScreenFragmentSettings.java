package com.blitz.app.screens.main;

import android.os.Bundle;
import android.widget.Switch;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.app.AppDataObject;
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
}