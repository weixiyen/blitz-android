package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.object_models.RestModelUser;
import com.blitz.app.utilities.reactive.Observer;

import java.util.List;

/**
 * View model for leaderboard page.
 * Created by Nate on 9/30/14.
 */
public class ViewModelLeaderboard extends ViewModel implements RestModelUser.CallbackUsers {


    private final Observer<List<RestModelUser>> mLeadersObserver;
    private final Activity mActivity;

    public ViewModelLeaderboard(Activity activity, Observer<List<RestModelUser>> observer) {
        super(activity, observer);

        mActivity = activity;
        mLeadersObserver = observer;
    }

    @Override
    public void initialize() {

        RestModelUser.getTopPlayersWithLimit(mActivity, 150, this);
    }


    @Override
    public void onSuccess(List<RestModelUser> users) {

        mLeadersObserver.onNext(users);
    }
}
