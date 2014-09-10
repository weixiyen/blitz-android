package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.object_models.ObjectModelDraft;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPI;
import com.blitz.app.utilities.rest.RestAPIClient;

import org.apache.http.auth.AUTH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nate on 9/7/14.
 */
public class ViewModelMatches extends ViewModel {

    private final ViewModelMatchesCallbacks mCallbacks;

    public ViewModelMatches(Activity activity, ViewModelMatchesCallbacks callbacks) {
        super(activity, callbacks);
        mCallbacks = callbacks;
    }

    public void updateWeek(int week) {

        // TODO: test test test
        final List<MatchInfo> list = new ArrayList<MatchInfo>();
        list.add(new MatchInfo("galfgarion", 9000.1f, "mericsson", 9000.2f, "IN PROGRESS"));
        list.add(new MatchInfo("galfgarion", 9000.3f, "mgaeta", 90.2f, "IN PROGRESS"));
        list.add(new MatchInfo("galfgarion", 900.1f, "supercalifragilisticexpialidocious", 9000.4f, "IN PROGRESS"));

        final List<MatchInfo> list2 = new ArrayList<MatchInfo>();
        list2.add(new MatchInfo("galfgarion", 90.1f, "jsekiguchi", 342.2f, "IN PROGRESS"));
        list2.add(new MatchInfo("galfgarion", 9000.3f, "crajan", 90.2f, "IN PROGRESS"));
        list2.add(new MatchInfo("galfgarion", 900.1f, "bsundaravaradan", 900.4f, "IN PROGRESS"));

        if(week % 2 == 0) {
            mCallbacks.onMatches(list);
        } else {
            mCallbacks.onMatches(list2);
        }
    }

    @Override
    public void initialize() {

        /*
        ObjectModelDraft.fetchDraftsForUser(mActivity, AuthHelper.instance().getUserId(), 1, 2014, null,
                new ObjectModelDraft.DraftsCallback() {
            @Override
            public void onSuccess(List<ObjectModelDraft> drafts) {
                mCallbacks.onMatches(drafts);
            }
        });
        */

        updateWeek(1);
    }

    public interface ViewModelMatchesCallbacks extends ViewModel.ViewModelCallbacks {
        public void onMatches(List<MatchInfo> matches);
    }
}


