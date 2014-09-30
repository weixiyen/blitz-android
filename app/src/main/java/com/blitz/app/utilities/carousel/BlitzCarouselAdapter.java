package com.blitz.app.utilities.carousel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.blitz.app.R;
import com.blitz.app.utilities.logging.LogHelper;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import java.util.List;

/**
 * Created by mrkcsc on 8/17/14. Copyright 2014 Blitz Studios
 */
public class BlitzCarouselAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    // region Member Variables
    // =============================================================================================

    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    final static int LOOPS = 1000;

    final static float MAX_SCALE = 1.0f;
    final static float MIN_SCALE = 0.7f;

    final static float DIFF_SCALE = MAX_SCALE - MIN_SCALE;

    private BlitzCarouselScalingView cur = null;
    private BlitzCarouselScalingView next = null;

    private Context mContext;

    private ViewPager mViewPager;

    private FragmentManager mFragmentManager;

    private float scale;

    private int mPageCount;
    private int mFirstPage;

    // endregion

    // region Constructors
    // =============================================================================================

    /**
     * Create a new carousel adapter.  Requires a view pager
     * and a fragment manager to internally handle operations.
     *
     * @param viewPager Valid view pager.
     * @param fragmentManager Fragment manager.
     */
    private BlitzCarouselAdapter(ViewPager viewPager, FragmentManager fragmentManager,
                                 int pageCount) {
        super(fragmentManager);

        // Assign member variables.
        mViewPager = viewPager;
        mContext = viewPager.getContext();
        mFragmentManager = fragmentManager;
        mPageCount = pageCount;
        mFirstPage = pageCount * LOOPS / 2;
    }

    // endregion

    public static void createWithViewPager(ViewPager viewPager, FragmentManager fragmentManager,
                                           List<String> avatarIds, List<String> avatarUrls) {

        // Assert valid id's and urls are provided.
        if (avatarIds == null || avatarUrls == null || viewPager == null
                || avatarIds.size() != avatarUrls.size()) {

            return;
        }

        BlitzCarouselAdapter adapter = new BlitzCarouselAdapter
                (viewPager, fragmentManager, avatarIds.size());

        // Assign the adapter.
        viewPager.setAdapter(adapter);

        // Assign it as page change listener.
        viewPager.setOnPageChangeListener(adapter);

        // Set current item to the middle page so we can
        // fling to both directions left and right.
        viewPager.setCurrentItem(adapter.mFirstPage);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see.
        viewPager.setOffscreenPageLimit(3);

        // Set padding for pages as a negative number,
        // so a part of next and previous pages will be showed
        int pixelPadding = ReflectionHelper.densityPixelsToPixels
                (viewPager.getContext(), 90);

        // Assign and clip the padding.
        viewPager.setPadding(pixelPadding, 0, pixelPadding, 0);
        viewPager.setClipToPadding(false);
    }

    @Override
    public Fragment getItem(int position)
    {
// make the first pager bigger than others
        if (position == mFirstPage)
            scale = MAX_SCALE;
        else
            scale = MIN_SCALE;
        position = position % mPageCount;

        LogHelper.log("Item: " + position);

        return BlitzCarouselAdapterFragment.newInstance(mContext, position, scale);
    }

    @Override
    public int getCount()
    {
        return mPageCount * LOOPS;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        if (positionOffset >= 0f && positionOffset <= 1f)
        {
            cur = getRootView(position);
            next = getRootView(position +1);
            cur.setScale(MAX_SCALE
                    - DIFF_SCALE * positionOffset);
            next.setScale(MIN_SCALE
                    + DIFF_SCALE * positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    private BlitzCarouselScalingView getRootView(int position)
    {
        return (BlitzCarouselScalingView)
                mFragmentManager.findFragmentByTag(getFragmentTag(position))
                        .getView().findViewById(R.id.blitz_carousel_helmet);
    }

    private String getFragmentTag(int position)
    {
        return "android:switcher:" + mViewPager.getId() + ":" + position;
    }
}