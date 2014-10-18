package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelCallbacks;
import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelPreferences;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;

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
     */
    public ViewModelRecent(BaseActivity activity, Callbacks callbacks) {
        super(activity, callbacks);

        // Set the callbacks.
        mCallbacks = callbacks;
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

    // region Public Methods
    // =============================================================================================

    /**
     * Fetch current week in the season.
     *
     * @return Current week.
     */
    @SuppressWarnings("unused")
    public int getCurrentWeek() {

        return  mWeekCurrent;
    }

    /**
     * Update the view model for the
     * specified week.
     *
     * @param week Specified week.
     */
    @SuppressWarnings("unused")
    public void updateWeek(final int week) {

        // Only update if the week changes.
        if (mWeekSelected != null && mWeekSelected == week) {

            return;
        }

        // Set the current week.
        mWeekSelected = week;

        RestModelDraft.fetchDraftsForUser(mActivity, AuthHelper.instance().getUserId(),
                week, mYearCurrent, null, new RestModelCallbacks<RestModelDraft>() {

                    @Override
                    public void onSuccess(List<RestModelDraft> object) {

                        processRecentDrafts(object, week);
                    }
                });
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Process draft fetch results.
     *
     * @param drafts Drafts list.
     * @param week Selected week.
     */
    private void processRecentDrafts(List<RestModelDraft> drafts, int week) {

        // If the week has since changed,
        // ignore these results.
        if (mWeekSelected != week) {

            return;
        }

        List<SummaryDraft> matches = new ArrayList<SummaryDraft>();

        for (RestModelDraft draft : drafts) {

            final int p1Index;
            final int p2Index;

            if (AuthHelper.instance().getUserId()
                    .equals(draft.getUsers().get(0))) {

                p1Index = 0;
                p2Index = 1;
            } else {
                p1Index = 1;
                p2Index = 0;
            }

            matches.add(new SummaryDraft(
                    draft.getId(),
                    draft.getTeamName(p1Index),
                    draft.getTeamPoints(p1Index),
                    draft.getTeamRatingChange(p1Index),
                    draft.getTeamName(p2Index),
                    draft.getTeamPoints(p2Index),
                    draft.getWeek(),
                    draft.getStatus()
            ));
        }

        if (mCallbacks != null) {
            mCallbacks.onDrafts(matches, new SummaryDrafts(matches), week);
        }
    }

    // endregion

    // region Inner Classes
    // =============================================================================================

    /**
     * Summary of drafts.
     */
    public final static class SummaryDrafts {

        private final List<SummaryDraft> mDrafts;

        private final int mRatingChange;
        private final int mEarnings;
        private final int mWins;
        private final int mLosses;

        private SummaryDrafts(List<SummaryDraft> drafts) {

            mDrafts = drafts;

            int ratingChange = 0;
            int wins = 0;
            int losses = 0;

            for (SummaryDraft draft: mDrafts) {

                ratingChange += draft.getPlayer1RatingChange();

                if (draft.getPlayer1Score() > draft.getPlayer2Score()) {
                    wins += 1;
                } else if (draft.getPlayer1Score() < draft.getPlayer2Score()) {
                    losses += 1;
                }
            }

            mRatingChange = ratingChange;
            mWins = wins;
            mLosses = losses;

            mEarnings = 0;
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

    /**
     * Summary of a draft.
     */
    public final static class SummaryDraft {

        private final String mPlayer1Name;
        private final float  mPlayer1Score;
        private final int    mPlayer1RatingChange;

        private final String mPlayer2Name;
        private final float  mPlayer2Score;

        private final int mWeek;
        private final String mStatus;
        private final String mId;

        private SummaryDraft(String draftId,
                             String p1UserName, float p1Score, int p1RatingChange,
                             String p2UserName, float p2Score, int week, String status) {

            mId = draftId;

            mPlayer1Name  = p1UserName;
            mPlayer1Score = p1Score;
            mPlayer1RatingChange = p1RatingChange;


            mPlayer2Name  = p2UserName;
            mPlayer2Score = p2Score;

            mWeek = week;
            mStatus = status;
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

        public String getPlayer2Name() {

            return mPlayer2Name;
        }

        public float getPlayer2Score() {

            return mPlayer2Score;
        }

        public String getStatus() {

            return mStatus;
        }

        public int getWeek() {

            return mWeek;
        }

        public String getId() {

            return mId;
        }
    }

    // endregion

    // region View Model Callbacks
    // =============================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onDrafts(List<SummaryDraft> drafts, SummaryDrafts summaryDrafts, int week);
    }

    // endregion
}