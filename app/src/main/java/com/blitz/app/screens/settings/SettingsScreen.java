package com.blitz.app.screens.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.payments.DepositScreen;
import com.blitz.app.screens.payments.TransactionListViewScreen;
import com.blitz.app.screens.web.WebScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppData;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.carousel.BlitzCarouselAdapter;
import com.blitz.app.utilities.sound.SoundHelper;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelSettings;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class SettingsScreen extends BaseFragment implements ViewModelSettings.Callbacks,
        BlitzCarouselAdapter.Callbacks {

    // region Member Variables
    // ============================================================================================================

    // Toggle switches.
    @InjectView(R.id.settings_toggle_music) Switch mSettingsToggleMusic;
    @InjectView(R.id.settings_toggle_sound) Switch mSettingsToggleSound;

    // Reset button.
    @InjectView(R.id.settings_reset) View mSettingsReset;

    // Misc settings elements.
    @InjectView(R.id.settings_email) TextView mSettingsEmail;
    @InjectView(R.id.cash_balance) TextView mCashBalance;
    @InjectView(R.id.settings_version) TextView mSettingsVersion;

    // Helmet view pager.
    @InjectView(R.id.settings_carousel) ViewPager mCarouselViewPager;

    // View model.
    private ViewModelSettings mViewModelSettings;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Setup main navigational interface.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize switch states.
        mSettingsToggleMusic.setChecked(!AppDataObject.settingsMusicDisabled.get());
        mSettingsToggleSound.setChecked(!AppDataObject.settingsSoundDisabled.get());

        if (AppConfig.isProduction()) {

            // Hide if not on production.
            mSettingsReset      .setVisibility(View.GONE);
        }
    }

    /**
     * Fetch the view model.
     *
     * @return View model object.
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModelSettings == null) {
            mViewModelSettings = new ViewModelSettings(getBaseActivity(), this);
        }

        return mViewModelSettings;
    }

    // endregion

    // region Click Methods
    // ============================================================================================================

    /**
     * Deposit some dollars.
     */
    @OnClick(R.id.settings_deposit_money) @SuppressWarnings("unused")
    public void depositMoneyClicked() {

        startActivity(new Intent(getActivity(), DepositScreen.class));
    }

    /**
     * Withdraw some dollars.
     */
    @OnClick(R.id.settings_withdraw_money) @SuppressWarnings("unused")
    public void withdrawMoneyClicked() {

        // TODO
    }

    /**
     * Navigate to transaction history
     */
    @OnClick(R.id.settings_transaction_history) @SuppressWarnings("unused")
    public void transactionHistoryClicked() {

        startActivity(new Intent(getActivity(), TransactionListViewScreen.class));
    }

    /**
     * Toggle music setting.
     */
    @OnClick(R.id.settings_toggle_music) @SuppressWarnings("unused")
    public void toggleMusicClicked() {

        // Update the music disabled flag.
        AppDataObject.settingsMusicDisabled.set(!mSettingsToggleMusic.isChecked());

        // Set the disabled state of the music player.
        SoundHelper.instance().setMusicDisabled(AppDataObject.settingsMusicDisabled.get());
    }

    /**
     * Show terms of use.
     */
    @OnClick(R.id.settings_terms_of_use) @SuppressWarnings("unused")
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
    @OnClick(R.id.settings_privacy_policy) @SuppressWarnings("unused")
    public void privacyPolicyClicked() {

        Intent intent = new Intent(this.getActivity(), WebScreen.class);

        // Pass parameters to the web screen.
        intent.putExtra(WebScreen.PARAM_URL, AppConfig.getPrivacyPolicyUrl());
        intent.putExtra(WebScreen.PARAM_TITLE, "Privacy Policy");
        intent.putExtra(WebScreen.PARAM_TRANSITION_TYPE,
                BaseActivity.CustomTransition.T_SLIDE_VERTICAL);

        startActivity(intent);
    }

    @OnClick(R.id.settings_support) @SuppressWarnings("unused")
    public void supportClicked() {

        // Create a send action.
        Intent intent = new Intent(Intent.ACTION_SEND);

        // Set it to email type.
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@blitzstudios.net" });

        try {

            // Attempt to open an email activity.
            startActivity(Intent.createChooser(intent, "Contact support"));

        } catch (Exception ignored) { }
    }

    /**
     * Log out user when clicked.
     */
    @OnClick(R.id.settings_sign_out) @SuppressWarnings("unused")
    public void signOutClicked() {

        AuthHelper.instance().signOut(this.getActivity());
    }

    /**
     * Reset all app data when clicked.
     */
    @OnClick(R.id.settings_reset) @SuppressWarnings("unused")
    public void resetClicked() {

        // Clear app data.
        AppData.clear();

        // Bounce user back to the loading screen.
        AuthHelper.instance().tryEnterMainApp(this.getActivity());
    }

    // endregion

    // region View Model & Carousel Callbacks
    // ============================================================================================================

    /**
     * Current email address received.
     *
     * @param email Email address.
     */
    @Override
    public void onEmail(String email) {

        if (mSettingsEmail != null) {
            mSettingsEmail.setText(email);
        }
    }

    /**
     * User's cash balance
     */
    @Override
    public void onCash(int balance) {

        if (mCashBalance != null) {
            mCashBalance.setText(String.format("Account Balance: $%.2f", balance / 100.0f));
        }
    }

    /**
     * When avatars are received, create and/or
     * update the helmet carousel.
     *
     * @param userAvatarIds List of ids.
     * @param userAvatarUrls List of urls.
     * @param userAvatarId Current user avatar id.
     */
    @Override
    public void onAvatars(List<String> userAvatarIds,
                          List<String> userAvatarUrls, String userAvatarId) {

        // Create the avatar carousel.
        BlitzCarouselAdapter.createWithViewPager(mCarouselViewPager, getChildFragmentManager(),
                userAvatarIds, userAvatarUrls, userAvatarId, this);
    }

    /**
     * Set the version.
     *
     * @param version Version.
     */
    @Override
    public void onVersion(String version) {

        if (mSettingsVersion != null) {
            mSettingsVersion.setText(version);
        }
    }

    /**
     * When a carousel item is selected,
     * update the user information.
     *
     * @param itemId Helmet id.
     */
    @Override
    public void onCarouselItemSelected(String itemId) {

        // Update avatar.
        mViewModelSettings.updateUserAvatar(itemId);
    }

    // endregion
}