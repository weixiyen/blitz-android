package com.blitz.app.screens.splash;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Miguel Gaeta on 5/31/14.
 */
public class SplashScreenPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 3;

    public SplashScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SplashScreenSlide1Fragment();
            case 1:
                return new SplashScreenSlide2Fragment();
            case 2:
                return new SplashScreenSlide3Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
