package com.blitz.app.utilities.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mrkcsc on 10/22/14. Copyright 2014 Blitz Studios
 */
public abstract class BlitzViewPagerAdapter extends FragmentStatePagerAdapter {

    // region Member Variables
    // =============================================================================================

    // Array of registered fragments / root views.
    SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();
    SparseArray<View>     mRegisteredRootViews = new SparseArray<View>();

    // endregion

    // region Constructor
    // =============================================================================================

    @SuppressWarnings("unused")
    public BlitzViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * When a fragment is instantiated, save
     * a reference to it.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container, position);

        // Save handler to the fragment.
        mRegisteredFragments.put(position, fragment);

        return fragment;
    }

    /**
     * When a fragment is destroyed, also remove it
     * from our list of registered fragment.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        // Remove root views and fragments.
        mRegisteredFragments.remove(position);
        mRegisteredRootViews.remove(position);

        // Ensure handle to fragment is destroyed.
        super.destroyItem(container, position, object);
    }

    // endregion

    // region Protected Methods
    // =============================================================================================

    /**
     * Fetch currently registered fragment
     * at the specified position.
     *
     * @param position View pager position.
     *
     * @return Fragment instance, or null if unavailable.
     */
    @SuppressWarnings("unused")
    protected Fragment getRegisteredFragment(int position) {
        return mRegisteredFragments.get(position);
    }

    /**
     * Fetch root view at position.
     *
     * @param position View pager position.
     *
     * @return Root view at position.
     */
    @SuppressWarnings("unused")
    protected View getRegisteredRootView(int position, int rootViewId) {

        Fragment fragment = getRegisteredFragment(position);

        // If fragment registered and no root view loaded.
        if (fragment != null && mRegisteredRootViews.get(position) == null) {

            // Load the root view into the array.
            mRegisteredRootViews.put(position, fragment.getView().findViewById(rootViewId));
        }

        return mRegisteredRootViews.get(position);
    }

    // endregion
}