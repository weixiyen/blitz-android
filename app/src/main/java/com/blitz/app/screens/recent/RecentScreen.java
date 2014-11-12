package com.blitz.app.screens.recent;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.scrubber.BlitzScrubber;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelRecent;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class RecentScreen extends BaseFragment implements ViewModelRecent.Callbacks,
        BlitzScrubber.Callbacks {

    // region Member Variables
    // ============================================================================================================

    // Total weeks in an NFL season.
    private static final int WEEKS_IN_SEASON = 17;

    @InjectView(R.id.recent_header)             TextView mRecentHeader;
    @InjectView(R.id.recent_no_games)           TextView mRecentNoGames;
    @InjectView(R.id.recent_drafts_list)        ListView mRecentMatches;
    @InjectView(R.id.recent_week_wins)          TextView mRecentWeekWins;
    @InjectView(R.id.recent_week_losses)        TextView mRecentWeekLosses;
    @InjectView(R.id.recent_week_earnings)      TextView mRecentWeekEarnings;
    @InjectView(R.id.recent_week_rating_change) TextView mRecentWeekRatingChange;

    // Week selector.
    @InjectView(R.id.blitz_scrubber) BlitzScrubber mBlitzScrubber;
    @InjectView(R.id.blitz_scrubber_selected) TextView mBlitzScrubberSelected;

    private ViewModelRecent mViewModel;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Setup the recent matches UI.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        // Provide the view pager.
        mBlitzScrubber.setViewPager(((MainScreen) getActivity()).getViewPager());

        // Weeks in season.
        mBlitzScrubber.setSize(WEEKS_IN_SEASON);

        // Week display text view.
        mBlitzScrubber.setScrubberTextView(mBlitzScrubberSelected, null);

        // Callbacks.
        mBlitzScrubber.setCallbacks(this);
    }

    /**
     * Fetch recent screen view model.
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelRecent(getBaseActivity(), this);
        }

        return mViewModel;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Setup UI when user has games. Will also show some
     * basic UI for when the user does not have any
     * games this week.
     *
     * @param drafts List of matches.
     */
    private void setupGamesList(final List<ViewModelRecent.SummaryDraft> drafts, int week) {

        if (mRecentMatches != null) {
            mRecentMatches.setAdapter(new RecentScreenMatchAdapter(drafts, getActivity()));
        }

        if (drafts.size() > 0) {

            // No need to show the no games UI.
            mRecentNoGames.setVisibility(View.GONE);

        } else {

            if (week <= mViewModel.getCurrentWeek()) {

                // Serious message.
                mRecentNoGames.setText("You did not play any games during week " + week + "!");

            } else {

                // Funny message.
                mRecentNoGames.setText("Draft week " + week + " has not yet occurred!"
                        + "  No time travel allowed.");
            }

            // Show the no games UI.
            mRecentNoGames.setVisibility(View.VISIBLE);
        }
    }

    // endregion

    // region View Model & Scrubber Callbacks
    // ============================================================================================================

    /**
     * When the scrubber item changes, the position
     * is the current week selected. Pass it
     * into the view model.
     *
     * @param position Week selected.
     */
    @Override
    public void onScrubberItemSelected(int position) {

        // Update the week.
        if (mViewModel != null) {
            mViewModel.updateWeek(position + 1);
        }
    }

    /**
     * When drafts are received, process them and
     * setup the corresponding UI.
     *
     * @param drafts List of matches.
     * @param summaryDrafts Week summary.
     * @param week Current week.
     */
    @Override
    public void onDrafts(List<ViewModelRecent.SummaryDraft> drafts, ViewModelRecent.SummaryDrafts summaryDrafts, int week) {

        if (mRecentHeader != null) {
            mRecentHeader.setText("Week " + week);
        }

        if (mBlitzScrubber != null) {
            mBlitzScrubber.setScrubberItemSelected(week - 1);
        }

        // Set summary info.
        mRecentWeekWins
                .setText(String.valueOf(summaryDrafts.getWins()));
        mRecentWeekLosses
                .setText(String.valueOf(summaryDrafts.getLosses()));
        mRecentWeekEarnings
                .setText(summaryDrafts.getEarningsCents());
        mRecentWeekRatingChange
                .setText(summaryDrafts.getRatingChange());

        // Update drafts adapter.
        setupGamesList(drafts, week);
    }

    // endregion
}