package com.blitz.app.screens.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.simple_models.HeadToHeadDraft;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelGameLog;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenFragmentRecent extends BaseFragment implements ViewModelGameLog.ViewModelGameLogCallbacks {

    // region Member Variables
    // =============================================================================================

    // Total weeks in an NFL season.
    private static final int WEEKS_IN_SEASON = 17;

    @InjectView(R.id.main_recent_scrubber) ViewGroup mScrubber;
    @InjectView(R.id.main_recent_list)     ListView mRecentMatches;

    private ViewModelGameLog mViewModel; // lazy loaded

    // endregion

    // region Overwritten Methods
    // =============================================================================================


    /**
     * Setup the recent matches UI.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        setupScrubber();
    }

    @Override
    public void onDrafts(List<HeadToHeadDraft> matches, ViewModelGameLog.Summary summary, int week) {

        setActiveWeekIndicator(week);

        final MatchInfoAdapter adapter = new MatchInfoAdapter(matches, getActivity());

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

    // poor man's closure
    private static abstract class WeekSettingListener implements View.OnClickListener {
        final int mWeek;
        WeekSettingListener(int week) {
            mWeek = week;
        }
    }

    /**
     * Setup the week scrubber.
     */
    private void setupScrubber() {

        for (int i = 0; i < WEEKS_IN_SEASON; i++) {

            // Inflate the week view.
            LayoutInflater.from(getActivity())
                    .inflate(R.layout.main_screen_fragment_recent_week, mScrubber, true);
        }

        for (int i = 0; i < WEEKS_IN_SEASON; i++) {
            TextView weekPicker = (TextView) mScrubber.getChildAt(i);

            weekPicker.setOnClickListener(new WeekSettingListener(i + 1) {
                @Override
                public void onClick(View view) {

                    // Immediately update the page indicator to make
                    // the UI feel more responsive.
                    setActiveWeekIndicator(mWeek);

                    // Update the data, which is asynchronous
                    mViewModel.updateWeek(mWeek);
                }
            });
        }
    }

    private void setActiveWeekIndicator(int week) {

        for(int j=0; j < WEEKS_IN_SEASON; j++) {

            TextView indicator = (TextView) mScrubber.getChildAt(j);
            if(j + 1 == week) {
                indicator.setTextColor(getResources().getColor(R.color.active_blue));
            } else {
                indicator.setTextColor(getResources().getColor(R.color.inactive_control_color));
            }
        }

        ((TextView)getActivity().findViewById(R.id.main_recent_header)).setText("Week " + week);
    }


    @Override
    public ViewModel onFetchViewModel() {
        if(mViewModel == null) {
            mViewModel = new ViewModelGameLog(getActivity(), this);
        }
        return mViewModel;
    }

    // endregion
}