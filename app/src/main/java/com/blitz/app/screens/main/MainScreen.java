package com.blitz.app.screens.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.blitz.app.R;
import com.blitz.app.dialogs.DialogInfo;
import com.blitz.app.models.comet.CometAPICallback;
import com.blitz.app.models.comet.CometAPIManager;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.viewpager.ViewPagerDepthTransformer;
import com.google.gson.JsonObject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreen extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Fragment view pager.
    @InjectView(R.id.main_screen_pager) ViewPager mPager;

    // Navigation bar tabs.
    @InjectView(R.id.main_screen_nav_featured_active) View mNavFeaturedActive;
    @InjectView(R.id.main_screen_nav_recent_active)   View mNavRecentActive;
    @InjectView(R.id.main_screen_nav_settings_active) View mNavSettingsActive;

    // Info dialog.
    private DialogInfo mDialogInfo;

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

        // Setup view pager.
        setupViewPager();

        // Setup callbacks.
        setupDraftCallbacks();
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
    // Public Methods
    //==============================================================================================

    /**
     * Show a confirmation dialog for the draft.
     */
    public void showConfirmDraftDialog() {

        // Initialize dialog.
        if (mDialogInfo == null) {
            mDialogInfo = new DialogInfo(this);

            // Configure the dialog.
            mDialogInfo.setInfoText("Match Found! Join the match!");
            mDialogInfo.setInfoLeftButton("Cancel", new Runnable() {

                @Override
                public void run() {

                    // TODO: Cancel the queue.
                }
            });
            mDialogInfo.setInfoRightButton("Join!", new Runnable() {

                @Override
                public void run() {

                    // TODO: Join the draft.
                }
            });
        }

        mDialogInfo.show(true);
    }

    /**
     * Hide the confirmation dialog for the draft.
     */
    public void hideConfirmDraftDialog() {

        // Hide if needed.
        if (mDialogInfo != null) {
            mDialogInfo.hide(null);
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Setup callbacks for the draft
     * powered by the comet manager.
     */
    private void setupDraftCallbacks() {

        // Fetch comet channel for this user.
        String userCometChannel = "user:" + AppDataObject.userId.getString();

        // Subscribe to channel, set callback.
        CometAPIManager

                // Subscribe to user channel.
                .subscribeToChannel(userCometChannel)

                // Set callback action.
                .addCallback(this, new CometAPICallback<MainScreen>() {

                    @Override
                    public void messageReceived(MainScreen receivingClass, JsonObject message) {

                        receivingClass.handleDraftAction(receivingClass, message);
                    }
                }, "draftUserCallbackMainScreen");
    }

    /**
     * Handle a draft callback action.  Either
     * show or hide a confirmation dialog, or
     * simply enter the draft.
     *
     * @param receivingClass Instance of this activity.
     * @param message Json message sent.
     */
    private void handleDraftAction(MainScreen receivingClass, JsonObject message) {

        // Fetch sent action.
        String action = message.get("action").getAsString();

        if (action.equals("confirm_draft")) {

            // Present confirmation dialog to user.
            receivingClass.showConfirmDraftDialog();

        } else if (action.equals("left_queue")) {

            // Dismiss dialog.
            receivingClass.hideConfirmDraftDialog();
        }
    }

    /**
     * Create and setup the viewpager and associated
     * elements.
     */
    private void setupViewPager() {

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