package com.blitz.app.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.blitz.app.R;
import com.blitz.app.dialogs.DialogInfo;
import com.blitz.app.dialogs.DialogLoading;
import com.blitz.app.view_models.ViewModelMain;
import com.blitz.app.screens.draft_preview.DraftPreviewScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseDialog;
import com.blitz.app.utilities.viewpager.ViewPagerDepthTransformer;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreen extends BaseActivity implements ViewModelMain.ViewModelMainCallbacks {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Fragment view pager.
    @InjectView(R.id.main_screen_pager) ViewPager mPager;

    // Navigation bar tabs.
    @InjectView(R.id.main_screen_nav_featured_active) View mNavFeaturedActive;
    @InjectView(R.id.main_screen_nav_recent_active)   View mNavRecentActive;
    @InjectView(R.id.main_screen_nav_settings_active) View mNavSettingsActive;

    // Info/loading dialog.
    private DialogInfo mDialogInfo;
    private DialogLoading mDialogLoading;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Setup main navigational interface.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view model.
        setViewModel(new ViewModelMain(), savedInstanceState);

        // Setup view pager.
        setViewPager();
    }

    /**
     * Bounce user back to first page
     * "featured" before backing out.
     */
    @Override
    public void onBackPressed() {

        if (mPager.getCurrentItem() > 0) {
            mPager.setCurrentItem(0);
        } else {

            super.onBackPressed();
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Create and setup the viewpager and associated
     * elements.
     */
    private void setViewPager() {

        // Create adapter.
        MainScreenPagerAdapter adapter = new
                MainScreenPagerAdapter(getSupportFragmentManager());

        // Create adapter for the view pager.
        mPager.setAdapter(adapter);

        // Add a custom page transition effect.
        mPager.setPageTransformer(true, new ViewPagerDepthTransformer());

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) { }

            @Override
            public void onPageScrollStateChanged(int i) { }

            @Override
            public void onPageSelected(int i) {

                deselectNav();

                switch (i) {
                    case 0:
                        mNavFeaturedActive.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        mNavRecentActive  .setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        mNavSettingsActive.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    /**
     * Deselect all navigation bar items.
     */
    private void deselectNav() {

        mNavFeaturedActive.setVisibility(View.GONE);
        mNavRecentActive  .setVisibility(View.GONE);
        mNavSettingsActive.setVisibility(View.GONE);
    }

    //==============================================================================================
    // View Model Callbacks
    //==============================================================================================

    /**
     * Show a confirmation dialog for the draft.
     *
     * @param viewModel View model.
     */
    @Override
    public void onConfirmDraft(final ViewModelMain viewModel) {

        // Initialize dialog.
        if (mDialogInfo == null) {
            mDialogInfo = new DialogInfo(this);

            // Configure the dialog.
            mDialogInfo.setInfoText(R.string.match_found);
            mDialogInfo.setInfoLeftButton(R.string.cancel, new Runnable() {

                @Override
                public void run() {

                    // Leave the queue.
                    viewModel.leaveQueue();
                }
            });

            mDialogInfo.setInfoRightButton(R.string.join, new Runnable() {

                @Override
                public void run() {

                    // Confirm the draft.
                    viewModel.confirmQueue();
                }
            });
        }

        mDialogInfo.show(true);
    }

    /**
     * When draft queue confirmed, setup a loading state
     * while we wait for the draft enter confirmation.
     *
     * @param viewModel View model.
     */
    @Override
    public void onConfirmQueue(ViewModelMain viewModel) {

        // Initialize loading dialog.
        if (mDialogLoading == null) {
            mDialogLoading = new DialogLoading(MainScreen.this);
        }

        // Hide info dialog and show loading one.
        mDialogInfo.hide(new BaseDialog.HideListener() {

            @Override
            public void didHide() {

                mDialogLoading.show(true);
            }
        });
    }

    /**
     * Hide the confirmation dialog for the draft.
     *
     * @param viewModel View model.
     */
    @Override
    public void onLeftQueue(ViewModelMain viewModel) {

        // Hide if needed.
        if (mDialogInfo != null) {
            mDialogInfo.hide(null);
        }

        // Hide if needed.
        if (mDialogLoading != null) {
            mDialogLoading.hide(null);
        }
    }

    /**
     * Transition into the main draft flow.
     *
     * @param viewModel View model.
     */
    @Override
    public void onEnterDraft(ViewModelMain viewModel) {

        // Hide loading dialog.
        if (mDialogLoading != null) {
            mDialogLoading.hide(new BaseDialog.HideListener() {

                @Override
                public void didHide() {

                    // Enter draft preview and clear history.
                    startActivity(new Intent(MainScreen.this, DraftPreviewScreen.class), true);
                }
            });
        }
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Change active nav item, and
     * switch to associated fragment.
     *
     * @param button Button clicked.
     */
    @OnClick({R.id.main_screen_nav_featured,
              R.id.main_screen_nav_recent,
              R.id.main_screen_nav_settings})
    @SuppressWarnings("unused")
    public void navClicked(Button button) {

        deselectNav();

        switch (button.getId()) {

            case R.id.main_screen_nav_featured:
                mNavFeaturedActive.setVisibility(View.VISIBLE);
                break;

            case R.id.main_screen_nav_recent:
                mNavRecentActive  .setVisibility(View.VISIBLE);
                break;

            case R.id.main_screen_nav_settings:
                mNavSettingsActive.setVisibility(View.VISIBLE);
                break;
        }

        // Sync with view pager when selected.
        mPager.setCurrentItem(Integer.parseInt((String) button.getTag()));
    }
}