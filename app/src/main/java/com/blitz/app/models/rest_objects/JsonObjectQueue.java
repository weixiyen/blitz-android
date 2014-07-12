package com.blitz.app.models.rest_objects;

/**
 * Created by mrkcsc on 7/10/14.
 */
public class JsonObjectQueue extends JsonObject {

    public static class Body {
        public String draft_key;

        public Body(String draft_key) {

            this.draft_key = draft_key;
        }
    }

    public static class Result {

    }

    public Result result;
}