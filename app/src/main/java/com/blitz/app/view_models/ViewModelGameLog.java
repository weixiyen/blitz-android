package com.blitz.app.view_models;

import android.app.Activity;
import android.util.SparseArray;

import com.blitz.app.object_models.ObjectModelDraft;
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

    private final ViewModelGameLogCallbacks mCallbacks;
    private final SparseArray<List<HeadToHeadDraft>> mCache;

    private int mCurrentWeek;

    public ViewModelGameLog(Activity activity, ViewModelGameLogCallbacks callbacks) {
        super(activity, callbacks);
        mCallbacks = callbacks;
        mCache = new SparseArray<List<HeadToHeadDraft>>(17);
        mCurrentWeek = 1;
    }

    public void updateWeek(final int week) {

        // Populate the UI with existing cached data if we already have it
        if(mCache.get(week) != null) {
            mCallbacks.onDrafts(mCache.get(week));
        }

        // Get fresh data from the server.
        ObjectModelDraft.fetchDraftsForUser(mActivity, AuthHelper.instance().getUserId(), week, 2014, null,
            new ObjectModelDraft.DraftsCallback() {
                @Override
                public void onSuccess(List<ObjectModelDraft> drafts) {
                    List<HeadToHeadDraft> matches = new ArrayList<HeadToHeadDraft>(drafts.size());
                    for(ObjectModelDraft draft: drafts) {
                        matches.add(new HeadToHeadDraft(
                                draft.getTeamName(0),
                                draft.getTeamRoster(0),
                                draft.getTeamPoints(0),
                                draft.getTeamName(1),
                                draft.getTeamRoster(1),
                                draft.getTeamPoints(1),
                                "IN PROGRESS" // TODO: derive the status from the data.
                        ));
                    }
                    mCache.put(week, matches);
                    mCallbacks.onDrafts(matches);
                }
         });

        mCurrentWeek = week;
    }

    @Override
    public void initialize() {

        updateWeek(mCurrentWeek);
    }

    public interface ViewModelGameLogCallbacks extends ViewModel.ViewModelCallbacks {
        public void onDrafts(List<HeadToHeadDraft> drafts);
    }
}


