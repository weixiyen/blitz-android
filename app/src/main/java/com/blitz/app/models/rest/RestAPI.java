package com.blitz.app.models.rest;

import com.blitz.app.models.rest_objects.JsonObjectCode;
import com.blitz.app.models.rest_objects.JsonObjectPreference;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
public interface RestAPI {

    //==============================================================================================
    // Public REST API Methods
    //==============================================================================================

    @GET("/preferences")
    void preferences(
            Callback<JsonObjectPreference> callback);

    @POST("/code")
    void code(
            @retrofit.http.Body JsonObjectCode.Body body,

            Callback<JsonObjectCode> callback);
}