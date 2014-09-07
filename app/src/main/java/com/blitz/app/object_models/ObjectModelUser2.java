package com.blitz.app.object_models;

import android.app.Activity;
import android.widget.EditText;

import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPI;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.blitz.app.utilities.rest.RestAPIObject;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Immutable, Observable flavor of User object model.
 *
 * Created by Nate on 7/9/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelUser2 {

    // region Member Variables
    // =============================================================================================

    private final String mUsername;
    private final int mRating;
    private final int mWins;
    private final int mLosses;
    private final int mCash;

    // endregion

    public ObjectModelUser2(String userName, int rating, int wins, int losses, int cash) {
        mUsername = userName;
        mRating = rating;
        mWins = wins;
        mLosses = losses;
        mCash = cash;
    }

    public String getUsername() {
        return mUsername;
    }

    public int getRating() {
        return mRating;
    }

    public int getWins() {
        return mWins;
    }

    public int getLosses() {
        return mLosses;
    }

    public int getCash() {
        return mCash;
    }

    private static final Subject<ObjectModelUser2, ObjectModelUser2> subject = ReplaySubject.createWithSize(1);

    public static synchronized Observable<ObjectModelUser2> sync() {

        // Fetch user id.
        String userId = AuthHelper.getUserId();


        RestAPIClient.getAPI().user_get(userId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {

                if (jsonObject != null) {

                    // Fetch user object.
                    JsonObject result = jsonObject.getAsJsonObject("result");
                    if (result != null) {

                        subject.onNext(new ObjectModelUser2(
                                result.get("username").getAsString(),
                                result.get("rating").getAsInt(),
                                result.get("wins").getAsInt(),
                                result.get("losses").getAsInt(),
                                result.get("cash").getAsInt()
                        ));
                    }
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                subject.onError(retrofitError);
            }
        });

        return subject.asObservable();
    }


}