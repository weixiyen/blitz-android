package com.blitz.app.models.rest_objects;

/**
 * Created by mrkcsc on 7/10/14.
 */
public class JsonObjectQueue extends JsonObject {

    /**
     * Body parameters for POST request.
     */
    public static class Body {
        public String draft_key;

        public Body(String draft_key) {

            this.draft_key = draft_key;
        }
    }

    /**
     * Body parameters for PUT request.
     */
    public static class BodyPUT {
        public String draft_key;

        public BodyPUT(String draft_key) {

            this.draft_key = draft_key;
        }
    }
}