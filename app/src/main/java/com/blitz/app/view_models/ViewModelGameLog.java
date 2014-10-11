package com.blitz.app.view_models;

import android.app.Activity;
import android.util.SparseArray;

import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelCallbacks;
import com.blitz.app.simple_models.HeadToHeadDraft;
import com.blitz.app.utilities.authentication.AuthHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A view model for game logs.
 *
 * Created by Nate on 9/7/14.
 */
public class ViewModelGameLog extends ViewModel {

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

    private final ViewModelGameLogCallbacks mCallbacks;
    private final SparseArray<List<HeadToHeadDraft>> mCache;

    private int mCurrentWeek;

    public ViewModelGameLog(Activity activity, ViewModelGameLogCallbacks callbacks) {
        super(activity, callbacks);
        mCallbacks = callbacks;
        mCache = new SparseArray<List<HeadToHeadDraft>>(17);
        mCurrentWeek = AuthHelper.instance().getPreferences().getCurrentWeek();
    }

    public void updateWeek(final int week) {

        // Populate the UI with existing cached data if we already have it
        if(mCache.get(week) != null) {
            List<HeadToHeadDraft> drafts = mCache.get(week);
            mCallbacks.onDrafts(drafts, new Summary(drafts));
            mCurrentWeek = week;
        }

        // Get fresh data from the server.
        RestModelDraft.fetchDraftsForUser(mActivity, AuthHelper.instance().getUserId(), week, 2014, null,
                new RestModelCallbacks<RestModelDraft>() {
                    @Override
                    public void onSuccess(List<RestModelDraft> drafts) {
                        List<HeadToHeadDraft> matches = new ArrayList<HeadToHeadDraft>(drafts.size());
                        for (RestModelDraft draft : drafts) {
                            matches.add(new HeadToHeadDraft(
                                    draft.getId(),
                                    draft.getTeamName(0),
                                    draft.getTeamRoster(0),
                                    draft.getTeamPoints(0),
                                    draft.getTeamRatingChange(0),
                                    draft.getTeamName(1),
                                    draft.getTeamRoster(1),
                                    draft.getTeamPoints(1),
                                    draft.getTeamRatingChange(1),
                                    draft.getYear(),
                                    draft.getWeek(),
                                    draft.getStatus()
                            ));
                        }
                        mCache.put(week, matches);
                        mCurrentWeek = week;
                        mCallbacks.onDrafts(matches, new Summary(matches));
                    }
                });

    }

    @Override
    public void initialize() {

        updateWeek(mCurrentWeek);
    }

    public interface ViewModelGameLogCallbacks extends ViewModel.ViewModelCallbacks {

        public void onDrafts(List<HeadToHeadDraft> drafts, Summary summary);
    }
}


