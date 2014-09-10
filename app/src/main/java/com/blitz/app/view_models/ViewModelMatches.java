package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.object_models.ObjectModelDraft;
import com.blitz.app.utilities.authentication.AuthHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A view model for draft logs.
 *
 * Created by Nate on 9/7/14.
 */
public class ViewModelMatches extends ViewModel {

    private final ViewModelMatchesCallbacks mCallbacks;

    public ViewModelMatches(Activity activity, ViewModelMatchesCallbacks callbacks) {
        super(activity, callbacks);
        mCallbacks = callbacks;
    }

    public void updateWeek(int week) {

        ObjectModelDraft.fetchDraftsForUser(mActivity, AuthHelper.instance().getUserId(), 1, 2014, null,
            new ObjectModelDraft.DraftsCallback() {
                @Override
                public void onSuccess(List<ObjectModelDraft> drafts) {
                    List<MatchInfo> matches = new ArrayList<MatchInfo>(drafts.size());
                    for(ObjectModelDraft draft: drafts) {
                        matches.add(new MatchInfo(
                                draft.getTeamName(0),
                                draft.getTeamPoints(0),
                                draft.getTeamName(1),
                                draft.getTeamPoints(1),
                                "IN PROGRESS" // TODO: derive the status from the data.
                        ));
                    }
                    mCallbacks.onMatches(matches);
                }
         });
    }

    @Override
    public void initialize() {

        updateWeek(1);
    }

    public interface ViewModelMatchesCallbacks extends ViewModel.ViewModelCallbacks {
        public void onMatches(List<MatchInfo> matches);
    }
}


