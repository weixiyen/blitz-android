package com.blitz.app.screens.recent;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.main.MainScreen;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelRecent;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/14/14. Copyright 2014 Blitz Studios
 */
public class RecentScreen extends BaseFragment implements ViewModelRecent.Callbacks,
        RecentScreenScrubber.Callbacks {

    // region Member Variables
    // =============================================================================================

    // Total weeks in an NFL season.
    private static final int WEEKS_IN_SEASON = 17;

    @InjectView(R.id.recent_header)             TextView mRecentHeader;
    @InjectView(R.id.recent_scrubber_week)      TextView mRecentScrubberWeek;
    @InjectView(R.id.recent_no_games)           TextView mRecentNoGames;
    @InjectView(R.id.recent_drafts_list)        ListView mRecentMatches;
    @InjectView(R.id.recent_week_wins)          TextView mRecentWeekWins;
    @InjectView(R.id.recent_week_losses)        TextView mRecentWeekLosses;
    @InjectView(R.id.recent_week_earnings)      TextView mRecentWeekEarnings;
    @InjectView(R.id.recent_week_rating_change) TextView mRecentWeekRatingChange;

    // Week selector.
    @InjectView(R.id.recent_scrubber) RecentScreenScrubber mRecentScrubber;

    private ViewModelRecent mViewModel;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup the recent matches UI.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        // Provide the view pager.
        mRecentScrubber.setViewPager(((MainScreen) getActivity()).getViewPager());

        // Weeks in season.
        mRecentScrubber.setSize(WEEKS_IN_SEASON);

        // Week display text view.
        mRecentScrubber.setScrubberTextView(mRecentScrubberWeek);

        // Callbacks.
        mRecentScrubber.setCallbacks(this);
    }

    /**
     * Fetch recent screen view model.
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelRecent(getBaseActivity(), this, WEEKS_IN_SEASON);
        }

        return mViewModel;
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Format rating change.
     */
    private static String formatRatingChange (int change) {

        String sign = "+";

        // Negative number already has a sign.
        if (change < 0) {
            sign = "";
        }

        return sign + change;
    }

    /**
     * Format earnings.
     */
    private static String formatEarnings (int cents) {
        String sign = "+";

        if (cents < 0) {
            sign = "-";
        }

        String amount = String.format("$%.2f", Math.abs(cents / 100f));

        return sign + amount;
    }

    /**
     * Setup the UI for when the user
     * has no games this week.
     *
     * @param week Current week.
     */
    private void setupGamesListEmpty(final int week) {

        // Either fade out the matches, or existing UI.
        View targetFrom = mRecentMatches.getVisibility() == View.GONE
                ? mRecentNoGames : mRecentMatches;

        AnimHelperFade.setVisibility(targetFrom, View.GONE, new Runnable() {

            @Override
            public void run() {

                if (week <= mViewModel.getCurrentWeek()) {

                    // Serious message.
                    mRecentNoGames.setText("You did not play any games during week " + week + "!");

                } else {

                    // Funny message.
                    mRecentNoGames.setText("Draft week " + week + " has not yet occurred!"
                            + "  No time travel allowed.");
                }

                // Show the no games UI.
                AnimHelperFade.setVisibility(mRecentNoGames, View.VISIBLE);
            }
        });
    }

    /**
     * Setup UI when user has games.
     *
     * @param drafts List of matches.
     * @param summary Week summary.
     */
    private void setupGamesList(final List<ViewModelRecent.HeadToHeadDraft> drafts,
                                final ViewModelRecent.Summary summary) {

        // Either fade out the matches, or existing UI.
        View targetFrom = mRecentNoGames.getVisibility() == View.GONE
                ? mRecentMatches : mRecentNoGames;

        AnimHelperFade.setVisibility(targetFrom, View.GONE, new Runnable() {

            @Override
            public void run() {

                if (mRecentMatches != null) {
                    mRecentMatches.setAdapter(new RecentScreenMatchAdapter(drafts, getActivity()));
                }

                // Set summary info.
                mRecentWeekWins
                        .setText(String.valueOf(summary.getWins()));
                mRecentWeekLosses
                        .setText(String.valueOf(summary.getLosses()));
                mRecentWeekEarnings
                        .setText(formatEarnings(summary.getEarningsCents()));
                mRecentWeekRatingChange
                        .setText(formatRatingChange(summary.getRatingChange()));

                // Show the drafts list.
                AnimHelperFade.setVisibility(mRecentMatches, View.VISIBLE);
            }
        });
    }

    // endregion

    // region View Model & Scrubber Callbacks
    // =============================================================================================

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
            mViewModel.updateWeek(position);
        }
    }

    /**
     * When drafts are received, process them and
     * setup the corresponding UI.
     *
     * @param drafts List of matches.
     * @param summary Week summary.
     * @param week Current week.
     */
    @Override
    public void onDrafts(List<ViewModelRecent.HeadToHeadDraft> drafts, ViewModelRecent.Summary summary, int week) {

        if (mRecentHeader != null) {
            mRecentHeader.setText("Week " + (week + 1));
        }

        if (mRecentScrubber != null) {
            mRecentScrubber.setScrubberItemSelected(week);
        }

        if (drafts.size() > 0) {

            // We have drafts.
            setupGamesList(drafts, summary);

        } else {

            // No games played.
            setupGamesListEmpty(week + 1);
        }
    }

    // endregion
}