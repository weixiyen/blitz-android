package com.blitz.app.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.dialogs.info.DialogInfo;
import com.blitz.app.dialogs.loading.DialogLoading;
import com.blitz.app.screens.draft.DraftScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.android.BaseDialog;
import com.blitz.app.utilities.viewpager.ViewPagerZoomOutTransformer;
import com.blitz.app.view_models.ViewModelMain;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
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
    @InjectViews({
            R.id.main_nav_play_active,
            R.id.main_nav_recent_active,
            R.id.main_nav_settings_active}) List<View> mNavActiveButtons;

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
     * "play" before backing out.
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
        mPager.setPageTransformer(true, new ViewPagerZoomOutTransformer());

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) { }

            @Override
            public void onPageScrollStateChanged(int i) { }

            @Override
            public void onPageSelected(int i) {

                // Enable nav item.
                selectNavItemWithTag(i);
            }
        });
    }

    /**
     * Deselect all navigation bar items.
     */
    private void selectNavItemWithTag(int tag) {

        // Hide all nav button glows first.
        ButterKnife.apply(mNavActiveButtons, new ButterKnife.Action<View>() {

            @Override
            public void apply(View view, int index) {

                view.setVisibility(View.GONE);
            }
        });

        // Set active nav button.
        mNavActiveButtons.get(tag).setVisibility(View.VISIBLE);
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
                    startActivity(new Intent(MainScreen.this, DraftScreen.class), true);
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
    @OnClick({R.id.main_nav_button_play,
              R.id.main_nav_button_recent,
              R.id.main_nav_button_settings})
    @SuppressWarnings("unused")
    public void navClicked(View button) {

        // Fetch tag associated with the item clicked.
        int tag = Integer.parseInt((String) button.getTag());

        // Enable nav item.
        selectNavItemWithTag(tag);

        // Sync with view pager when selected.
        mPager.setCurrentItem(tag);
    }
}