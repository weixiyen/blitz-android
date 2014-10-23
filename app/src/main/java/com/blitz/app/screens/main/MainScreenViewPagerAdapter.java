package com.blitz.app.screens.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.blitz.app.screens.play.PlayScreen;
import com.blitz.app.screens.recent.RecentScreen;
import com.blitz.app.screens.settings.SettingsScreen;
import com.blitz.app.utilities.viewpager.ViewPagerTransformerZoom;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenViewPagerAdapter extends FragmentStatePagerAdapter
        implements ViewPager.OnPageChangeListener {

    // region Member Variables
    // =============================================================================================

    // Title of each page.
    private static String[] PAGE_TITLES = new String[] { "Play", "Recent", "Settings" };

    // Handle to the adapter callbacks.
    private Callbacks mCallbacks;

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Initialize the adapter.
     */
    private MainScreenViewPagerAdapter(FragmentManager fm) {
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
                return new PlayScreen();
            case 1:
                return new RecentScreen();
            case 2:
                return new SettingsScreen();
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

    // region Public Methods
    // =============================================================================================

    /**
     * Create this adapter with a view pager (convenience method).
     *
     * @param viewPager View pager.
     * @param fragmentManager Fragment manager.
     * @param callbacks Callbacks.
     */
    public static void createWithViewPager(ViewPager viewPager, FragmentManager fragmentManager,
                                           Callbacks callbacks) {

        MainScreenViewPagerAdapter adapter = new MainScreenViewPagerAdapter(fragmentManager);

        // Set the callbacks.
        adapter.mCallbacks = callbacks;

        // Assign the adapter.
        viewPager.setAdapter(adapter);

        // Assign it as page change listener.
        viewPager.setOnPageChangeListener(adapter);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see.
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);

        // Add a custom page transition effect.
        viewPager.setPageTransformer(true, new ViewPagerTransformerZoom());
    }

    // endregion

    // region Page Change Methods
    // =============================================================================================

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {

        if (mCallbacks != null) {
            mCallbacks.onPageSelected(position);
        }
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     */
    @Override
    public void onPageScrollStateChanged(int state) { }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface Callbacks {

        public void onPageSelected(int position);
    }

    // endregion
}