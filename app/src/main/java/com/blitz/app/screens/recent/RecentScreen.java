package com.blitz.app.screens.recent;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.simple_models.HeadToHeadDraft;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelRecent;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class RecentScreen extends BaseFragment implements ViewModelRecent.Callbacks {

    // region Member Variables
    // =============================================================================================

    // Total weeks in an NFL season.
    private static final int WEEKS_IN_SEASON = 17;

    @InjectView(R.id.main_recent_header) TextView mRecentHeader;
    @InjectView(R.id.main_recent_scrubber) RecentScreenScrubber mScrubber;
    @InjectView(R.id.main_recent_list)     ListView mRecentMatches;

    private ViewModelRecent mViewModel; // lazy loaded

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup the recent matches UI.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        mScrubber.setViewPager(((MainScreen)getActivity()).getViewPager());
        mScrubber.setSize(WEEKS_IN_SEASON);
    }

    @Override
    public ViewModel onFetchViewModel() {
        if(mViewModel == null) {
            mViewModel = new ViewModelRecent(getBaseActivity(), this);
        }
        return mViewModel;
    }

    @Override
    public void onDrafts(List<HeadToHeadDraft> matches, ViewModelRecent.Summary summary, int week) {

        if (mRecentHeader != null) {
            mRecentHeader.setText("Week " + week);
        }

        if (mScrubber != null) {
            mScrubber.setScrubberItemSelected(week);
        }

        final RecentScreenMatchAdapter adapter = new RecentScreenMatchAdapter(matches, getActivity());

        if(mRecentMatches != null) {
            mRecentMatches.setAdapter(adapter);
        }

        ((TextView)getActivity().findViewById(R.id.wins)).setText(String.valueOf(summary.getWins()));
        ((TextView)getActivity().findViewById(R.id.losses)).setText(String.valueOf(summary.getLosses()));
        ((TextView)getActivity().findViewById(R.id.earnings)).setText(formatEarnings(summary.getEarningsCents()));
        ((TextView)getActivity().findViewById(R.id.rating_change)).setText(formatRatingChange(summary.getRatingChange()));
    }

    private static String formatRatingChange(int change) {
        String sign = "+";
        if(change < 0) { // negative number already has a sign
            sign = "";
        }
        return sign + change;
    }

    private static String formatEarnings(int cents) {
        String sign = "+";
        if(cents < 0) {
            sign = "-";
        }
        String amount = String.format("$%.2f", Math.abs(cents / 100f));
        return sign + amount;
    }

    // endregion
}