package com.blitz.app.models.rest;

import android.util.Log;

import com.blitz.app.models.api.BlitzAPI;
import com.blitz.app.models.api.BlitzAPIClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
public class BlitzAPIPreferences extends BlitzAPIClient {

    public void TestCall() {

        getAPI().preferences(new Callback<BlitzAPI.Preference>() {

            @Override
            public void success(BlitzAPI.Preference preference, Response response) {

                Log.e("TEST2", "success " + preference.current_week + " year: " + preference.current_year);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                Log.e("TEST2", "fail");
            }
        });
    }
}