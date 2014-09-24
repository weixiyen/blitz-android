package com.blitz.app.utilities.rest;

import com.blitz.app.object_models.ObjectModelDraft;
import com.blitz.app.object_models.ObjectModelItem;
import com.blitz.app.object_models.ObjectModelStats;
import com.blitz.app.simple_models.Game;
import com.blitz.app.simple_models.Player;
import com.blitz.app.simple_models.Stat;
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
                    @Query("limit") Integer limit, Callback<RestAPIResult<ObjectModelDraft>> callback);

    @GET("/stats?index=player_year_week_index&keys[]=5836814e-a6cf-48b6-a186-f53a249d7800_2014_2&keys[]=797c2a60-f657-4eff-b5fa-69e613603683_2014_2&keys[]=c265fa5a-bb12-4b01-a82c-1a5e40179563_2014_2&keys[]=e567e957-d080-44e9-b374-c681e579b589_2014_2&keys[]=2c698afa-2416-4094-848f-85205f60d57e_2014_2&keys[]=3a87ac36-b508-482a-afec-b00d30b2e3e3_2014_2&keys[]=ba838253-78a4-4301-8acf-0c8b987b9005_2014_2&keys[]=4c1b074d-a3e6-459e-9c84-c9ad41a375b7_2014_2&keys[]=bda20d7b-5998-4770-99b2-52318fcdc2ac_2014_2&keys[]=62d37e17-7d17-4a2c-9298-88e481ea76a1_2014_2&keys[]=131123be-6b1e-450d-b4c1-ae22c2b5fda5_2014_2&keys[]=8177501c-515b-4d41-9fdb-2dd283920ba0_2014_2&keys[]=070855e5-50ff-4470-84a4-47995c3be532_2014_2&keys[]=a665d15d-2337-43b8-beb9-e7c5a2bdafdb_2014_2&keys[]=38ead747-e1e1-4bf5-a6de-b38c69c87a6c_2014_2&keys[]=2fbe9479-324d-4763-a538-148fa3d172fd_2014_2&keys[]=4c16ffa6-c591-42d1-b3b6-0b1ff3848665_2014_2&keys[]=8cfb1991-6047-44b9-aec2-6af67dffa4a2_2014_2&limit=100&pluck[]=player_id&pluck[]=type&pluck[]=value&pluck[]=week&pluck[]=year")
    void test_stats_get(Callback<RestAPIResult<ObjectModelStats>> listCallback);

    @GET("/nfl_games?index=year_week_index")
    void games_get(@Query("key") String key, Callback<RestAPIResult<Game>> callback);

    @GET("/nfl_players?index=id")
    void players_get(@Query("keys[]") List<String> playerIds,
                     Callback<RestAPIResult<Player>> listCallback);

    @GET("/item/{item_id}")
    void item_get(@Path("item_id") String itemId, Callback<RestAPIResult<ObjectModelItem>> callback);

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
    void stats_get(@Query("keys[]") List<String> keys,
                   @Query("index") String index,
                   @Query("pluck[]") List<String> pluck,
                   @Query("limit") Integer limit,
                   Callback<RestAPIResult<Stat>> callback);

    @GET("/user/{user_id}")
    void user_get(@Path("user_id") String userId, Callback<JsonObject> callback);

    @POST("/users")
    void users_post(@Body JsonObject body, Callback<JsonObject> callback);
}