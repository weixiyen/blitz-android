package com.blitz.app.screens.main;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseDialogFragment;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DialogFragment for rules.
 *
 * Created by Nate on 10/9/14.
 */
public class DialogRules extends BaseDialogFragment {

    @Override
    protected void onViewCreated(View view) {

        int[] contentResources = new int[] {
                R.string.rules_heads_up_draft,
                R.string.rules_scoring_offense,
                R.string.rules_scoring_defense,
                R.string.rules_blitz_rating
        };

        String style = getString(R.string.rules_style);
        List<String> content = new ArrayList<String>(contentResources.length);
        for(int resourceId: contentResources) {
            content.add(style + getString(resourceId));
        }

        ViewPager rulesPager = (ViewPager) view.findViewById(R.id.dialog_rules_pager);

        // Formatting to make the pages look like cards
        rulesPager.setClipToPadding(false);
        int pixelPadding = ReflectionHelper.densityPixelsToPixels
                (rulesPager.getContext(), 20);

        // Assign and clip the padding.
        rulesPager.setPadding(pixelPadding, 0, pixelPadding, 0);

        // Set up rules view pager.
        rulesPager.setAdapter(new RulesPagerAdapter(getChildFragmentManager(), content, this));
    }
}