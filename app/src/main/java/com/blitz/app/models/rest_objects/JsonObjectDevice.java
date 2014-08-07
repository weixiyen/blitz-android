package com.blitz.app.models.rest_objects;

/**
 * Created by Miguel on 8/7/2014.
 */
public class JsonObjectDevice extends JsonObject {

    public static class Body {
        public String value;

        public Body(String value) {
            this.value = value;
        }
    }
}