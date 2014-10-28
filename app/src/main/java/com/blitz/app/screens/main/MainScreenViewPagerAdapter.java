package com.blitz.app.screens.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.blitz.app.utilities.viewpager.ViewPagerTransformerZoom;

import java.util.List;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenViewPagerAdapter extends FragmentStatePagerAdapter
        implements ViewPager.OnPageChangeListener {

    // region Member Variables
    // ============================================================================================================

    // Title of each page.
    private List<String> mScreens;

    // Handle to the adapter callbacks.
    private Callbacks mCallbacks;

    // Holds the context.
    private Context mContext;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Initialize the adapter.
     */
    private MainScreenViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

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

        // Return new instance of the associated screen.
        return Fragment.instantiate(mContext, mScreens.get(position));
    }

    /**
     * Get total pages.
     *
     * @return Total pages.
     */
    @Override
    public int getCount() {

        return mScreens.size();
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Create this adapter with a view pager (convenience method).
     *
     * @param viewPager View pager.
     * @param fragmentManager Fragment manager.
     * @param callbacks Callbacks.
     */
    public static void createWithViewPager(ViewPager viewPager, FragmentManager fragmentManager,
                                           Callbacks callbacks, List<String> screens) {

        MainScreenViewPagerAdapter adapter = new MainScreenViewPagerAdapter(fragmentManager);

        // Set the callbacks.
        adapter.mCallbacks = callbacks;

        // Set screens.
        adapter.mScreens = screens;

        // Set the context.
        adapter.mContext = viewPager.getContext();

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
    // ============================================================================================================

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
    // ============================================================================================================

    public interface Callbacks {

        public void onPageSelected(int position);
    }

    // endregion
}