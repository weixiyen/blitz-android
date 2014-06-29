package com.blitz.app.models.rest_objects;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class JsonObjectCode extends JsonObject {

    public static class Body {
        public String value;
    }

    public static class Result {
        public String code_type;
    }

    public Result result;
}