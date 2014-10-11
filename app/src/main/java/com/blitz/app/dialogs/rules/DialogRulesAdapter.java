package com.blitz.app.dialogs.rules;

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
public class DialogRulesAdapter extends FragmentStatePagerAdapter {

    private final List<String> mContent;
    private final DialogFragment mDialog;

    public DialogRulesAdapter(FragmentManager fm, List<String> content, DialogFragment dialog) {
        super(fm);
        mContent = content;
        mDialog = dialog;
    }

    @Override
    public Fragment getItem(int position) {

        DialogRulesAdapterFragment fragment = DialogRulesAdapterFragment.newInstance();
        fragment.setContent(mContent.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return mContent.size();
    }
}