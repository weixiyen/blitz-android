package com.blitz.app.models.rest;

import com.blitz.app.models.rest_objects.JsonObjectAuth;
import com.blitz.app.models.rest_objects.JsonObjectCode;
import com.blitz.app.models.rest_objects.JsonObjectDraft;
import com.blitz.app.models.rest_objects.JsonObjectPreference;
import com.blitz.app.models.rest_objects.JsonObjectQueue;
import com.blitz.app.models.rest_objects.JsonObjectUsers;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

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
    void code(@Body JsonObjectCode.Body body,

               Callback<JsonObjectCode> callback);

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