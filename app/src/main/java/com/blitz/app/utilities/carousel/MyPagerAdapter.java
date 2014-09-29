package com.blitz.app.utilities.carousel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.blitz.app.R;
import com.blitz.app.utilities.reflection.ReflectionHelper;

/**
 * Created by mrkcsc on 8/17/14. Copyright 2014 Blitz Studios
 */
public class MyPagerAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {

    // region Member Variables
    // =============================================================================================

    public final static int PAGES = 5;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public final static int LOOPS = 1000;
    public final static int FIRST_PAGE = PAGES * LOOPS / 2;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;

    private Context context;

    private ViewPager mViewPager;

    private FragmentManager mFragmentManager;

    private float scale;

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
    private MyPagerAdapter(ViewPager viewPager, FragmentManager fragmentManager) {
        super(fragmentManager);

        // Assign view pager.
        mViewPager = viewPager;

        // Assign fragment manager.
        mFragmentManager = fragmentManager;

        // Assign context.
        context = viewPager.getContext();
    }

    // endregion

    public static void createWithViewPager(ViewPager viewPager, FragmentManager fragmentManager) {

        if (viewPager != null) {

            MyPagerAdapter adapter = new MyPagerAdapter(viewPager, fragmentManager);

            // Assign the adapter.
            viewPager.setAdapter(adapter);

            // Assign it as page change listener.
            viewPager.setOnPageChangeListener(adapter);

            // Set current item to the middle page so we can fling to both
            // directions left and right
            viewPager.setCurrentItem(FIRST_PAGE);
            // Necessary or the pager will only have one extra page to show
            // make this at least however many pages you can see
            viewPager.setOffscreenPageLimit(3);
            // Set margin for pages as a negative number, so a part of next and
            // previous pages will be showed

            int pixelPadding = ReflectionHelper.densityPixelsToPixels(viewPager.getContext(), 90);

            viewPager.setPadding(pixelPadding, 0, pixelPadding, 0);
            viewPager.setClipToPadding(false);
        }
    }

    @Override
    public Fragment getItem(int position)
    {
// make the first pager bigger than others
        if (position == FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;
        position = position % PAGES;

        return MyFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount()
    {
        return PAGES * LOOPS;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        if (positionOffset >= 0f && positionOffset <= 1f)
        {
            cur = getRootView(position);
            next = getRootView(position +1);
            cur.setScaleBoth(BIG_SCALE
                    - DIFF_SCALE * positionOffset);
            next.setScaleBoth(SMALL_SCALE
                    + DIFF_SCALE * positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    private MyLinearLayout getRootView(int position)
    {
        return (MyLinearLayout)
                mFragmentManager.findFragmentByTag(this.getFragmentTag(position))
                        .getView().findViewById(R.id.root);
    }

    private String getFragmentTag(int position)
    {
        return "android:switcher:" + mViewPager.getId() + ":" + position;
    }
}