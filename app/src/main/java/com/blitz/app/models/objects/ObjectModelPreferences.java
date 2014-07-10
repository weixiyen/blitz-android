package com.blitz.app.models.objects;

import android.util.Log;

import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest_objects.JsonObjectPreference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
@SuppressWarnings("unused")
public class ObjectModelPreferences extends ObjectModel {

    public void TestCall() {

        RestAPIClient.getAPI().preferences(new Callback<JsonObjectPreference>() {

            @Override
            public void success(JsonObjectPreference preference, Response response) {

                Log.e("TEST2", "success " + preference.current_week + " year: " + preference.current_year);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                Log.e("TEST2", "fail");
            }
        });
    }
}