package com.blitz.app.screens.main;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Pager adapter for rules.
 *
 * Created by Nate on 10/1/14.
 */
public class RulesPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> mContent;
    private final DialogFragment mDialog;

    public RulesPagerAdapter(FragmentManager fm, List<String> content, DialogFragment dialog) {
        super(fm);
        mContent = content;
        mDialog = dialog;
    }

    @Override
    public Fragment getItem(int position) {

        RulesFragment fragment = RulesFragment.newInstance(mDialog);
        fragment.setContent(mContent.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return mContent.size();
    }

    /**
     * Scale the page width so that the pages look more like cards. See:
     * https://github.com/thecodepath/android_guides/wiki/ViewPager-with-FragmentPagerAdapter
     */
    @Override
    public float getPageWidth(int _) {

        return 0.93f;
    }

}

