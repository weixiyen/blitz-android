package com.blitz.app.dialogs.rules;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Pager adapter for rules.
 *
 * Created by Nate on 10/1/14.
 */
public class DialogRulesAdapter extends FragmentStatePagerAdapter {

    // region Member Variables
    // =============================================================================================

    // Number of pages in the dialog.
    private static final int RULES_PAGES = 1;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Default adapter constructor.
     *
     * @param fm Fragment manager.
     */
    public DialogRulesAdapter(FragmentManager fm) {
        super(fm);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Each rules page is uniquely styled so use
     * a unique fragment for each page.
     *
     * @param position Page position.
     *
     * @return Instantiated fragment.
     */
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new DialogRulesPage0();
        }

        return null;
    }

    /**
     * Fixed number of rules pages.
     *
     * @return Read from constant.
     */
    @Override
    public int getCount() {

        return RULES_PAGES;
    }

    // endregion
}