package com.blitz.app.view_models;

import android.util.SparseArray;

import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelCallbacks;
import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelPreferences;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.logging.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A view model for game logs.
 *
 * Created by Nate on 9/7/14.
 */
public class ViewModelRecent extends ViewModel {

    // region Member Variables
    // =============================================================================================

    // View model callbacks.
    private final Callbacks mCallbacks;

    // Draft cache // TODO: Refactor HeadToHeadDraft
    private final SparseArray<List<HeadToHeadDraft>> mCache;

    // Current draft week and year.
    private static Integer mYearCurrent;
    private static Integer mWeekCurrent;

    // Current selected week.
    private Integer mWeekSelected;

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Setup the view model.
     *
     * @param activity Target activity.
     * @param callbacks Target callbacks.
     * @param weeks Total weeks.
     */
    public ViewModelRecent(BaseActivity activity, Callbacks callbacks, int weeks) {
        super(activity, callbacks);

        // Set the callbacks.
        mCallbacks = callbacks;

        // Create cache of some sort. TODO: Revise
        mCache = new SparseArray<List<HeadToHeadDraft>>(weeks);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * On start make sure we have the current week
     * and year, else just use the last selected week.
     */
    @Override
    public void initialize() {

        if (mWeekCurrent == null || mYearCurrent == null || mWeekSelected == null) {

            // Fetch the current draft week and year if needed.
            AuthHelper.instance().getPreferences(mActivity, false,
                    new RestModelCallback<RestModelPreferences>() {

                        @Override
                        public void onSuccess(RestModelPreferences object) {

                            // Get the current week and year.
                            mWeekCurrent = object.getCurrentWeek();
                            mYearCurrent = object.getCurrentYear();

                            updateWeek(mWeekCurrent);
                        }
                    });
        } else {

            // Used cache value.
            updateWeek(mWeekSelected);
        }
    }

    // endregion

    /**
     * Fetch current week in the season.
     *
     * @return Current week.
     */
    @SuppressWarnings("unused")
    public int getCurrentWeek() {

        return  mWeekCurrent;
    }

    public void updateWeek(final int week) {

        // Only update if the week changes.
        if (mWeekSelected != null && mWeekSelected == week) {

            return;
        }

        // Set the current week.
        mWeekSelected = week;

        // Populate the UI with existing cached data if we already have it
        if (mCache.get(week) != null) {
            List<HeadToHeadDraft> drafts = mCache.get(week);
            mCallbacks.onDrafts(drafts, new Summary(drafts), week);
        } else {

            // Get fresh data from the server.
            RestModelDraft.fetchDraftsForUser(mActivity, AuthHelper.instance().getUserId(),
                    week, mYearCurrent, null, new RestModelCallbacks<RestModelDraft>() {

                        @Override
                        public void onSuccess(List<RestModelDraft> drafts) {

                            // If the week has since changed,
                            // ignore these results.
                            if (mWeekSelected != week) {

                                return;
                            }

                            List<HeadToHeadDraft> matches = new ArrayList<HeadToHeadDraft>(drafts.size());
                            for (RestModelDraft draft : drafts) {
                                final int p1index;
                                final int p2index;
                                if(AuthHelper.instance().getUserId().equals(draft.getUsers().get(0))) {
                                    p1index = 0;
                                    p2index = 1;
                                } else {
                                    p1index = 1;
                                    p2index = 0;
                                }
                                matches.add(new HeadToHeadDraft(
                                        draft.getId(),
                                        draft.getTeamName(p1index),
                                        draft.getTeamRoster(p1index),
                                        draft.getTeamPoints(p1index),
                                        draft.getTeamRatingChange(p1index),
                                        draft.getTeamName(p2index),
                                        draft.getTeamRoster(p2index),
                                        draft.getTeamPoints(p2index),
                                        draft.getTeamRatingChange(p2index),
                                        draft.getYear(),
                                        draft.getWeek(),
                                        draft.getStatus()
                                ));
                            }

                            LogHelper.log("On drafts: " + week);

                            mCache.put(week, matches);
                            mCallbacks.onDrafts(matches, new Summary(matches), week);
                        }
                    });
        }
    }

    public final static class Summary {

        private final List<HeadToHeadDraft> mDrafts;
        private final int mRatingChange;
        private final int mEarnings;
        private final int mWins;
        private final int mLosses;


        Summary(List<HeadToHeadDraft> drafts) {
            mDrafts = drafts;

            int ratingChange = 0;
            int wins = 0;
            int losses = 0;
            for(HeadToHeadDraft draft: mDrafts) {

                ratingChange += draft.getPlayer1RatingChange();
                if(draft.getPlayer1Score() > draft.getPlayer2Score()) {
                    wins += 1;
                } else if(draft.getPlayer1Score() < draft.getPlayer2Score()) {
                    losses += 1;
                }
            }
            mRatingChange = ratingChange;
            mWins = wins;
            mLosses = losses;

            mEarnings = 0; // TODO calculate earnings

        }

        public int getWins() {
            return mWins;
        }

        public int getLosses() {
            return mLosses;
        }

        public int getEarningsCents() {
            return mEarnings;
        }

        public int getRatingChange() {
            return mRatingChange;
        }
    }

    public final static class HeadToHeadDraft {

        private final String mPlayer1Name;
        private final float  mPlayer1Score;
        private final int    mPlayer1RatingChange;
        private final List<String> mPlayer1Picks;

        private final String mPlayer2Name;
        private final float  mPlayer2Score;
        private final int    mPlayer2RatingChange;
        private final List<String> mPlayer2Picks;

        private final int mYear;
        private final int mWeek;
        private final String mStatus;
        private final String mId;

        public HeadToHeadDraft(String id, String player1Name, List<String> player1Picks, float player1Score, int player1RatingChange,
                               String player2Name, List<String> player2Picks, float player2Score, int player2RatingChange,
                               int year, int week, String status) {

            mId = id;

            mPlayer1Name  = player1Name;
            mPlayer1Score = player1Score;
            mPlayer1Picks = player1Picks;
            mPlayer1RatingChange = player1RatingChange;


            mPlayer2Name  = player2Name;
            mPlayer2Score = player2Score;
            mPlayer2Picks = player2Picks;
            mPlayer2RatingChange = player2RatingChange;

            mYear = year;
            mWeek = week;
            mStatus       = status;
        }

        public String getPlayer1Name() {
            return mPlayer1Name;
        }

        public float getPlayer1Score() {
            return mPlayer1Score;
        }

        public int getPlayer1RatingChange() {
            return mPlayer1RatingChange;
        }

        public int getPlayer2RatingChange() {
            return mPlayer2RatingChange;
        }

        public String getPlayer2Name() {
            return mPlayer2Name;
        }

        public float getPlayer2Score() {
            return mPlayer2Score;
        }

        public String getStatus() {
            return mStatus;
        }

        public List<String> getPlayer1Picks() {
            return mPlayer1Picks;
        }

        public List<String> getPlayer2Picks() {
            return mPlayer2Picks;
        }

        public int getYear() { return mYear; }

        public int getWeek() { return mWeek; }

        public String getId() {
            return mId;
        }
    }

    // region View Model Callbacks
    // =============================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onDrafts(List<HeadToHeadDraft> drafts, Summary summary, int week);
    }

    // endregion
}


