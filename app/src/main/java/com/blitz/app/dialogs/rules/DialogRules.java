package com.blitz.app.dialogs.rules;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseDialogFragment;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import butterknife.InjectView;

/**
 * DialogFragment for rules.
 *
 * Created by Nate on 10/9/14.
 */
public class DialogRules extends BaseDialogFragment {

    // region Member Variables
    // ============================================================================================================

    // Rules dialog contains a view pager.
    @InjectView(R.id.dialog_rules_pager) ViewPager mRulesPager;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Create a carousel-like pager to
     * show the dialog rules cards.
     */
    @Override
    protected void onViewCreated(View view) {

        // Enable clipping to show side cards.
        mRulesPager.setClipToPadding(false);

        int pixelPadding = ReflectionHelper.densityPixelsToPixels
                (mRulesPager.getContext(), 20);

        // Assign and clip the padding.
        mRulesPager.setPadding(pixelPadding, 0, pixelPadding, 0);

        // Set up rules view pager.
        mRulesPager.setAdapter(new DialogRulesAdapter(getChildFragmentManager()));
    }

    // endregion
}