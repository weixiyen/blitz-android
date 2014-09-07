package com.blitz.app.object_models;

import com.blitz.app.utilities.logging.LogHelper;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Observer;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by Nate on 9/5/14. Copyright 2014 Blitz Studios
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

    // ReplaySubject with size=1 is similar to RACObservable. The current value will be
    // reflected in all current and future subscribers.
    private static final Subject<ObjectModelAccessQueue2, ObjectModelAccessQueue2> subject =
            ReplaySubject.createWithSize(1);

    public static final Observable<ObjectModelAccessQueue2> getObservable() {
        return subject.asObservable();
    }

    public static void sync(String deviceId, final Observer<Integer> playersAhead, final Observer<Integer> playersBehind, final Observer<Boolean> accessGranted) {
        // Make rest call and forward result into subject
        RestAPIClient.getAPI().access_queue_get(deviceId, new Callback<JsonObject>() {

            @Override
            public void success(JsonObject jsonObject, Response response) {
                JsonElement peopleAheadEl = jsonObject.get("people_ahead");
                JsonElement peopleBehindEl = jsonObject.get("people_behind");
                JsonElement accessGrantedEl = jsonObject.get("access_granted");

                if(peopleAheadEl != null) {
                    playersAhead.onNext(peopleAheadEl.getAsInt());
                }
                if(peopleBehindEl != null) {
                    playersBehind.onNext(peopleBehindEl.getAsInt());
                }
                if(accessGrantedEl != null) {
                    accessGranted.onNext(accessGrantedEl.getAsBoolean());
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                LogHelper.log(retrofitError.toString());
            }
        });
    }

    public static Observable<ObjectModelAccessQueue2> sync(String deviceId) {

        // Make rest call and forward result into subject
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

        return subject.asObservable();
    }

    // endregion
}