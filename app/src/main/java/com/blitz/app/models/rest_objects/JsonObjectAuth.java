package com.blitz.app.models.rest_objects;

/**
 * Created by mrkcsc on 7/12/14.
 */
public class JsonObjectAuth extends JsonObject {

    public static class Body {
        public String username;
        public String password;

        public Body(String username, String password) {

            this.username = username;
            this.password = password;
        }
    }

    public static class User {
        public String id;
        public String username;
    }

    public User user;
}
