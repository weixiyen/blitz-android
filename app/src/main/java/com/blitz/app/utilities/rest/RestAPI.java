package com.blitz.app.utilities.rest;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Miguel Gaeta on 6/26/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public interface RestAPI {

    //==============================================================================================
    // Public REST API Methods
    //==============================================================================================

    @GET("/access_queue/{device_id}")
    void access_queue_get(@Path("device_id") String deviceId, Callback<JsonObject> callback);

    @GET("/achievements")
    void achievements_get(Callback<JsonObject> callback);

    @POST("/auth")
    void auth_post(@Body JsonObject body, Callback<JsonObject> callback);

    @DELETE("/auth")
    void auth_delete(@Body JsonObject body, Callback<JsonObject> callback);

    @GET("/preferences")
    void preferences_get(Callback<JsonObject> callback);

    @POST("/code")
    void code_post(@Body JsonObject body, Callback<JsonObject> callback);

    @GET("/device/{device_id}")
    void device_get(@Path("device_id") String deviceId, Callback<JsonObject> callback);

    @PATCH("/device/{device_id}")
    void device_patch(@Path("device_id") String deviceId, @Body JsonObject body, Callback<JsonObject> callback);

    @POST("/devices")
    void devices_post(@Body JsonObject body, Callback<JsonObject> callback);

    @GET("/draft/{draft_id}")
    void draft_get(@Path("draft_id") String draftId, Callback<JsonObject> callback);

    @GET("/drafts")
    void drafts_get(@Query("keys[]") ArrayList<String> keys,   // Required
                    @Query("pluck[]") ArrayList<String> pluck, // Optional
                    @Query("index") String index,              // Required
                    @Query("filter") String filter,            // Optional
                    @Query("order_by") String orderBy,         // Optional
                    @Query("limit") Integer limit, Callback<JsonObject> callback);

    @GET("/item/{item_id}")
    void item_get(@Path("item_id") String itemId, Callback<JsonObject> callback);

    @GET("/items")
    void items_get(@Query("keys[]") ArrayList<String> keys, // Required
                   @Query("index") String index,            // Required
                   @Query("filter") String filter,          // Optional
                   @Query("limit") Integer limit,           // Optional
                   Callback<JsonObject> callback);

    @POST("/queue")
    void queue_post(@Body JsonObject body, Callback<JsonObject> callback);

    @DELETE("/queue/{draft_key}")
    void queue_delete(@Path("draft_key") String draftKey, Callback<JsonObject> callback);

    @PUT("/queue")
    void queue_put(@Body JsonObject body, Callback<JsonObject> callback);

    @GET("/stats")
    void stats_get(@Query("keys[]") List<String> playerIds,
                   @Query("index")        String index,
                   @Query("pluck[]")      String pluck,
                   @Query("limit")        String limit,
                   Callback<JsonObject> callback);


    @GET("/user/{user_id}")
    void user_get(@Path("user_id") String userId, Callback<JsonObject> callback);

    @POST("/users")
    void users_post(@Body JsonObject body, Callback<JsonObject> callback);
}