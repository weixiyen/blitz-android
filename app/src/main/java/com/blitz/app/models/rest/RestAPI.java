package com.blitz.app.models.rest;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
@SuppressWarnings("UnusedDeclaration")
public interface RestAPI {

    //==============================================================================================
    // Public REST API Methods
    //==============================================================================================

    @GET("/access_queue/{device_id}")
    void access_queue(@Path("device_id") String deviceId, Callback<JsonObject> callback);

    @POST("/auth")
    void auth(@Body JsonObject body, Callback<JsonObject> callback);

    @GET("/preferences")
    void preferences(Callback<JsonObject> callback);

    @POST("/code")
    void code(@Body JsonObject body, Callback<JsonObject> callback);

    @GET("/device/{device_id}")
    void device(@Path("device_id") String deviceId, Callback<JsonObject> callback);

    @PATCH("/device/{device_id}")
    void device(@Path("device_id") String deviceId, @Body JsonObject body, Callback<JsonObject> callback);

    @POST("/devices")
    void devices(@Body JsonObject body, Callback<JsonObject> callback);

    @GET("/draft/{draft_id}")
    void draft(@Path("draft_id") String draftId, Callback<JsonObject> callback);

    @POST("/queue")
    void queue(@Body JsonObject body, Callback<JsonObject> callback);

    @DELETE("/queue/{draft_key}")
    void queue(@Path("draft_key") String draftKey, Callback<JsonObject> callback);

    @PUT("/queue")
    void queue_put(@Body JsonObject body, Callback<JsonObject> callback);

    @POST("/users")
    void users_post(@Body JsonObject body, Callback<JsonObject> callback);
}