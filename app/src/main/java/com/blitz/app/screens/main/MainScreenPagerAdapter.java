package com.blitz.app.screens.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.blitz.app.utilities.app.AppConfig;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenPagerAdapter extends FragmentStatePagerAdapter {

    // region Member Variables
    // =============================================================================================

    // Title of each page.
    private static String[] PAGE_TITLES = new String[] { "Play", "Recent", "Settings" };

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Initialize the adapter.
     */
    public MainScreenPagerAdapter(FragmentManager fm) {
        super(fm);

        // Only show play screen for now.
        if (AppConfig.isProduction()) {

            PAGE_TITLES = new String[] { "Play" };
        }
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainScreenFragmentPlay();
            case 1:
                return new MainScreenFragmentRecent();
            case 2:
                return new MainScreenFragmentSettings();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLES[position];
    }

    @Override
    public int getCount() {
        return PAGE_TITLES.length;
    }

    // endregion
}