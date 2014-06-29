package com.blitz.app.models.objects;

import android.util.Log;

import com.blitz.app.models.rest.RestAPI;
import com.blitz.app.models.rest.RestAPIClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
@SuppressWarnings("unused")
public class ObjectModelPreferences {

    public void TestCall() {

        RestAPIClient.getAPI().preferences(new Callback<RestAPI.Preference>() {

            @Override
            public void success(RestAPI.Preference preference, Response response) {

                Log.e("TEST2", "success " + preference.current_week + " year: " + preference.current_year);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                Log.e("TEST2", "fail");
            }
        });
    }
}