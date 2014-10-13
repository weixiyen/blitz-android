package com.blitz.app.view_models;

import android.util.SparseArray;

import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelCallbacks;
import com.blitz.app.rest_models.RestModelDraft;
import com.blitz.app.rest_models.RestModelPreferences;
import com.blitz.app.simple_models.HeadToHeadDraft;
import com.blitz.app.utilities.android.BaseActivity;
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

    public ViewModelGameLog(BaseActivity activity, ViewModelGameLogCallbacks callbacks) {
        super(activity, callbacks);
        mCallbacks = callbacks;
        mCache = new SparseArray<List<HeadToHeadDraft>>(17);
    }

    public void updateWeek(final int week) {

        // Populate the UI with existing cached data if we already have it
        if(mCache.get(week) != null) {
            List<HeadToHeadDraft> drafts = mCache.get(week);
            mCallbacks.onDrafts(drafts, new Summary(drafts), week);
        }

        // Get fresh data from the server.
        RestModelDraft.fetchDraftsForUser(mActivity, AuthHelper.instance().getUserId(), week, 2014, 1000,
                new RestModelCallbacks<RestModelDraft>() {
                    @Override
                    public void onSuccess(List<RestModelDraft> drafts) {
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
                        mCache.put(week, matches);
                        mCallbacks.onDrafts(matches, new Summary(matches), week);
                    }
                });

    }

    @Override
    public void initialize() {

        AuthHelper.instance().getPreferences(mActivity, false,
                new RestModelCallback<RestModelPreferences>() {

                    @Override
                    public void onSuccess(RestModelPreferences object) {

                        updateWeek(object.getCurrentWeek());
                    }
                });
    }

    public interface ViewModelGameLogCallbacks extends ViewModel.ViewModelCallbacks {

        public void onDrafts(List<HeadToHeadDraft> drafts, Summary summary, int week);
    }
}


