package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.object_models.ObjectModelAccessQueue;
import com.blitz.app.object_models.ObjectModelAccessQueue2;
import com.blitz.app.utilities.gcm.GcmRegistrationHelper;

import rx.Observer;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by mrkcsc on 8/9/14. Copyright 2014 Blitz Studios
 */
public class ViewModelAccessQueue extends ViewModel {

    // region Member Variables
    // =============================================================================================
    private Subject<Integer, Integer> mPlayersAhead = ReplaySubject.createWithSize(1);
    private Subject<Integer, Integer> mPlayersBehind = ReplaySubject.createWithSize(1);
    private Subject<Boolean, Boolean> mAccessGranted = ReplaySubject.createWithSize(1);

    // endregion

    // region Overwritten Methods
    // =============================================================================================
    /**
     * Initialize and sync the access queue numbers.
     *
     * @param activity Target activity.
     * @param callbacks Callbacks.
     */
    @Override
    public void initialize(Activity activity, ViewModelCallbacks callbacks) {
        super.initialize(activity, callbacks);

        // Initialize access queue model (Observable flavor).
        ObjectModelAccessQueue2.sync(GcmRegistrationHelper.getDeviceId(mActivity), mPlayersAhead, mPlayersBehind, mAccessGranted);
    }

    // endregion

    // region Public methods
    /**
     * Subscribe observers
     */
    public void subscribe(Observer<Integer> playersAhead, Observer<Integer> playersBehind, Observer<Boolean> accessGranted) {

        mPlayersAhead.subscribe(playersAhead);
        mPlayersBehind.subscribe(playersBehind);
        mAccessGranted.subscribe(accessGranted);
    }

    /**
     * Sync state changes from server
     */
    public void sync() {
        ObjectModelAccessQueue2.sync(GcmRegistrationHelper.getDeviceId(mActivity), mPlayersAhead, mPlayersBehind, mAccessGranted);
    }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface ViewModelAccessQueueCallbacks extends ViewModelCallbacks {

        public void onPeopleAhead(int peopleAhead);
        public void onPeopleBehind(int peopleBehind);
        public void onAccessGranted(boolean accessGranted);
    }

    // endregion
}