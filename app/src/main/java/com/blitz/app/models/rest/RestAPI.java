package com.blitz.app.models.rest;

import com.blitz.app.models.rest_objects.JsonObjectAccessQueue;
import com.blitz.app.models.rest_objects.JsonObjectAuth;
import com.blitz.app.models.rest_objects.JsonObjectCode;
import com.blitz.app.models.rest_objects.JsonObjectDevice;
import com.blitz.app.models.rest_objects.JsonObjectDraft;
import com.blitz.app.models.rest_objects.JsonObjectPreference;
import com.blitz.app.models.rest_objects.JsonObjectQueue;
import com.blitz.app.models.rest_objects.JsonObjectUsers;

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
    void access_queue(@Path("device_id") String deviceId, Callback<JsonObjectAccessQueue> callback);

    @GET("/preferences")
    void preferences(

               Callback<JsonObjectPreference> callback);

    @POST("/code")
    void code(@Body JsonObjectCode.Body body,

               Callback<JsonObjectCode> callback);

    @PATCH("/device/{device_id}")
    void device(@Path("device_id") String deviceId, @Body JsonObjectDevice.Body body, Callback<JsonObjectDevice> callback);

    @POST("/devices")
    void devices(@Body JsonObjectDevice.Body body, Callback<JsonObjectDevice> callback);

    @GET("/device/{device_id}")
    void device(@Path("device_id") String deviceId, Callback<JsonObjectDevice> callback);

    @POST("/users")
    void users(@Body JsonObjectUsers.Body body,

               Callback<JsonObjectUsers> callback);

    @POST("/queue")
    void queue(@Body JsonObjectQueue.Body body,

               Callback<JsonObjectQueue> callback);

    @DELETE("/queue/{draft_key}")
    void queue(@Path("draft_key") String draftKey,

               Callback<JsonObjectQueue> callback);

    @PUT("/queue")
    void queue(@Body JsonObjectQueue.BodyPUT body,

               Callback<JsonObjectQueue> callback);

    @POST("/auth")
    void auth(@Body JsonObjectAuth.Body body,

               Callback<JsonObjectAuth> callback);

    @GET("/draft/{draft_id}")
    void draft(@Path("draft_id") String draftId,

               Callback<JsonObjectDraft> callback);
}