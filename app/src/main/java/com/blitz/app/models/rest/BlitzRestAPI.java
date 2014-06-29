package com.blitz.app.models.rest;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Miguel Gaeta on 6/26/14.
 */
public interface BlitzRestAPI {

    /**
     * All API rest objects can potentially
     * contain an errors object.
     */
    public static class BaseApiObject {

        /**
         * Contains a list of strings
         * pertaining to the error.
         */
        public static class Errors {

            // Error string payload.
            public ArrayList<String> value;
        }

        // Errors object.
        public Errors errors;

        /**
         * Find out if any errors exist.
         *
         * @return True/false.
         */
        public boolean hasErrors() {

            return errors       != null &&
                   errors.value != null;
        }
    }

    public static class Code extends BaseApiObject {
        public static class Body {
            public String value;
        }

        public static class Result {
            public String code_type;
        }

        public Result result;
    }

    public static class Preference extends BaseApiObject {
        public String current_week;
        public String current_year;
    }

    @GET("/preferences")
    void preferences(
            Callback<Preference> callback);

    @POST("/code")
    void code(
            @retrofit.http.Body Code.Body body,
            Callback<Code> callback);
}