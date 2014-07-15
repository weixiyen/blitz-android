package com.blitz.app.screens.main;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.viewpager.ViewPagerDepthTransformer;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreen extends BaseActivity implements ActionBar.TabListener {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.main_screen_pager) ViewPager mPager;

    // Adapter for view pager.
    private MainScreenPagerAdapter mAdapter;

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

        // Setup view pager tabs.
        setupViewPagerTabs();
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        //startActivity(new Intent(this, MainScreen.class));
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Create and setup the viewpager and associated
     * elements.
     */
    private void setupViewPager() {

        // Create adapter.
        mAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());

        // Create adapter for the view pager.
        mPager.setAdapter(mAdapter);

        // Add a custom page transition effect.
        mPager.setPageTransformer(true, new ViewPagerDepthTransformer());
    }

    /**
     * Setup tabs associated with view pager. Uses
     * a custom widget for total customization.
     *
     * TODO: Use custom widget.
     */
    private void setupViewPagerTabs() {
        ActionBar bar = getActionBar();

        if (bar != null) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // Iterate over each tab.
            for (int tabIndex = 0; tabIndex < mAdapter.getCount(); tabIndex++) {

                // Initialize a tab.
                ActionBar.Tab tab = bar.newTab();

                // Configure it.
                tab.setTag(tabIndex);
                tab.setTabListener(this);
                tab.setText(mAdapter.getPageTitle(tabIndex));

                // Add it.
                bar.addTab(tab);
            }
        }
    }
}