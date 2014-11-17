package com.blitz.app.rest_models;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.FieldMap;
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
interface RestAPI {

    // region Public REST API Methods
    // ============================================================================================================

    @GET("/access_queue/{device_id}")
    void access_queue_get(@Path("device_id") String deviceId,
        Callback<JsonObject> callback);

    @GET("/achievements")
    void achievements_get(
        Callback<JsonObject> callback);

    @POST("/auth")
    void auth_post(@Body JsonObject body,
        Callback<RestAPIResult<RestModelUser>> callback);

    @DELETE("/auth")
    void auth_delete(@Body JsonObject body,
        Callback<JsonObject> callback);

    @GET("/preferences")
    void preferences_get(
        Callback<RestModelPreferences> callback);

    @POST("/code")
    void code_post(@Body JsonObject body,
        Callback<JsonObject> callback);

    @GET("/device/{device_id}")
    void device_get(@Path("device_id") String deviceId,
        Callback<RestAPIResult<RestModelDevice>> callback);

    @PATCH("/device/{device_id}")
    void device_patch(@Path("device_id") String deviceId, @Body JsonObject body,
        Callback<RestAPIResult<RestModelDevice>> callback);

    @POST("/devices")
    void devices_post(@Body JsonObject body,
        Callback<RestAPIResult<RestModelDevice>> callback);

    @GET("/draft/{draft_id}")
    void draft_get(@Path("draft_id") String draftId,
                   Callback<RestModelDraft> callback); // TODO: Result format needs to be changed to
                                                         // TODO: RestAPIResult format, but requires clients
                                                         // TODO: do the update together.

    @GET("/drafts")
    void drafts_get(@Query("keys[]") List<String> keys,   // Required
                    @Query("pluck[]") List<String> pluck, // Optional
                    @Query("index") String index,         // Required
                    @Query("filter") String filter,       // Optional
                    @Query("order_by") String orderBy,    // Optional
                    @Query("limit") Integer limit,        // Optional
        Callback<RestAPIResult<RestModelDraft>> callback);

    @PATCH("/draft/{draft_id}")
    void draft_patch(@Path("draft_id") String draftId, @Body JsonObject body,
        Callback<RestAPIResult<RestModelDraft>> callback);

    @GET("/group/{group_id}")
    void group_get(@Path("group_id") String groupId,
        Callback<RestAPIResult<RestModelGroup>> callback);

    @PATCH("/group/{group_id}")
    void group_patch(@Path("group_id") String groupId, @Body JsonObject body,
        Callback<RestAPIResult<RestModelGroup>> callback);

    @GET("/groups")
    void groups_get(@Query("index") String index,             // Required
                    @Query("keys[]") List<String> keys,       // Optional
                    @Query("pluck[]") List<String> pluck,     // Optional
                    @Query("between[]") List<String> between, // Optional
                    @Query("filter") String filter,           // Optional
                    @Query("order_by") String orderBy,        // Optional
                    @Query("limit") Integer limit,            // Optional
        Callback<RestAPIResult<RestModelGroup>> callback);

    @POST("/groups")
    void groups_post(@Body JsonObject body,
        Callback<RestAPIResult<RestModelGroup>> callback);

    @GET("/nfl_games?index=year_week_index")
    void games_get(@Query("key") String key,
        Callback<RestAPIResult<RestModelGame>> callback);

    @GET("/nfl_player/{player_id}")
    void nfl_player_get(@Path("player_id") String playerId,
        Callback<RestAPIResult<RestModelPlayer>> callback);

    @GET("/nfl_players")
    void nfl_players_get(@Query("keys[]") List<String> playerIds, // Required
                         @Query("index") String index,            // Required
        Callback<RestAPIResult<RestModelPlayer>> listCallback);

    @GET("/item/{item_id}")
    void item_get(@Path("item_id") String itemId,
        Callback<RestAPIResult<RestModelItem>> callback);

    @GET("/items")
    void items_get(@Query("keys[]") List<String> keys, // Required
                   @Query("index") String index,       // Required
                   @Query("filter") String filter,     // Optional
                   @Query("limit") Integer limit,      // Optional
        Callback<RestAPIResult<RestModelItem>> callback);

    @POST("/queue")
    void queue_post(@Body JsonObject body,
        Callback<JsonObject> callback);

    @DELETE("/queue/{draft_key}")
    void queue_delete(@Path("draft_key") String draftKey,
        Callback<JsonObject> callback);

    @PUT("/queue")
    void queue_put(@Body JsonObject body,
        Callback<JsonObject> callback);

    @GET("/stats")
    void stats_get(@Query("keys[]") List<String> keys,   // Required
                   @Query("index") String index,         // Required
                   @Query("pluck[]") List<String> pluck, // Optional
                   @Query("limit") Integer limit,        // Optional
        Callback<RestAPIResult<RestModelStats>> callback);

    @GET("/transactions")
    void transactions_get(@Query("keys[]") List<String> userIds,
                          @Query("index") String index,
                          @Query("order_by") String orderBy,
                          @Query("limit") int limit,
                          Callback<RestAPIResult<RestModelTransaction>> transaction);

    @POST("/transaction")
    void transaction_post(@FieldMap Map<String,String> params,
                          Callback<RestAPIResult<RestModelTransaction>> callback);


    @GET("/transaction_token")
    void transaction_token_get(Callback<RestAPIResult<RestModelTransaction>> transaction);

    @GET("/user/{user_id}")
    void user_get(@Path("user_id") String userId,
        Callback<RestAPIResult<RestModelUser>> callback);

    @PATCH("/user")
    void user_patch(@Body JsonObject body,
                    Callback<RestAPIResult<RestModelUser>> callback);

    @GET("/users")
    void users_get(@Query("keys[]") List<String> keys,   // Required
                   @Query("index") String index,         // Required
                   @Query("pluck[]") List<String> pluck, // Optional
                   @Query("order_by") String orderBy,    // Optional
                   @Query("limit") Integer limit,        // Optional
        Callback<RestAPIResult<RestModelUser>> callback);

    @POST("/users")
    void users_post(@Body JsonObject body,
        Callback<RestAPIResult<RestModelUser>> callback);

    // endregion
}