package com.blitz.app.utilities.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by mrkcsc on 10/22/14. Copyright 2014 Blitz Studios
 */
public abstract class BlitzViewPagerAdapter extends FragmentStatePagerAdapter {

    // region Member Variables
    // =============================================================================================

    // Array of registered fragments.
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

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
        registeredFragments.put(position, fragment);

        return fragment;
    }

    /**
     * When a fragment is destroyed, also remove it
     * from our list of registered fragment.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);

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
        return registeredFragments.get(position);
    }

    // endregion
}