package com.blitz.app.screens.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.view_models.HeadToHeadDraft;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelMatches;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class MainScreenFragmentRecent extends BaseFragment implements ViewModelMatches.ViewModelMatchesCallbacks {

    // region Member Variables
    // =============================================================================================

    // Total weeks in an NFL season.
    private static final int WEEKS_IN_SEASON = 17;

    @InjectView(R.id.main_recent_scrubber) ViewGroup mScrubber;
    @InjectView(R.id.main_recent_list)     ListView mRecentMatches;

    private ViewModelMatches mViewModel; // lazy loaded

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

    public void onMatches(List<HeadToHeadDraft> matches) {
        final MatchInfoAdapter adapter = new MatchInfoAdapter(getActivity().getApplicationContext(),
                matches, getActivity());

        if(mRecentMatches != null) {
            mRecentMatches.setAdapter(adapter);
        }
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
                    for(int j=0; j < WEEKS_IN_SEASON; j++) {
                        ((TextView) mScrubber.getChildAt(j)).setTextColor(Color.rgb(251, 251, 251));
                    }
                    ((TextView) view).setTextColor(getResources().getColor(R.color.text_color_link));
                    ((TextView)getActivity().findViewById(R.id.main_recent_header)).setText("Week " + mWeek);
                    mViewModel.updateWeek(mWeek);
                }
            });
        }
    }


    @Override
    public ViewModel onFetchViewModel() {
        if(mViewModel == null) {
            mViewModel = new ViewModelMatches(getActivity(), this);
        }
        return mViewModel;
    }

    // endregion
}