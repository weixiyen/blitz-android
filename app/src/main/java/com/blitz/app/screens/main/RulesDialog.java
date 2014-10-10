package com.blitz.app.screens.main;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blitz.app.R;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DialogFragment for rules.
 *
 * Created by Nate on 10/9/14.
 */
public class RulesDialog extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_rules, container, false);

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

        ViewPager rulesPager = (ViewPager) v.findViewById(R.id.dialog_rules_pager);

        // Formatting to make the pages look like cards
       // rulesPager.setClipToPadding(false);
        int pixelPadding = ReflectionHelper.densityPixelsToPixels
                (rulesPager.getContext(), 10);

        // Assign and clip the padding.
        rulesPager.setPadding(pixelPadding, 0, pixelPadding, 0);

        // Set up rules view pager.
        rulesPager.setAdapter(new RulesPagerAdapter(getChildFragmentManager(), content, this));

        return v;
    }
}
