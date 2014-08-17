package com.blitz.app.utilities.carousel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.blitz.app.R;
import com.blitz.app.screens.main.MainScreenFragmentSettings;

/**
 * Created by mrkcsc on 8/17/14.
 */
public class MyPagerAdapter extends FragmentPagerAdapter implements
        ViewPager.OnPageChangeListener {
    private MyLinearLayout cur = null;
    private MyLinearLayout next = null;

    private MainScreenFragmentSettings context;

    private FragmentManager fm;

    private float scale;
    public MyPagerAdapter(MainScreenFragmentSettings context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }
    @Override
    public Fragment getItem(int position)
    {
// make the first pager bigger than others
        if (position == MainScreenFragmentSettings.FIRST_PAGE)
            scale = MainScreenFragmentSettings.BIG_SCALE;
        else
            scale = MainScreenFragmentSettings.SMALL_SCALE;
        position = position % MainScreenFragmentSettings.PAGES;

        return MyFragment.newInstance(context.getActivity(), position, scale);
    }

    @Override
    public int getCount()
    {
        return MainScreenFragmentSettings.PAGES * MainScreenFragmentSettings.LOOPS;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        if (positionOffset >= 0f && positionOffset <= 1f)
        {
            cur = getRootView(position);
            next = getRootView(position +1);
            cur.setScaleBoth(MainScreenFragmentSettings.BIG_SCALE
                    - MainScreenFragmentSettings.DIFF_SCALE * positionOffset);
            next.setScaleBoth(MainScreenFragmentSettings.SMALL_SCALE
                    + MainScreenFragmentSettings.DIFF_SCALE * positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}

    private MyLinearLayout getRootView(int position)
    {
        return (MyLinearLayout)
                fm.findFragmentByTag(this.getFragmentTag(position))
                        .getView().findViewById(R.id.root);
    }

    private String getFragmentTag(int position)
    {
        return "android:switcher:" + context.pager.getId() + ":" + position;
    }
}