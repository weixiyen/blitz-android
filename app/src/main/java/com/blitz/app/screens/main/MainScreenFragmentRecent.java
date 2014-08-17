package com.blitz.app.screens.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreenFragmentRecent extends BaseFragment {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Total weeks in an NFL season.
    private static final int WEEKS_IN_SEASON = 17;

    @InjectView(R.id.main_recent_scrubber) ViewGroup mScrubber;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Setup the recent matches UI.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        setupScrubber();
    }

    /**
     * Setup the week scrubber.
     */
    private void setupScrubber() {

        for (int i = 0; i < WEEKS_IN_SEASON; i++) {

            // Inflate the week view.
            LayoutInflater.from(getActivity())
                    .inflate(R.layout.main_screen_fragment_recent_week, mScrubber, true);

            // Set the current week value.
            ((TextView)mScrubber.getChildAt(i)).setText(String.format("%02d", i + 1));
        }
    }
}