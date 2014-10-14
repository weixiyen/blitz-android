package com.blitz.app.screens.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.blitz.app.screens.recent.RecentScreen;

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
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Return one of the primary screens
     * depending on pager position.
     *
     * @param position Position.
     *
     * @return Initialized screen fragment.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainScreenFragmentPlay();
            case 1:
                return new RecentScreen();
            case 2:
                return new MainScreenFragmentSettings();
            default:
                return null;
        }
    }

    /**
     * Get associated page title.
     *
     * @param position Position.
     *
     * @return Page title.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLES[position];
    }

    /**
     * Get total pages.
     *
     * @return Total pages.
     */
    @Override
    public int getCount() {
        return PAGE_TITLES.length;
    }

    // endregion
}