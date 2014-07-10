package com.blitz.app.models.rest_objects;

/**
 * Created by mrkcsc on 7/9/14.
 */
public class JsonObjectUsers extends JsonObject {

    public static class Body {
        public String email;
        public String username;
        public String password;

        public Body(String email, String username, String password) {

            this.email = email;
            this.username = username;
            this.password = password;
        }
    }

    public static class Result {
        public String id;
        public String username;
    }

    public Result result;
}