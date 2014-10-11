package com.blitz.app.dialogs.rules;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseDialogFragment;
import com.blitz.app.utilities.reflection.ReflectionHelper;

/**
 * DialogFragment for rules.
 *
 * Created by Nate on 10/9/14.
 */
public class DialogRules extends BaseDialogFragment {

    @Override
    protected void onViewCreated(View view) {

        ViewPager rulesPager = (ViewPager) view.findViewById(R.id.dialog_rules_pager);

        // Formatting to make the pages look like cards
        rulesPager.setClipToPadding(false);
        int pixelPadding = ReflectionHelper.densityPixelsToPixels
                (rulesPager.getContext(), 20);

        // Assign and clip the padding.
        rulesPager.setPadding(pixelPadding, 0, pixelPadding, 0);

        // Set up rules view pager.
        rulesPager.setAdapter(new DialogRulesAdapter(getChildFragmentManager()));
    }
}