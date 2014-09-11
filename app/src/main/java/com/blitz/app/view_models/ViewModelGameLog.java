package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.object_models.ObjectModelDraft;
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

    public ViewModelGameLog(Activity activity, ViewModelGameLogCallbacks callbacks) {
        super(activity, callbacks);
        mCallbacks = callbacks;
    }

    public void updateWeek(int week) {
        /*

        ObjectModelDraft.fetchDraftsForUser(mActivity, AuthHelper.instance().getUserId(), 1, 2014, null,
            new ObjectModelDraft.DraftsCallback() {
                @Override
                public void onSuccess(List<ObjectModelDraft> drafts) {
                    List<HeadToHeadDraft> matches = new ArrayList<HeadToHeadDraft>(drafts.size());
                    for(ObjectModelDraft draft: drafts) {
                        matches.add(new HeadToHeadDraft(
                                draft.getTeamName(0),
                                draft.getTeamPoints(0),
                                draft.getTeamName(1),
                                draft.getTeamPoints(1),
                                "IN PROGRESS" // TODO: derive the status from the data.
                        ));
                    }
                    mCallbacks.onDrafts(matches);
                }
         });
         */

        // TODO: test test test
        final List<HeadToHeadDraft> list = new ArrayList<HeadToHeadDraft>();
        list.add(new HeadToHeadDraft("galfgarion", 9000.1f, "mericsson", 9000.2f, "IN PROGRESS"));
        list.add(new HeadToHeadDraft("galfgarion", 9000.3f, "mgaeta", 90.2f, "IN PROGRESS"));
        list.add(new HeadToHeadDraft("galfgarion", 900.1f, "supercalifragilisticexpialidocious", 9000.4f, "IN PROGRESS"));

        final List<HeadToHeadDraft> list2 = new ArrayList<HeadToHeadDraft>();
        list2.add(new HeadToHeadDraft("galfgarion", 90.1f, "jsekiguchi", 342.2f, "IN PROGRESS"));
        list2.add(new HeadToHeadDraft("galfgarion", 9000.3f, "crajan", 90.2f, "IN PROGRESS"));
        list2.add(new HeadToHeadDraft("galfgarion", 900.1f, "bsundaravaradan", 900.4f, "IN PROGRESS"));

        if(week % 2 == 0) {
            mCallbacks.onDrafts(list);
        } else {
            mCallbacks.onDrafts(list2);
        }
    }

    @Override
    public void initialize() {

        updateWeek(1);
    }

    public interface ViewModelGameLogCallbacks extends ViewModel.ViewModelCallbacks {
        public void onDrafts(List<HeadToHeadDraft> drafts);
    }
}


