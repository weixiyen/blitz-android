package com.blitz.app.object_models;

import com.blitz.app.utilities.logging.LogHelper;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by mrkcsc on 8/9/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelAccessQueue2 {

    // region Member Variables
    // =============================================================================================

    private final int mPeopleAhead;
    private final int mPeopleBehind;

    private final boolean mAccessGranted;

    public ObjectModelAccessQueue2(int peopleAhead, int peopleBehind, boolean accessGranted) {
        mPeopleAhead = peopleAhead;
        mPeopleBehind = peopleBehind;
        mAccessGranted = accessGranted;
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * People ahead in queue.
     *
     * @return People ahead.
     */
    public int getPeopleAhead() {
        return mPeopleAhead;
    }

    /**
     * People behind in queue.
     *
     * @return People behind.
     */
    public int getPeopleBehind() {
        return mPeopleBehind;
    }

    /**
     * Get access granted status.
     *
     * @return Is access granted.
     */
    public boolean getAccessGranted() {
        return mAccessGranted;
    }

    public static Observable<ObjectModelAccessQueue2> getObservable(String deviceId) {

        final Subject<ObjectModelAccessQueue2, ObjectModelAccessQueue2> subject = ReplaySubject.createWithSize(1);

        // Make rest call for code.
        RestAPIClient.getAPI().access_queue_get(deviceId, new Callback<JsonObject>() {


            @Override
            public void success(JsonObject jsonObject, Response response) {
                subject.onNext(new ObjectModelAccessQueue2(
                        jsonObject.get("people_ahead").getAsInt(),
                        jsonObject.get("people_behind").getAsInt(),
                        jsonObject.get("access_granted").getAsBoolean()));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                LogHelper.log(retrofitError.toString());
            }
        });

        return subject;
    }



    // endregion
}