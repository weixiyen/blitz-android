package com.blitz.app.models.api;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
public interface BlitzAPI {

    public static class Preference {
        public String current_week;
        public String current_year;
    }

    @GET("/preferences")
    void preferences(
            Callback<Preference> callback
    );
}