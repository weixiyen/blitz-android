package com.blitz.app.models.objects;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectQueue;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
@SuppressWarnings("unused")
public class ObjectModelPreferences extends ObjectModel {

    public void TestCall(RestAPIOperation operation) {

        // Construct POST body.
        JsonObjectQueue.Body body = new JsonObjectQueue.Body("football_heads_up_draft_free");

        // Make rest call for code.
        RestAPIClient.getAPI().queue(body, new RestAPICallback<JsonObjectQueue>(mRestApiObject, operation));
    }
}