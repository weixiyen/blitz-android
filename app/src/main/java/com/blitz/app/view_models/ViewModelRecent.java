package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestResult;
import com.blitz.app.rest_models.RestResults;
import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelItem;
import com.blitz.app.rest_models.RestModelPreferences;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A view model for game logs.
 *
 * Created by Nate on 9/7/14.
 */
public class ViewModelRecent extends ViewModel {

    // region Member Variables
    // ============================================================================================================

    // View model callbacks.
    private final Callbacks mCallbacks;

    // Current draft week and year.
    private static Integer mYearCurrent;
    private static Integer mWeekCurrent;

    // Current selected week.
    private Integer mWeekSelected;

    // endregion

    // region Constructor
    // ============================================================================================================

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
    // ============================================================================================================

    /**
     * On start make sure we have the current week
     * and year, else just use the last selected week.
     */
    @Override
    public void initialize() {

        if (mWeekCurrent == null || mYearCurrent == null || mWeekSelected == null) {

            // Fetch the current draft week and year if needed.
            AuthHelper.instance().getPreferences(mActivity, false,
                    new RestResult<RestModelPreferences>() {

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
    // ============================================================================================================

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
                week, mYearCurrent, null, new RestResults<RestModelDraft>() {

                    @Override
                    public void onSuccess(List<RestModelDraft> object) {

                        processRecentDrafts(object, week);
                    }
                });
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Process draft fetch results.
     *
     * @param drafts Drafts list.
     * @param week Selected week.
     */
    private void processRecentDrafts(List<RestModelDraft> drafts, int week) {

        // A valid user id is needed to proceed.
        if (AuthHelper.instance().getUserId() == null) {

            return;
        }

        // Initialize list of matches.
        List<SummaryDraft> matches = new ArrayList<>();

        // Initialize list of associated avatar ids.
        List<String> userAvatarIds = new ArrayList<>();

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

            // Insert each avatar id into collection.
            for (RestModelUser user : draft.getUserInfo().values()) {

                userAvatarIds.add(user.getAvatarId());
            }

            // Insert a new draft summary.
            matches.add(new SummaryDraft(draft, p1Index, p2Index));
        }

        // Fetch additional avatar information.
        processUserItems(matches, userAvatarIds, week);
    }

    /**
     * Process avatars.
     *
     * @param matches Drafts.
     * @param userAvatarIds Associated avatar ids.
     * @param week Selected week.
     */
    private void processUserItems(final List<SummaryDraft> matches,
                                  final List<String> userAvatarIds, final int week) {

        RestModelItem.fetchItems(mActivity, userAvatarIds, userAvatarIds.size(),
                new RestResults<RestModelItem>() {

            @Override
            public void onSuccess(List<RestModelItem> object) {

                // If the week has since changed,
                // ignore these results.
                if (mWeekSelected != week) {

                    return;
                }

                Map<String, String> itemUrls = new HashMap<>();

                // For each unique item.
                for (RestModelItem item : object) {

                    // Convert into a map of id to image path.
                    if (!itemUrls.containsKey(item.getId())) {
                        itemUrls.put(item.getId(), item.getDefaultImgPath());
                    }
                }

                // Populate avatar urls.
                for (SummaryDraft draft : matches) {

                    draft.setAvatarUrls(itemUrls);
                }

                if (mCallbacks != null) {
                    mCallbacks.onDrafts(matches, new SummaryDrafts(matches), week);
                }
            }
        });
    }

    // endregion

    // region Inner Classes
    // ============================================================================================================

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

                ratingChange += draft.getRatingChange();

                if (draft.getP1Score() > draft.getP2Score()) {
                    wins += 1;
                } else if (draft.getP1Score() < draft.getP2Score()) {
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

        public String getEarningsCents() {

            String sign = "+";

            if (mEarnings < 0) {
                sign = "-";
            }

            String amount = String.format("$%.2f", Math.abs(mEarnings / 100f));

            return sign + amount;
        }

        public String getRatingChange() {

            String sign = "+";

            // Negative number already has a sign.
            if (mRatingChange < 0) {
                sign = "";
            }

            return sign + mRatingChange;
        }
    }

    /**
     * Summary of a draft.
     */
    public final static class SummaryDraft {

        // Draft object.
        private final RestModelDraft mDraft;

        // Index of associated players.
        private final int mP1Index, mP2Index;

        // Avatar urls.
        private String mP1AvatarUrl, mP2AvatarUrl;

        /**
         * Private constructor.
         *
         * @param draft Draft object.
         * @param p1Index Index of first player.
         * @param p2Index Index of second player.
         */
        private SummaryDraft(RestModelDraft draft, int p1Index, int p2Index) {

            mDraft = draft;

            mP1Index = p1Index;
            mP2Index = p2Index;
        }

        /**
         * Set the avatar urls using a map of avatar id,
         * mapped to associated url.
         *
         * @param itemUrls Item urls map.
         */
        private void setAvatarUrls(Map<String, String> itemUrls) {

            String p1UserId = mDraft.getUsers().get(mP1Index);
            String p2UserId = mDraft.getUsers().get(mP2Index);

            RestModelUser p1User = mDraft.getUserInfo().get(p1UserId);
            RestModelUser p2User = mDraft.getUserInfo().get(p2UserId);

            mP1AvatarUrl = itemUrls.get(p1User.getAvatarId());
            mP2AvatarUrl = itemUrls.get(p2User.getAvatarId());
        }

        public String getP1AvatarUrl() {

            return mP1AvatarUrl;
        }

        public String getP2AvatarUrl() {

            return mP2AvatarUrl;
        }

        public String getP1Name() {

            return mDraft.getTeamName(mP1Index);
        }

        public String getP2Name() {

            return mDraft.getTeamName(mP2Index);
        }

        public float getP1Score() {

            return mDraft.getTeamPoints(mP1Index);
        }

        public float getP2Score() {

            return mDraft.getTeamPoints(mP2Index);
        }

        public int getRatingChange() {

            return mDraft.getTeamRatingChange(mP1Index);
        }

        public String getStatus() {

            return mDraft.getGameStatus().replace('_', ' ');
        }

        public int getWeek() {

            return mDraft.getWeek();
        }

        public String getId() {

            return mDraft.getId();
        }
    }

    // endregion

    // region View Model Callbacks
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onDrafts(List<SummaryDraft> drafts, SummaryDrafts summaryDrafts, int week);
    }

    // endregion
}